package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "BlueStraight", group = "Auto")

public class BlueStraight extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareInnov8Auto robot = new HardwareInnov8Auto();   // Use a Innov8's hardware

    /*Declare variables */
    double startPositionR = 0; // Establishing starting positions
    double startPositionL = 0;
    double endPositionR = 0; // Estalishing current end positions - should be 0
    double endPositionL = 0;
    double MID_SERVO = 0.3; // Mid position of the servo range
    double START_SERVO = 0; // "Down" position of the paddles
    double END_SERVO = 0.9; // "Up" position of the paddles
    int Time = 0; // Establishing time counter
    int FDrive = 2; // Changes speed that DC motors move at
    int teamColor = 1;   // 0 = red   or 1 = blue
    int taskNumber = 0;   // Used to determine the step that should be executed
    int multR = -1; // Multipliers used to change speed of servos if they are not moving the same
    int multL = -1;

    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);

        // startPositionR = robot.rightMotor.getCurrentPosition(); // Current positions of DC motors - 0
        // startPositionL = robot.leftMotor.getCurrentPosition();
        // telemetry.addData("startR", startPositionR); // Current start positions of DC motors - 0
        // telemetry.addData("startL", startPositionL);
        // telemetry.addData("CurrentR", robot.rightMotor.getCurrentPosition()); // Current position of DC motors
        // telemetry.addData("CurrentL", robot.leftMotor.getCurrentPosition());
        // telemetry.addData("EndR", endPositionR); // End positions for motors
        // telemetry.addData("EndL", endPositionL);
        telemetry.addData("Fruity1Blue", robot.FruitySensor.blue()); // Color sensor values
        telemetry.addData("Fruity1Red", robot.FruitySensor.red());
        telemetry.addData("Orangey_Red", robot.OrangeySensor.red());
        telemetry.addData("Orangey_Blue", robot.OrangeySensor.blue());
        telemetry.update();

        robot.jewelArm.setPosition(END_SERVO); // Setting paddle servos to "up" position
        robot.leftJewelArm.setPosition(END_SERVO);
        robot.bigfoot.setPosition(START_SERVO);

        waitForStart();

        while (opModeIsActive() && taskNumber != 9999) {

            switch (taskNumber) {
                case 0: // Drop jewel arm and check for color

                    if (teamColor == 0) { // If blue, drop right paddle and check color sensor
                        telemetry.addData("Case", taskNumber);
                        robot.jewelArm.setPosition(0.1);
                        telemetry.addData("FruityBlue", robot.FruitySensor.blue());
                        telemetry.addData("FruityRed", robot.FruitySensor.red());
                        telemetry.addData("Orangey_Red", robot.OrangeySensor.red());
                        telemetry.addData("Orangey_Blue", robot.OrangeySensor.blue());
                        telemetry.addData("snickersnack", Time);
                        telemetry.update();
                    } else { // If red, drop left paddle and check color sensor
                        telemetry.addData("Case", taskNumber);
                        robot.leftJewelArm.setPosition(0.34);
                        telemetry.addData("FruityBlue", robot.FruitySensor.blue());
                        telemetry.addData("FruityRed", robot.FruitySensor.red());
                        telemetry.addData("Orangey_Red", robot.OrangeySensor.red());
                        telemetry.addData("Orangey_Blue", robot.OrangeySensor.blue());
                        telemetry.addData("snickersnack", Time);
                        telemetry.update();
                    }
                    taskNumber = 3;
                    break;

                case 1: // Drive backwards 4 inches, team color is not relevant
                    telemetry.addData("Case", taskNumber);
                    Time = 0;
                    while (opModeIsActive() && Time < 200) {
                        robot.rightMotor.setPower(-40 * multR);
                        robot.leftMotor.setPower(-40 * multL);
                        Time = Time + 1;
                    }
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);
                    robot.leftJewelArm.setPosition(END_SERVO);
                    robot.jewelArm.setPosition(END_SERVO);
                    taskNumber = 9999;
                    break;

                case 2: // Drive forwards 4 inches, team color is not relevant
                    telemetry.addData("Case", taskNumber);
                    // startPositionR = robot.rightMotor.getCurrentPosition();
                    // telemetry.addData("Start", startPositionR);
                    telemetry.update();
                    Time = 0;
                    while (opModeIsActive() && Time < 200) {
                        robot.rightMotor.setPower(40 * multR);
                        robot.leftMotor.setPower(40 * multL);
                        Time = Time + 1;
                    }
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);
                    robot.leftJewelArm.setPosition(END_SERVO);
                    robot.jewelArm.setPosition(END_SERVO);
                    Time = 0;
                    while (opModeIsActive() && Time < 20) { // Wait for arm to raise
                        Time = Time + 1;
                    }
                    taskNumber = 6;
                    break;

                case 3: // Wait for 5 seconds
                    telemetry.addData("Case", taskNumber);
                    while (opModeIsActive() && Time < 50) {
                        Time = Time + 1;
                        telemetry.addData("snickersnack", Time);
                        telemetry.addData("Blue", robot.FruitySensor.blue());
                        telemetry.addData("Red", robot.FruitySensor.red());
                        telemetry.addData("Green", robot.FruitySensor.green());
                        telemetry.update();
                    }
                    taskNumber = 4;
                    break;

                case 4: // Determine jewel color and set forward/backward
                    telemetry.addData("Case", taskNumber);
                    if (teamColor == 1) { // If blue, decide to go forward or backward
                        if (robot.OrangeySensor.red() >= 115 && robot.OrangeySensor.blue() <= 40) {
                            taskNumber = 1; // Move backwards
                        }
                        if (robot.OrangeySensor.red() <= 90 && robot.OrangeySensor.blue() >= 105) {
                            taskNumber = 2; // Move forwards
                        } else
                            robot.leftJewelArm.setPosition(END_SERVO);  // Raise the arm
                        taskNumber = 6;
                    } else {                  //If team color is red
                        if (robot.FruitySensor.red() >= 180 && robot.FruitySensor.blue() <= 65) {
                            taskNumber = 2; // Move backwards
                        }
                        if (robot.FruitySensor.red() <= 100 && robot.FruitySensor.blue() >= 155) {
                            taskNumber = 1; // Move forwards
                        } else
                            robot.jewelArm.setPosition(END_SERVO);
                        taskNumber = 6;
                    }
                    telemetry.addData("Task: ", taskNumber);
                    telemetry.update();
                    break;

                default: // Stop the program in case default
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);
                    break;

                case 5: // Move forward
                    telemetry.addData("Case", taskNumber);
                    telemetry.addData("Move:", "Forward");
                    // startPositionR = robot.rightMotor.getCurrentPosition();
                    // telemetry.addData("Start", startPositionR);
                    telemetry.update();
                    Time = 0;
                    while (opModeIsActive() && Time < 700) {
                        robot.rightMotor.setPower(50 * multR);
                        robot.leftMotor.setPower(50 * multL);
                        Time = Time + 1;
                    }
                    if (teamColor == 0) {
                        taskNumber = 7;
                    } else {
                        taskNumber = 11;
                    }
                    break;

                case 6: // Move forward
                    // startPositionR = robot.rightMotor.getCurrentPosition();
                    // telemetry.addData("Start", startPositionR);
                    Time = 0;
                    while (opModeIsActive() && Time < 500) {
                        Time = Time + 1;
                        robot.rightMotor.setPower(40 * multR);
                        robot.leftMotor.setPower(40 * multL);
                        telemetry.addData("Time",Time);
                        telemetry.update();
                    }
                    if (teamColor == 0) {
                        taskNumber = 7;
                    } else {
                        taskNumber = 11;
                    }
                    break;

                case 7: // First turn
                    //startPositionR = robot.rightMotor.getCurrentPosition();
                    //endPositionR = startPositionR + (500 * multR);
                    Time = 0;
                    while (opModeIsActive() && Time < 300) {
                        robot.rightMotor.setPower(40 * multR);
                        robot.leftMotor.setPower(-40 * multL);
                        Time = Time + 1;
                    }

                    /*
                    robot.leftMotor.setPower(0);
                    robot.rightMotor.setPower(0); */
                    taskNumber = 8;
                    break;

                case 8: // Drive forwards 4 inches, team color is not relevant
                    telemetry.addData("Case", taskNumber);
                    startPositionR = robot.rightMotor.getCurrentPosition();
                    telemetry.addData("Start", startPositionR);
                    telemetry.update();
                    Time = 0;
                    while (opModeIsActive() && Time < 200) {
                        robot.rightMotor.setPower(40 * multR);
                        robot.leftMotor.setPower(40 * multL);
                        Time = Time + 1;
                    }
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);
                    taskNumber = 9;
                    break;

                case 9: // Second turn

                    //startPositionR = robot.rightMotor.getCurrentPosition();
                    //endPositionR = startPositionR + (500 * multR);
                    Time = 0;
                    while (opModeIsActive() && Time < 300) {
                        robot.rightMotor.setPower(-40 * multR);
                        robot.leftMotor.setPower(40 * multL);
                        Time = Time + 1;
                        taskNumber = 10;
                    }
                        /* robot.leftMotor.setPower(0);
                        robot.rightMotor.setPower(0); */
                    taskNumber = 10;
                    break;

                case 10: // Drive forwards 4 inches, team color is not relevant
                    telemetry.addData("Case", taskNumber);
                    startPositionR = robot.rightMotor.getCurrentPosition();
                    telemetry.addData("Start", startPositionR);
                    telemetry.update();
                    Time = 0;
                    while (opModeIsActive() && Time < 200) {
                        robot.rightMotor.setPower(40 * multR);
                        robot.leftMotor.setPower(40 * multL);
                        Time = Time + 1;
                    }
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);
                    taskNumber = 11;
                    break;

                /* case 10:
                    Time = 0;
                    while (opModeIsActive() && Time < 700) {
                        robot.rightMotor.setPower(40*multR);
                        robot.leftMotor.setPower(40*multL);
                    }
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);
                    taskNumber = 11;
                    break; */

                case 11:
                    robot.bigfoot.setPosition(MID_SERVO);
                    taskNumber = 12;
                    break;

                case 12: // Drive backwards 2 inches, team color is not relevant
                    telemetry.addData("Case", taskNumber);
                    Time = 0;
                    while (opModeIsActive() && Time < 100) {
                        robot.rightMotor.setPower(-40 * multR);
                        robot.leftMotor.setPower(-40 * multL);
                        Time = Time + 1;
                    }
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);
                    taskNumber = 9999;
                    break;

            }
        }
    }
}
