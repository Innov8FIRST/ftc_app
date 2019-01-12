package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Innov8_turnTest", group = "Auto")

public class Innov8_turnTest extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareInnov8Tinkerbell robot = new HardwareInnov8Tinkerbell();   // Use a Innov8's hardware

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
    }

    public void turn(double power, int degree) {
        startPositionR = robot.rightMotor.getCurrentPosition();
        double degreeinput = 50 / degree;
        double encoder = degreeinput;
        endPositionR = startPositionR + encoder;
        endPositionL = startPositionL + encoder;

        while (opModeIsActive() && robot.rightMotor.getCurrentPosition() >= endPositionR) {
            telemetry.addData("startR", startPositionR);
            telemetry.addData("startL", startPositionL);
            telemetry.addData("CurrentR", robot.rightMotor.getCurrentPosition());
            telemetry.addData("CurrentL", robot.leftMotor.getCurrentPosition());
            telemetry.addData("EndR", endPositionR);
            telemetry.addData("EndL", endPositionL);
            telemetry.update();
            if (degree < 0) {
                robot.rightMotor.setPower(power * degreeinput * multR * correctR);
                robot.leftMotor.setPower(power * degree * multR * correctR);
            } else {
                robot.rightMotor.setPower(power * degree * multR * correctR);
                robot.leftMotor.setPower(power * degreeinput * multR * correctR);
            }
            robot.rightMotor.setPower(0);
            robot.leftMotor.setPower(0);
        }

        waitForStart();


        while (opModeIsActive() && taskNumber != 9999) {

            turn(0.5, 90);

            time = 0;
            while (time < 1000) {
                robot.rightMotor.setPower(0.5 * multR);
                robot.leftMotor.setPower(0.5 * multL);
                time = time + 1;
            }
            robot.rightMotor.setPower(0);
            robot.leftMotor.setPower(0);

            turn(0.5, -180);

            time = 0;
            while (time < 1000) {
                robot.rightMotor.setPower(0.5 * multR);
                robot.leftMotor.setPower(0.5 * multL);
                time = time + 1;
            }
            robot.rightMotor.setPower(0);
            robot.leftMotor.setPower(0);
        }
    }
}
