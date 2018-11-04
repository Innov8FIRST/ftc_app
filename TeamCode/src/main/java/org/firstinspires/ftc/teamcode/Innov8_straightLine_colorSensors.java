package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Innov8_straightLine_colorSensors", group = "Auto")

public class Innov8_straightLine_colorSensors extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareInnov8Duck robot = new HardwareInnov8Duck();   // Use Duck's hardware

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
    double multR = 0.2;
    double multL = 0.05;
    double correctL = -1;  // 1 or -1
    double correctR = -1;  //1 or -1
    int while1 = 0;
    int while2 = 0;
    double PowerR = 0.5 * multR * correctR;
    double PowerL = 1 * multL * correctL;

    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);                                // Servo mid position

        startPositionR = robot.rightMotor.getCurrentPosition();
        startPositionL = robot.leftMotor.getCurrentPosition();
        telemetry.addData("Case", taskNumber);
        telemetry.addData("RightRed", robot.rightFruity.red());
        telemetry.addData("RightBlue", robot.rightFruity.blue());
        telemetry.addData("right", robot.rightMotor.getCurrentPosition());
        telemetry.addData("left", robot.leftMotor.getCurrentPosition());
        telemetry.addData("startR", startPositionR);
        telemetry.addData("endR", endPositionR);
        telemetry.addData("LeftRed", robot.leftFruity.red());
        telemetry.addData("LeftBlue", robot.leftFruity.blue());
        telemetry.addData("startL", startPositionL);
        telemetry.addData("endL", endPositionL);
        telemetry.addData("RightPower", PowerR);
        telemetry.addData("LeftPower", PowerL);
        telemetry.addData("Task", taskNumber);
        telemetry.addData("While1", while1);
        telemetry.addData("While2", while2);
        telemetry.update();

        waitForStart();

        robot.rightMotor.setPower(PowerR);
        robot.leftMotor.setPower(PowerL);

        while (opModeIsActive() && taskNumber != 9999) {
            while1 = while1 + 1;
            telemetry.addData("Case", taskNumber);
            telemetry.addData("RightRed", robot.rightFruity.red());
            telemetry.addData("RightBlue", robot.rightFruity.blue());
            telemetry.addData("right", robot.rightMotor.getCurrentPosition());
            telemetry.addData("left", robot.leftMotor.getCurrentPosition());
            telemetry.addData("startR", startPositionR);
            telemetry.addData("endR", endPositionR);
            telemetry.addData("LeftRed", robot.leftFruity.red());
            telemetry.addData("LeftBlue", robot.leftFruity.blue());
            telemetry.addData("startL", startPositionL);
            telemetry.addData("endL", endPositionL);
            telemetry.addData("RightPower", PowerR);
            telemetry.addData("LeftPower", PowerL);
            telemetry.addData("Task", taskNumber);
            telemetry.addData("While1", while1);
            telemetry.addData("While2", while2);
            telemetry.update();

            if (robot.rightMotor.getPower() == 0 && robot.leftMotor.getPower() == 0) {

                while2 = while2 + 1;
                telemetry.addData("While1", while1);
                telemetry.addData("While2", while2);
                telemetry.update();
                taskNumber = 9999;
            }
            if (robot.rightFruity.red() >= 450) {

                robot.rightMotor.setPower(0);
                PowerR = 0;
            }

            if (robot.leftFruity.red() >= 300) {
                robot.leftMotor.setPower(0);
                PowerL = 0;

            }

        }
    }
}