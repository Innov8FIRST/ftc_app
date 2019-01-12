package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.ClassFactory; //Imports all Vuforia information
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name = "Innov8_Tinkerbell_redCraterVuforia", group = "Auto")

public class Innov8_Tinkerbell_redCraterVuforia extends LinearOpMode {
    private static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite"; //Establish name for minerals
    private static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    private static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    private static final String VUFORIA_KEY = "ATVwosb/////AAAAGYlO5qoc6kZagqZX6jvBKGgVjiVqbwuCKCZeIQTBkfNwsuJY/+oa3DHJbR/aFFfPF2A/bsi9cY36hUzYuOhFVBmWjYzVbQEh3YPoVATeaQEr/P6hNDA2AbW1Xbq0+hxqiYKpA1vNu22pVPOMW7MDmDst4HiuDLEXATZC3boSoLU6d9up0qPxZbZ+3fjXMnMTr6QkXIle3O7dfg/FVM09i/CIsq/Harcgg6lCoOYnrw70TEmPXOAxYdMh6Dh2KxZ8uAfHLur0U2adA0mWUKS7+z8Axq6jlH5oY8LOXp0FqX6A820mkqeDZz5DCkupkLOuTw/taIqz4vf2ewHRB8xGt7hEu34ZOr1TWOpT0bVnLLhB";
    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod; //Calls the Tensor Flow Object Detection

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
    double multR = 0.02; //Speed multiplier for the right motor
    double multL = 0.02; //Speed multiplier for the left motor
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
        double degreeinput = 50 / degree;
        endPositionR = startPositionR + encoder;
        endPositionL = startPositionL + encoder;

        while (opModeIsActive() && robot.rightMotor.getCurrentPosition() >= endPositionR) {
            telemetried();
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
    }

    public void drop() {
        liftPos = robot.liftMotor.getCurrentPosition();
        liftEnd = liftPos - 23000;
        while (opModeIsActive() && robot.liftMotor.getCurrentPosition() >= liftEnd) {
            robot.liftMotor.setPower(-20);
        }
        robot.liftMotor.setPower(0);

        while (opModeIsActive() && robot.hook.getPosition() < 0.98) {
            robot.hook.setPosition(1);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
                telemetry.addLine("wait failed");
            }
        }
    }

    public void release() {
        if (opModeIsActive() && robot.hook.getPosition() <= 1) {
            liftPos = robot.liftMotor.getCurrentPosition();
            liftEnd = liftPos + 22000;
            while (opModeIsActive() && robot.liftMotor.getCurrentPosition() <= liftEnd) {
                robot.liftMotor.setPower(20);
            }
            robot.liftMotor.setPower(0);
        }
    }

    public void seeBlock() {

    }

    public void idenMineral() {
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
            initTfod();
        } else {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
        /** Activate Tensor Flow Object Detection. */
        if (tfod != null) {
            tfod.activate();
        }

        while (opModeIsActive()) {
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
                        } else if (recognition.getLeft() == -1) {
                            silverMineral1X = (int) recognition.getLeft();
                        } else {
                            silverMineral2X = (int) recognition.getLeft();
                        }
                        degree = recognition.estimateAngleToObject(AngleUnit.DEGREES);
                        confi = recognition.getConfidence();
                        right = recognition.getRight();
                        width = recognition.getWidth();
                        telemetry.addData("degree", degree);
                        telemetry.addData("confi", confi);
                        telemetry.addData("right", right);
                        telemetry.addData("width", width);
                        telemetry.addData("left", goldMineralX);
                        telemetry.update();

                    }
                    if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                        if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Left");
                        } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                            telemetry.addData("Gold Mineral Position", "Right");
                        } else {
                            telemetry.addData("Gold Mineral Position", "Center");
                        }
                    }
                    if (confi > 0.9) {
                        if (degree <= -0.1) {
                            robot.rightMotor.setPower(2 * degree);
                            robot.leftMotor.setPower(-2 * degree);
                        } else if (degree >= 0.1) {
                            robot.rightMotor.setPower(-2 * degree);
                            robot.leftMotor.setPower(2 * degree);
                        } else {
                            robot.rightMotor.setPower(0);
                            robot.leftMotor.setPower(0);
                        }
                    } else {
                        seeBlock();
                    }
                }
            }
        }
    }

    private void initVuforia() {
        // Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

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

    public void knockMineral() {
        endPositionR = robot.rightMotor.getCurrentPosition() + 540;
        while (endPositionR < robot.rightMotor.getCurrentPosition()) {
            robot.rightMotor.setPower(10*correctR);
            robot.leftMotor.setPower(10*correctL);
        }
        robot.rightMotor.setPower(0);
        robot.leftMotor.setPower(0);
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
        telemetry.addData("RightPower", robot.rightMotor.getPower());
        telemetry.addData("LeftPower", robot.rightMotor.getPower());
        telemetry.addData("right", right);
        telemetry.addData("degree", degree);
        telemetry.addData("time", time);
        telemetry.addData("degree", degree);
        telemetry.addData("confi", confi);
        telemetry.addData("right", right);
        telemetry.addData("width", width);
        telemetry.addData("left", goldMineralX);
        telemetry.update();
        telemetry.update();
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

        //Decide which mineral to knock
        idenMineral();
        taskNumber = 2;
        telemetried();

        //Move robot to knock mineral
        knockMineral();
        taskNumber = 4;
        telemetried();

        //After mineral has been knocked, moves backward to prepare for turn
        backward(4, 40);
        taskNumber = 5;
        telemetried();

        //Turns robot towards safe zone
        turn(2, 30, -90);
        taskNumber = 6;
        telemetried();

        //Moves forward towards safe zone
        forward(10, 40);
        taskNumber = 7;
        telemetried();

        //Turns robot again towards safe zone
        turn(1, 30, -30);
        taskNumber = 8;
        telemetried();

        //Move forward the last time towards safe zone
        forward(3, 20);
        taskNumber = 9;
        telemetried();

        //Drops totem
        crocDrop();
        taskNumber = 10;
        telemetried();

        //Wait for totem to drop
        wait(1000);
        taskNumber = 11;
        telemetried();

        //Move backwards out of safe zone
        backward(3, 30);
        taskNumber = 12;
        telemetried();
        taskNumber = 9999;
        telemetried();
        telemetry.update();
    }
}