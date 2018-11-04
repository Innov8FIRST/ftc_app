package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Innov8_straightLine", group = "Auto")

public class Innov8_straightLine extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareInnov8Duck robot = new HardwareInnov8Duck();   // Use a Innov8's hardware

    /*Declare variables */
    double startPositionR = 0;
    double startPositionL = 0; // the left encoder is placed backwards
    double endPositionR = 0;
    double endPositionL = 0;
    double MID_SERVO = 0.3;
    double START_SERVO = 0;
    double END_SERVO = 0.9;
    int Time = 0;
    int FDrive = 2;
    int teamColor = 0;   //0 = red   or 1 = blue
    int taskNumber = 0;   //used to determine the step that should be executed
    int multR = 1;
    int multL = 1;
    double correctL = 1;
    double correctR = 1;

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
        telemetry.update();


        waitForStart();


        while (opModeIsActive() && taskNumber != 9999) {

            switch (taskNumber) {

                case 0: // Drive forwards in a straight line - 2ft 7 in

                    endPositionL = startPositionL - (5000 * multL);

                    while (opModeIsActive() && robot.leftMotor.getCurrentPosition() >= endPositionL) {
                      //  robot.rightMotor.setPower(-50 * multR * correctR);
                        robot.leftMotor.setPower(50 * multL * correctL);
                        robot.rightMotor.setPower(50 * multR * correctR);
                        telemetry.addData("Case", taskNumber);
                        telemetry.addData("right", robot.rightMotor.getCurrentPosition());
                        telemetry.addData("left", robot.leftMotor.getCurrentPosition());
                        telemetry.addData("startL", startPositionL);
                        telemetry.addData("endL", endPositionL);
                        telemetry.update();
                    }
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);

                    taskNumber = 1;
                    break;

            case 1: // Drive forwards in a straight line - 2ft 7 in

                endPositionR = startPositionR - (5000 * multR);

                while (opModeIsActive() && robot.rightMotor.getCurrentPosition() >= endPositionR) {
                    robot.rightMotor.setPower(50 * multR * correctR);
                    robot.leftMotor.setPower(50 * multL * correctL);
                    telemetry.addData("Case", taskNumber);
                    telemetry.addData("right", robot.rightMotor.getCurrentPosition());
                    telemetry.addData("left", robot.leftMotor.getCurrentPosition());
                    telemetry.addData("startL", startPositionL);
                    telemetry.addData("endL", endPositionL);
                    telemetry.addData("startR", startPositionR);
                    telemetry.addData("endR", endPositionR);
                    telemetry.update();
                }
                robot.rightMotor.setPower(0);
                robot.leftMotor.setPower(0);

                taskNumber = 9999;
            }
        }
    }
}