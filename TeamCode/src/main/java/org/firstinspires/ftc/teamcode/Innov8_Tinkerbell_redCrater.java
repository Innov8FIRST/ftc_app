package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Innov8_Tinkerbell_redCrater", group = "Auto")

public class Innov8_Tinkerbell_redCrater extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareInnov8Tinkerbell robot = new HardwareInnov8Tinkerbell();   // Use Tinkerbell's hardware

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
    double multR = 0.09;
    double multL = 0.02;
    double correctL = -1;  // 1 or -1
    double correctR = -1;  //1 or -1
    double right = 0;
    double left = 0;
    int degree = 0;
    double crocPos = 0;

    public void forward(double feet, int power) {
        startPositionL = robot.leftMotor.getCurrentPosition();
        double encoder = feet * 180;
        endPositionL = startPositionL - encoder;

        while (opModeIsActive() && robot.leftMotor.getCurrentPosition() >= endPositionL) {
            telemetried();
            robot.rightMotor.setPower(power * multR * correctR);
            robot.leftMotor.setPower(power * multL * correctL);
            telemetry.addData("taskNumber", taskNumber);
            telemetry.update();
        }
        robot.rightMotor.setPower(0);
        robot.leftMotor.setPower(0);
    }

    public void backward(double feet, int power) {
        startPositionR = robot.rightMotor.getCurrentPosition();
        double encoder = feet * -180;
        endPositionR = startPositionR + encoder;

        while (opModeIsActive() && robot.rightMotor.getCurrentPosition() <= endPositionR) {
            telemetried();
            robot.rightMotor.setPower(-power * multR * correctR);
            robot.leftMotor.setPower(-power * multR * correctR);
            telemetry.addData("taskNumber", taskNumber);
            telemetry.update();
        }
        robot.rightMotor.setPower(0);
        robot.leftMotor.setPower(0);
    }

    public void turn(double feet, int power, int degree) {
        startPositionR = robot.rightMotor.getCurrentPosition();
        double encoder = feet * 180;
        double degreeinput = degree * 
        endPositionR = startPositionR + encoder;
        endPositionL = startPositionL + encoder;

        while (opModeIsActive() && robot.rightMotor.getCurrentPosition() >= endPositionR) {
            telemetried();
            if (degree < 0) {
                robot.rightMotor.setPower(power * degreeinput * multR * correctR);
                robot.leftMotor.setPower(power * degree * multR * correctR);
            }
            else {
                robot.rightMotor.setPower()
            }
        }
    }

    public void centeredOnLine() {
        robot.rightMotor.setPower(10 * multR * correctR);
        robot.leftMotor.setPower(10 * multL * correctL);

        while (opModeIsActive() && robot.leftMotor.getPower() != 0 || robot.rightMotor.getPower() != 0) {
            telemetried();

            telemetry.update();

            if (robot.rightFruity.red() >= 150) {

                robot.rightMotor.setPower(0);
            }

            if (robot.leftFruity.red() >= 300) {

                robot.leftMotor.setPower(0);
            }
        }
    }

    public void crocDrop() {
        crocPos = robot.croc.getPosition();
        robot.croc.setPosition(1);
        telemetry.addData("croc", crocPos);
        telemetry.update();
        time = 0;
        while (opModeIsActive() && time < 5000) {
            time = time + 1;
        }
    }

    public void telemetried() {
        telemetry.addData("Case", taskNumber);
        telemetry.addData("RightRed", robot.rightFruity.red());
        telemetry.addData("RightBlue", robot.rightFruity.blue());
        telemetry.addData("LeftRed", robot.leftFruity.red());
        telemetry.addData("LeftBlue", robot.leftFruity.blue());
        telemetry.addData("right", robot.rightMotor.getCurrentPosition());
        telemetry.addData("left", robot.leftMotor.getCurrentPosition());
        telemetry.addData("startR", startPositionR);
        telemetry.addData("endR", endPositionR);
        telemetry.addData("startL", startPositionL);
        telemetry.addData("endL", endPositionL);
        telemetry.addData("RightPower", 10 * multR * correctR);
        telemetry.addData("LeftPower", 10 * multL * correctL);
        telemetry.addData("right", right);
        telemetry.addData("degree", degree);
        telemetry.addData("time", time);
        telemetry.update();
    }

    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);                                // Servo mid position
        telemetried();

        waitForStart();

        startPositionL = 0;
        startPositionR = 0;
        telemetried();

        //Moves robot forward in front of minerals
        forward(4, 40);
        taskNumber = 3;
        telemetried();

        //Decide which mineral to knock

        //After mineral has been knocked, moves forward to in front of safe zone
        forward(8, 40);
        taskNumber = 4;
        telemetried();

        //Gets robot to inside the safe zone to drop totem
        forward(, 40);
        taskNumber = 5;
        telemetried();

        crocDrop();
        taskNumber = 9999;
        telemetried();

        telemetry.update();
    }
}