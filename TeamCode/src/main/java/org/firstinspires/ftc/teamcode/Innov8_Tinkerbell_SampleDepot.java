package org.firstinspires.ftc.teamcode;

import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name = "Innov8_Tinkerbell_SampleDepot", group = "Auto")

public class Innov8_Tinkerbell_SampleDepot extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite"; //Establish name for minerals
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = "ATVwosb/////AAAAGYlO5qoc6kZagqZX6jvBKGgVjiVqbwuCKCZeIQTBkfNwsuJY/+oa3DHJbR/aFFfPF2A/bsi9cY36hUzYuOhFVBmWjYzVbQEh3YPoVATeaQEr/P6hNDA2AbW1Xbq0+hxqiYKpA1vNu22pVPOMW7MDmDst4HiuDLEXATZC3boSoLU6d9up0qPxZbZ+3fjXMnMTr6QkXIle3O7dfg/FVM09i/CIsq/Harcgg6lCoOYnrw70TEmPXOAxYdMh6Dh2KxZ8uAfHLur0U2adA0mWUKS7+z8Axq6jlH5oY8LOXp0FqX6A820mkqeDZz5DCkupkLOuTw/taIqz4vf2ewHRB8xGt7hEu34ZOr1TWOpT0bVnLLhB";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod; //Calls the Tensor Flow Object Detection
    private static final double FEET_TO_ENCODER = (1120*12)/(4*Math.PI);

    /* Declare OpMode members. */
    HardwareInnov8Tinkerbell robot = new HardwareInnov8Tinkerbell();   // Use Tinkerbell's hardware

    /*Declare variables */
    double startPositionR = 0; //Right motor starts at 0
    double startPositionL = 0; //Left motor starts at 0
    double endPositionR = 0; //Distance to something based on right motor; is changed later
    double endPositionL = 0; //Distance to something based on left motor; is changed later
    double MID_SERVO = 0.3; //Position for servos that is in the middle of the required range
    double START_SERVO = 0; //Position for servos that is far to one side of the range
    double END_SERVO = 0.9; //Position for servos that is far to the other side of the range
    int Time = 0; //Used to count time
    int taskNumber = 0;   //used to determine the step that should be executed
    double multR = 0.03; //Speed multiplier for the right motor
    double multL = 0.03; //Speed multiplier for the left motor
    double correctL = -1;  // 1 or -1
    double correctR = -1;  //1 or -1
    double right = 0;
    double left = 0; //Shows distance to the left side of a detected object
    double crocPos = 0;
    double liftPos = 0;
    double liftEnd = 0;
    double confi = 0;
    double width = 0;
    double degree = 0;
    double goldMineralX = 0;
    double silverMineral1X = 0;
    double silverMineral2X = 0;
    double degreeten = 0; //This is the TensorFlow degree returned from the phone
    int mineralposition = 1;
    int i = 0;
    double maxConfi = 0;

    BNO055IMU imu;


    public void forward(double feet, double power) {
        startPositionL = robot.leftMotor.getCurrentPosition();
        endPositionL = startPositionL + feet * FEET_TO_ENCODER;

        while (opModeIsActive() && robot.leftMotor.getCurrentPosition() <= endPositionL) {
            telemetried();
            robot.rightMotor.setPower(-power * multR * correctR);
            robot.leftMotor.setPower(-power * multL * correctL);
            telemetry.addData("taskNumber", taskNumber);
            telemetry.update();
        }
        robot.rightMotor.setPower(0);
        robot.leftMotor.setPower(0);
    }

    public void backward(double feet, double power) {
        startPositionR = robot.rightMotor.getCurrentPosition();
        endPositionR = startPositionR - feet * FEET_TO_ENCODER;

        while (opModeIsActive() && robot.rightMotor.getCurrentPosition() >= endPositionR) {
            telemetried();
            robot.rightMotor.setPower(power * multR * correctR);
            robot.leftMotor.setPower(power * multR * correctR);
            telemetry.addData("taskNumber", taskNumber);
            telemetry.update();
        }
        robot.rightMotor.setPower(0);
        robot.leftMotor.setPower(0);
    }

    public void initGyro() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }


    public void turn(double power, int degree) { //right is negative
        initGyro();
        Orientation angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

        int direction = degree / Math.abs(degree);
        double endAng = degree + angles.firstAngle;
        double previousAng = angles.firstAngle;

        while (opModeIsActive() && Math.abs(angles.firstAngle - endAng) > 1.5) {
            robot.rightMotor.setPower(0.3 * direction);
            robot.leftMotor.setPower(0.3 * direction * -1);
            angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            telemetry.addData("previousAng", previousAng);
            telemetry.addData("first", angles.firstAngle);
            telemetry.addData("endAng", endAng);
            telemetry.addData("confi", confi);
            telemetry.update();
        }
        robot.rightMotor.setPower(0);
        robot.leftMotor.setPower(0);
    }


    public void drop() {
        liftPos = robot.liftMotor.getCurrentPosition();
        liftEnd = liftPos - 23000;
        while (opModeIsActive() && robot.liftMotor.getCurrentPosition() >= liftEnd) {
            robot.liftMotor.setPower(-1);
        }
        robot.liftMotor.setPower(0);
        robot.hook.setPosition(0.75);
    }

    public void release() {
        if (opModeIsActive() && robot.hook.getPosition() <= 1) {
            liftPos = robot.liftMotor.getCurrentPosition();
            liftEnd = liftPos + 22000;
            while (opModeIsActive() && robot.liftMotor.getCurrentPosition() <= liftEnd) {
                robot.liftMotor.setPower(1);
            }
            robot.liftMotor.setPower(0);
        }
    }

    public void centeredOnBlock() {
        if (degreeten <= -0.1) { //This uses the TensorFlow degree returned from the phone
            robot.rightMotor.setPower(0.2 * degreeten);
            robot.leftMotor.setPower(-0.2 * degreeten);
        } else if (degreeten >= 0.1) {
            robot.rightMotor.setPower(-0.2 * degreeten);
            robot.leftMotor.setPower(0.2 * degreeten);
        } else {
            robot.rightMotor.setPower(0);
            robot.leftMotor.setPower(0);
        }
    }

    public double idenMineral() {
        if (tfod != null) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                goldMineralX = -1;
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                        goldMineralX = (int) recognition.getLeft();
                        confi = recognition.getConfidence();
                    }
                    else {
                        confi = 0.22;
                    }
                }
            }
        }
        return confi;
    }

    private void initVuforia() {
        // Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
        // Activate Tensor Flow Object Detection
        if (tfod != null) {
            tfod.activate();
        }
    }

    public void knockMineral() {
        endPositionR = robot.rightMotor.getCurrentPosition() + FEET_TO_ENCODER * 3;
        while (endPositionR < robot.rightMotor.getCurrentPosition()) {
            robot.rightMotor.setPower(10 * correctR);
            robot.leftMotor.setPower(10 * correctL);
        }
        robot.rightMotor.setPower(0);
        robot.leftMotor.setPower(0);
    }

    public void crocDrop() {
        robot.croc.setPosition(1);
        crocPos = robot.croc.getPosition();
        telemetry.addData("croc", crocPos);
        telemetry.update();
    }

    public void telemetried() {
        telemetry.addData("Case", taskNumber);
        telemetry.addData("right", robot.rightMotor.getCurrentPosition());
        telemetry.addData("left", robot.leftMotor.getCurrentPosition());
        telemetry.addData("startR", startPositionR);
        telemetry.addData("endR", endPositionR);
        telemetry.addData("startL", startPositionL);
        telemetry.addData("endL", endPositionL);
        telemetry.addData("RightPower", robot.rightMotor.getPower());
        telemetry.addData("LeftPower", robot.rightMotor.getPower());
        telemetry.addData("right", right);
        telemetry.addData("degree", degree);
        telemetry.addData("time", time);
        telemetry.addData("degreeten", degreeten);
        telemetry.addData("confi", confi);
        telemetry.addData("right", right);
        telemetry.addData("width", width);
        telemetry.addData("left", goldMineralX);
        telemetry.addData("position", mineralposition);
        telemetry.addData("croc", crocPos);
        telemetry.addData("maxConfi", maxConfi);
        telemetry.addData("i", i);
        telemetry.update();
        Log.d("maxConfi", Double.toString(maxConfi));
        Log.d("Case", Double.toString(taskNumber));
        Log.d("right", Double.toString(robot.rightMotor.getCurrentPosition()));
        Log.d("left", Double.toString(robot.leftMotor.getCurrentPosition()));
        Log.d("startR", Double.toString(startPositionR));
        Log.d("endR", Double.toString(endPositionR));
        Log.d("startL", Double.toString(startPositionL));
        Log.d("endL", Double.toString(endPositionL));
        Log.d("RightPower", Double.toString(robot.rightMotor.getPower()));
        Log.d("LeftPower", Double.toString(robot.rightMotor.getPower()));
        Log.d("right", Double.toString(right));
        Log.d("degree", Double.toString(degree));
        Log.d("time", Double.toString(time));
        Log.d("degreeten", Double.toString(degreeten));
        Log.d("confi", Double.toString(confi));
        Log.d("right", Double.toString(right));
        Log.d("width", Double.toString(width));
        Log.d("left", Double.toString(goldMineralX));
        Log.d("position", Double.toString(mineralposition));
        Log.d("croc", Double.toString(crocPos));
    }

    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);                                // Servo mid position
        telemetried();

        waitForStart();

        startPositionL = 0;
        startPositionR = 0;
        telemetried();

        //Drops robot from lander
        drop();
        taskNumber = 1;
        telemetried();

        initVuforia();
        initTfod();
        initGyro();
        //Decide which mineral to knock
        taskNumber = 2;
        telemetried();
        for (i = 0; i < 50; i++) {
           confi = idenMineral();
           if (confi > maxConfi) {
               maxConfi = confi;
           }
           telemetried();
        }

        confi = idenMineral();
        telemetried();


        if (maxConfi <= 0.8) {
            maxConfi = 0;
            forward(0.3, 10);
            turn(10, -35);
            mineralposition = 2;
            telemetried();
            for (int i = 0; i < 50; i++) {
                confi = idenMineral();
                if (confi > maxConfi) {
                    maxConfi = confi;
                }
                telemetried();
            }

            taskNumber = 3;
            telemetried();

            if (maxConfi <= 0.9) {
                turn(10, 70);
                mineralposition = 3;
                confi = idenMineral();
                taskNumber = 4;
            }
        }
        //Move robot to knock mineral
        taskNumber = 5;
        telemetried();
        forward(2, 20);

        taskNumber = 9999;



        if (mineralposition == 1) {
            forward(32/12.0, 20);
            crocDrop();
            backward(13.5/12.0, 10);
            turn(30, 40);
            forward(9/12.0, 20);
            turn(30, 80);
            forward(69/12.0, 30);
        }

        if (mineralposition == 2) {
            turn(20, 65);
            forward(4, 20);
            crocDrop();
            backward(1, 20);
            turn(30, 60);
            forward(1.1, 40);
            turn(40,22);
            forward(5,40);
        }

        if (mineralposition == 3) {
            forward(.5, 30);
            turn(20, -65);
            forward(27/12.0, 20);
            crocDrop();
            backward(1,10);
            backward(1,30);
            turn(20, 170);
            forward(50/12.0, 20);
        }

        taskNumber = 9999;
    }
}