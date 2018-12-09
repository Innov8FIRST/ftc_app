package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "dropTest", group = "Auto")

public class dropTest extends LinearOpMode {

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
    double endLift = 0;
    double startLift = 0;
    double currentLift = 0;

    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);                                // Servo mid position

        startPositionR = robot.rightMotor.getCurrentPosition();
        startPositionL = robot.leftMotor.getCurrentPosition();
        startLift = robot.liftMotor.getCurrentPosition();
        currentLift = robot.liftMotor.getCurrentPosition();
        endLift = robot.liftMotor.getCurrentPosition() - 22600;
        telemetry.addData("startR", startPositionR);
        telemetry.addData("startL", startPositionL);
        telemetry.addData("CurrentR", robot.rightMotor.getCurrentPosition());
        telemetry.addData("CurrentL", robot.leftMotor.getCurrentPosition());
        telemetry.addData("EndR", endPositionR);
        telemetry.addData("EndL", endPositionL);
        telemetry.update();


        waitForStart();


        while (opModeIsActive() && taskNumber != 9999) {

            while (robot.liftMotor.getCurrentPosition() > endLift) {
                robot.liftMotor.setPower(-20);
            }
            robot.liftMotor.setPower(0);

            Thread.sleep(100);
            robot.hook.setPosition(1);
            Thread.sleep(5000);

            Thread.sleep(300);
            taskNumber = 9999;
        }
    }
}