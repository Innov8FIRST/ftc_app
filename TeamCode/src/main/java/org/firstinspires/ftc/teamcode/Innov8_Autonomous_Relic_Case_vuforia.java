package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Innov8_Autonomous_Relic_vuforia", group = "Auto")

public class Innov8_Autonomous_Relic_Case_vuforia extends LinearOpMode { // Blue 1

    /* Declare OpMode members. */
    HardwareInnov8Bot robot = new HardwareInnov8Bot();   // Use a Innov8's hardware

    /*Declare variables */
    double startPositionR = 0;
    double startPositionL = 0;
    double endPositionR = 0;
    double endPositionL = 0;
    double MID_SERVO = 0.3;
    double START_SERVO = 0;
    double END_SERVO = 0.9;
    int Time = 0;
    int FDrive = 2;
    int teamColor = 1;   //0 = red   or 1 = blue
    int taskNumber = 0;   //used to determine the step that should be executed

    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);                                // Servo mid position

        startPositionR = robot.rightMotor.getCurrentPosition();
        startPositionL = robot.leftMotor.getCurrentPosition();
        telemetry.addData("startR", startPositionR);
        telemetry.addData("startL", startPositionL);
        telemetry.addData("CurrentR", robot.rightMotor.getCurrentPosition());
        telemetry.addData("CurrentL", robot.leftMotor.getCurrentPosition());
        telemetry.addData("EndR", endPositionR);
        telemetry.addData("EndL", endPositionL);
        telemetry.addData("Fruity1Blue", robot.FruitySensor.blue());
        telemetry.addData("Fruity1Red", robot.FruitySensor.red());
        telemetry.addData("Fruity1Green", robot.FruitySensor.green());
        telemetry.update();

        robot.jewelArm.setPosition(START_SERVO);
        robot.leftJewelArm.setPosition(END_SERVO);
        robot.bigfoot.setPosition(0);

        waitForStart();


        while (opModeIsActive() && taskNumber != 9999) {

            switch (taskNumber) {
                case 0: // Drop jewel arm and check for color

                    if (teamColor == 0) {
                        robot.jewelArm.setPosition(MID_SERVO);
                        telemetry.addData("Fruity1Blue", robot.FruitySensor.blue());
                        telemetry.addData("Fruity1Red", robot.FruitySensor.red());
                        telemetry.addData("Fruity1Green", robot.FruitySensor.green());
                        telemetry.addData("Time", Time);
                        telemetry.update();
                    } else {
                        robot.leftJewelArm.setPosition(MID_SERVO);
                        telemetry.addData("Fruity1Blue", robot.FruitySensor.blue());
                        telemetry.addData("Fruity1Red", robot.FruitySensor.red());
                        telemetry.addData("Fruity1Green", robot.FruitySensor.green());
                        telemetry.addData("Time", Time);
                        telemetry.update();
                    }
                    taskNumber = 3;
                    break;
                case 1: // Drive backwards 4 inches
                    endPositionR = startPositionR - 200;
                    endPositionL = startPositionL - 200;
                    while (opModeIsActive() && robot.leftMotor.getCurrentPosition() >= endPositionL) {
                        robot.rightMotor.setPower(-40);
                        robot.leftMotor.setPower(-40);
                    }
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);
                    taskNumber = 9999;
                    break;

                case 2: // Drive forwards 4 inches
                    telemetry.addData("Move:", "Forward");
                    startPositionR = robot.rightMotor.getCurrentPosition();
                    telemetry.addData("Start", startPositionR);
                    telemetry.update();
                    endPositionR = startPositionR + 200;
                    endPositionL = startPositionL - 200;
                    while (opModeIsActive() && robot.rightMotor.getCurrentPosition() <= endPositionR) {
                        robot.rightMotor.setPower(40);
                        robot.leftMotor.setPower(-40);
                        telemetry.addData("Current", robot.rightMotor.getCurrentPosition());
                    }
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);
                    break;

                case 3: // wait for 5 seconds
                    while (opModeIsActive() && Time < 50) {
                        Time = Time + 1;
                        telemetry.addData("Time", Time);
                        telemetry.addData("Blue", robot.FruitySensor.blue());
                        telemetry.addData("Red", robot.FruitySensor.red());
                        telemetry.addData("Green", robot.FruitySensor.green());
                        telemetry.update();
                    }
                    taskNumber = 4;
                    break;

                case 4: // determine jewel color and set

                    if (teamColor == 0) {
                        if (robot.FruitySensor.red() >= 6) {
                            taskNumber = 1;
                        } else {
                            taskNumber = 2;
                        }
                    } else {
                        if (robot.FruitySensor.red() >= 6) {
                            taskNumber = 2;
                        } else {
                            taskNumber = 1;
                        }
                    }

                    telemetry.addData("Task: ", taskNumber);
                    telemetry.update();
                    break;

                default: // stop the program in case default

                    taskNumber = 9999;

                    break;
            }
        }
    }
}