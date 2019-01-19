package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

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

    BNO055IMU imu;

    public void runOpMode() throws InterruptedException {

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);


        robot.init(hardwareMap);                                // Servo mid position

        waitForStart();


        while (opModeIsActive() && taskNumber != 9999) {
            Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            startPositionR = robot.rightMotor.getCurrentPosition();
            startPositionL = robot.leftMotor.getCurrentPosition();
            telemetry.addData("first", angles.firstAngle);
            telemetry.addData("second", angles.secondAngle);
            telemetry.addData("third", angles.thirdAngle);
            telemetry.update();


        }
    }
}
