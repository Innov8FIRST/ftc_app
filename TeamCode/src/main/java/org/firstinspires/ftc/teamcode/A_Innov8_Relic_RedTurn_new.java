package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "A_Innov8_Relic_RedTurn_new", group = "Auto")

public class A_Innov8_Relic_RedTurn_new extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareInnov8Auto robot = new HardwareInnov8Auto();   // Use a Innov8's hardware

    /*Declare variables */
    double startPositionR = 0; // Declaring start positions for right and left motors
    double startPositionL = 0;
    double endPositionR = 0; // Declaring end positions for right and left motor
    double endPositionL = 0;
    double MID_SERVO = 0.3; // Middle of servo range
    double START_SERVO = 0; // "Down" positions of paddle servos
    double END_SERVO = 0.9; // "Up" positions of paddle servos
    int Time = 0; // Establishing time counter
    int FDrive = 2; // Changes speed that DC motors move at
    int teamColor = 0;   // 0 = red   or 1 = blue
    int taskNumber = 0;   // used to determine the step that should be executed
    int preNumber = 0;
    int NextTaskNumber = 9999;
    int multR = 1; // Multiplier - Change to 1 or -1 if encoders are backwards
    int multL = 1;
    double correctL = 1; // If one drive motor is moving faster than other - this makes sure robot drives straight
    double correctR = 0.97;

    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);

        startPositionR = robot.rightMotor.getCurrentPosition(); // Setting start positions to 0
        startPositionL = robot.leftMotor.getCurrentPosition();
        telemetry.addData("startR", startPositionR); // Making sure start positions are at 0
        telemetry.addData("startL", startPositionL);
        telemetry.addData("CurrentR", robot.rightMotor.getCurrentPosition()); // Keeping a counter for current position
        telemetry.addData("CurrentL", robot.leftMotor.getCurrentPosition());
        telemetry.addData("EndR", endPositionR); // Showing current "end" values - should be 0
        telemetry.addData("EndL", endPositionL);
        telemetry.addData("Fruity1Blue", robot.FruitySensor.blue()); // Updating telemetry for color sensors
        telemetry.addData("Fruity1Red", robot.FruitySensor.red());
        telemetry.addData("Orangey_Red", robot.OrangeySensor.red());
        telemetry.addData("Orangey_Blue", robot.OrangeySensor.blue());
        telemetry.update();

        robot.jewelArm.setPosition(1); // Setting paddles to "up"
        robot.leftJewelArm.setPosition(1);
        robot.bigfoot.setPosition(0.1);

        waitForStart();

        while (opModeIsActive() && taskNumber != 9999) {

            switch (taskNumber) {
                case 0: // Drop jewel arm and check for color
                    if (teamColor == 1) {   // If blue team, then drop left arm to check for color
                        telemetry.addData("current", taskNumber);
                        robot.jewelArm.setPosition(0.5);
                        telemetry.addData("FruityBlue", robot.FruitySensor.blue());
                        telemetry.addData("FruityRed", robot.FruitySensor.red());
                        telemetry.addData("Orangey_Red", robot.OrangeySensor.red());
                        telemetry.addData("Orangey_Blue", robot.OrangeySensor.blue());
                        telemetry.addData("Time", Time);
                        telemetry.update();
                    }

                    else {  // If red team, then drop right arm to check for color
                        telemetry.addData("current", taskNumber);
                        robot.leftJewelArm.setPosition(0);
                        telemetry.addData("FruityBlue", robot.FruitySensor.blue());
                        telemetry.addData("FruityRed", robot.FruitySensor.red());
                        telemetry.addData("Orangey_Red", robot.OrangeySensor.red());
                        telemetry.addData("Orangey_Blue", robot.OrangeySensor.blue());
                        telemetry.addData("Time", Time);
                        telemetry.update();
                    }

                    preNumber  =0;
                    taskNumber = 3;
                    NextTaskNumber = 4;
                break;

                case 1: // Drive backwards 4 inches - This code is same for both team colors
                    telemetry.addData("current", taskNumber);
                    Time = 0;
                    startPositionL = robot.leftMotor.getCurrentPosition();
                    endPositionL = startPositionL + 1000;

                    while (opModeIsActive() && robot.leftMotor.getCurrentPosition() < endPositionL) {

                        Time = Time + 1;
                        robot.rightMotor.setPower(10*multR*correctR);
                        robot.leftMotor.setPower(10*multL*correctL);
                        telemetry.addData("Time", Time);
                        telemetry.addData("next", NextTaskNumber);
                        telemetry.addData("current", taskNumber);
                        telemetry.update();

                    }
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);

                    Time = 0;  // Timer to check for color

                    while (opModeIsActive() && Time < 1000) {
                        Time = Time + 1;
                        telemetry.addData("Case", taskNumber);
                        telemetry.addData("Time", Time);
                        telemetry.addData("current", taskNumber);
                        telemetry.addData("Blue", robot.FruitySensor.blue());
                        telemetry.addData("Red", robot.FruitySensor.red());
                        telemetry.addData("Orangey_Red", robot.OrangeySensor.red());
                        telemetry.addData("Orangey_Blue", robot.OrangeySensor.blue());
                        telemetry.update();
                    }

                    taskNumber =3;
                    NextTaskNumber = 9999;  // Stop code so robot doesn't move forward after falling off balance board
                    preNumber  =1;

                break;

                case 2: // Drive forwards 4 inches - This is same for both teams
                    telemetry.addData("Case", taskNumber);
                    telemetry.addData("current", taskNumber);
                    startPositionL = robot.leftMotor.getCurrentPosition();
                    telemetry.addData("Start", startPositionL);
                    telemetry.update();

                    Time = 0;

                    startPositionL = robot.leftMotor.getCurrentPosition();
                    endPositionL = startPositionL - 200;

                    while (opModeIsActive() && robot.leftMotor.getCurrentPosition() > endPositionL) {

                        Time = Time + 1;
                        robot.rightMotor.setPower(-2*multR*correctR);
                        robot.leftMotor.setPower(-10*multL*correctL);
                        telemetry.addData("Time", Time);
                        telemetry.addData("next", NextTaskNumber);
                        telemetry.addData("current", taskNumber);
                        telemetry.update();
                    }

                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);
                    robot.jewelArm.setPosition(END_SERVO);

                    taskNumber =3;
                    NextTaskNumber = 6;
                    preNumber  =2;

                    break;

                case 3: // Wait for 5 seconds - This is same for both teams
                       telemetry.addData("Case", taskNumber);
                       Time = 0;

                       while (opModeIsActive() && Time < 60) {
                                Time = Time + 1;
                                telemetry.addData("Time", Time);
                                telemetry.addData("Previous", preNumber);
                                telemetry.addData("current", taskNumber);
                                telemetry.addData("Next", NextTaskNumber);
                                telemetry.addData("right", robot.rightMotor.getPower());
                                telemetry.addData("left", robot.leftMotor.getPower());
                                telemetry.addData("Blue", robot.FruitySensor.blue());
                                telemetry.addData("Red", robot.FruitySensor.red());
                                telemetry.addData("Orangey_Red", robot.OrangeySensor.red());
                                telemetry.addData("Orangey_Blue", robot.OrangeySensor.blue());
                                telemetry.addData("jewel", robot.jewelArm.getPosition());
                                telemetry.addData("leftjewel", robot.leftJewelArm.getPosition());
                                telemetry.update();
                       }

                       taskNumber = NextTaskNumber;
                break;

                case 4: // determine jewel color and set case; forward or backward
                    preNumber  =4;

                       telemetry.addData("Case", taskNumber);
                       if (teamColor == 0) {  // If team red, then make decision based on color sensor
                           if (robot.OrangeySensor.blue() <= 85 && robot.OrangeySensor.red() >= 120) {
                                    taskNumber = 3;
                                    NextTaskNumber = 1; // Will drive backwards to knock jewel
                           }

                           if (robot.OrangeySensor.blue() >=85 && robot.OrangeySensor.red() <= 120) {
                                    taskNumber = 3;
                                    NextTaskNumber = 2; // Will drive forwards to knock jewel
                           }

                           else {
                                robot.leftJewelArm.setPosition(END_SERVO);
                                robot.jewelArm.setPosition(END_SERVO);
                                taskNumber = 3;
                                NextTaskNumber = 6;
                               preNumber  =4;

                           }
                       }

                       else {  // If team red, then make decision based on color sensor
                           if(robot.FruitySensor.red() <= robot.FruitySensor.blue()) {
                                    taskNumber = 3;
                                    NextTaskNumber = 1; // Will drive backwards to knock jewel
                           }

                           if(robot.FruitySensor.blue() <= robot.FruitySensor.red()) {
                                    taskNumber = 3;
                                    NextTaskNumber = 2; // Will drive forwards to knock jewel
                           }

                           else {
                                robot.leftJewelArm.setPosition(END_SERVO);
                                robot.jewelArm.setPosition(END_SERVO);
                                taskNumber =3;
                                NextTaskNumber = 6;
                           }
                       }

                           telemetry.addData("Task: ", taskNumber);
                           telemetry.update();
                       break;

                       case 5:   // Moving the robot forward 1 foot if it previously moved backward
                            robot.jewelArm.setPosition(1);  // Same for both red and blue
                            telemetry.addData("Case", taskNumber);
                            Time = 0;

                            while (opModeIsActive() && Time < 1000) {  // Timer
                                Time = Time + 1;
                                robot.rightMotor.setPower(0);
                                robot.leftMotor.setPower(0);
                                telemetry.addData("current", taskNumber);
                                telemetry.addData("Time", Time);
                                telemetry.addData("Time", Time);
                                telemetry.addData("Blue", robot.FruitySensor.blue());
                                telemetry.addData("Red", robot.FruitySensor.red());
                                telemetry.addData("Orangey_Red", robot.OrangeySensor.red());
                                telemetry.addData("Orangey_Blue", robot.OrangeySensor.blue());
                                telemetry.update();
                            }

                            startPositionL = robot.leftMotor.getCurrentPosition();
                            endPositionL = startPositionL - 400;

                            telemetry.addData("Case", taskNumber);
                            telemetry.addData("Move:", "Forward");
                            telemetry.addData("Start", startPositionL);
                            telemetry.update();

                            Time = 0;

                            while (opModeIsActive () && robot.leftMotor.getCurrentPosition() > endPositionL) {
                                Time = Time + 1;
                                telemetry.addData("current", taskNumber);
                                robot.rightMotor.setPower(-10*multR*correctR);
                                robot.leftMotor.setPower(-10*multL*correctL);
                                telemetry.update();
                            }

                            robot.rightMotor.setPower(0);
                            robot.leftMotor.setPower(0);

                           preNumber  =5;
                           taskNumber =3;
                            NextTaskNumber = 7;
                break;

                case 6: // Moving the robot forward if it previously moved forward - Same for both teams

                       telemetry.addData("current", taskNumber);
                            telemetry.addData("Move:", "Forward");
                            telemetry.addData("Start", startPositionL);
                            telemetry.update();

                            Time = 0;

                            startPositionL = robot.leftMotor.getCurrentPosition();
                            endPositionL = startPositionL - 1200;

                            while (opModeIsActive() && robot.leftMotor.getCurrentPosition() > endPositionL) {
                                Time = Time + 1;
                                telemetry.addData("current", taskNumber);
                                robot.rightMotor.setPower(-10*multR*correctR);
                                robot.leftMotor.setPower(-10*multL*correctL);
                                telemetry.update();
                            }

                            robot.rightMotor.setPower(0);
                            robot.leftMotor.setPower(0);

                            taskNumber=3;
                            NextTaskNumber = 7;
                            preNumber  =6;

                       break;

                       case 7: // Moves the robot forward into the safe zone - Preparing to turn
                            telemetry.addData("current", taskNumber);
                            telemetry.update();

                            Time = 0;

                            startPositionL = robot.leftMotor.getCurrentPosition();
                            endPositionL = startPositionL - 600;

                            while (opModeIsActive() && robot.leftMotor.getCurrentPosition() > endPositionL) {
                                Time = Time + 1;
                                telemetry.addData("current", taskNumber);
                                robot.rightMotor.setPower(10*multR*correctR);
                                robot.leftMotor.setPower(-10*multL*correctL);
                                telemetry.update();
                            }

                            robot.rightMotor.setPower(0);
                            robot.leftMotor.setPower(0);

                           preNumber  =7;

                           taskNumber =3;
                            NextTaskNumber = 8;
                       break;

                       case 8: // Stopping in safe zone and drop glyph
                            telemetry.addData("current", taskNumber);
                            telemetry.update();

                            startPositionL = robot.leftMotor.getCurrentPosition();
                            endPositionL = startPositionL - 100;
                            while (opModeIsActive() && robot.leftMotor.getCurrentPosition() >= endPositionL) {
                                robot.rightMotor.setPower(-10*multR*correctR);
                                robot.leftMotor.setPower(-10*multL*correctL);
                            }

                            robot.rightMotor.setPower(0);
                            robot.leftMotor.setPower(0);
                            Time = 0;  // Timer
                            while (opModeIsActive() && Time < 100) {
                                Time = Time + 1;
                                telemetry.addData("current", taskNumber);
                                telemetry.addData("Time", Time);
                                telemetry.update();
                            }

                            robot.bigfoot.setPosition(1); // Setting bigfoot to position to push glyph into cryptobox
                            taskNumber =3;
                           preNumber  =8;

                           NextTaskNumber = 9;
                       break;

                case 9: // moves backwards after flipping glyph
                    Time = 0;
                    telemetry.addData("current", taskNumber);
                    telemetry.update();

                    startPositionL = robot.leftMotor.getCurrentPosition();
                    endPositionL = startPositionL + 200;

                    while (opModeIsActive() && robot.leftMotor.getCurrentPosition() < endPositionL) {
                        Time = Time + 1;
                        robot.rightMotor.setPower(20*multR*correctR);
                        robot.leftMotor.setPower(20*multL*correctL);
                    }
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);

                    NextTaskNumber = 9999;
                    taskNumber = 3;
                    preNumber  =9;

                    break;

                default:  // This defaults in stopping the motors
                    telemetry.addData("current", taskNumber);
                    telemetry.update();
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);
                    break;
                }
            }
        }
    }