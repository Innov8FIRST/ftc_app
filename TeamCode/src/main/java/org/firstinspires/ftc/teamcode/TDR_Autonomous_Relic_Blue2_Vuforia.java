package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/* for Vuforia */
import org.firstinspires.ftc.robotcontroller.external.samples.ConceptVuforiaNavigation;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


@Autonomous(name = "TDR_Autonomous_Relic_Blue2_Vuforia", group = "Auto")

public class TDR_Autonomous_Relic_Blue2_Vuforia extends LinearOpMode { // Blue 2

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
    int multR = 1;
    int multL = 1;

    /* For Vuforia */
    public static final String TAG = "Vuforia VuMark Sample";

    OpenGLMatrix lastLocation = null;

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */

    VuforiaLocalizer vuforia;



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

    /* For Vuforia */

        // Activate Camera
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        // OR...  Do Not Activate the Camera Monitor View, to save power
        // VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        /*
         * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
         * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
         * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
         * web site at https://developer.vuforia.com/license-manager.
         *
         * Vuforia license keys are always 380 characters long, and look as if they contain mostly
         * random data. As an example, here is a example of a fragment of a valid key:
         *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
         * Once you've obtained a license key, copy the string from the Vuforia web site
         * and paste it in to your code onthe next line, between the double quotes.
         */
        parameters.vuforiaLicenseKey = "AQ1M9Hr/////AAAAGWU4/VA9CkB2qgx3FSD3/fw7bv2rFbgag0jODlm8OJlj9coE/2ldwiRYwteK6A0INCjLtDrsBB5m+hphfYZjNLTb6BU7+9qGCnprf45lWm/bnlvIyB7dptTblbZ2K1Se8LSTKNhIXpA5r9cThAjUkg8PbiUfG7Qj5fD5lq3w+q+RHnIyNv2l6RjqlhHw5IF2aGQxEFmsPPa8YDjTDSGF0CFEWokxNTSe55H3etnQVysmx7mTUws0VZu7rnSgeN26RRZg91PB5xmEHi/zS7KVjLKDgktZenAht5kLHpvs2bWrHDcu6Yk+dP4I2YfPgd6gTYhxeY9Ge5rUXkufQ3y3XIffUSbhjWMRdTVNolB7/WAZ";

        /*
         * We also indicate which camera on the RC that we wish to use.
         * Here we chose the back (HiRes) camera (for greater range), but
         * for a competition robot, the front camera might be more convenient.
         */
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        /**
         * Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
         * in this data set: all three of the VuMarks in the game were created from this one template,
         * but differ in their instance id information.
         * @see VuMarkInstanceId
         */
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        telemetry.addData(">", "Press Play to start");
        telemetry.update();


        waitForStart();

        relicTrackables.activate();  // For Vuforia

        endPositionR = (startPositionR + 100);
        endPositionL = (startPositionL + 100);

        robot.rightMotor.setPower(200);
        robot.leftMotor.setPower(200);


        while (opModeIsActive() && taskNumber != 9999) {

            switch (taskNumber) {
                case 0: // Drop jewel arm and check for color

                    telemetry.addData("Case", taskNumber);
                    robot.leftJewelArm.setPosition(MID_SERVO);
                    telemetry.addData("Fruity1Blue", robot.FruitySensor.blue());
                    telemetry.addData("Fruity1Red", robot.FruitySensor.red());
                    telemetry.addData("Fruity1Green", robot.FruitySensor.green());
                    telemetry.addData("Time", Time);
                    telemetry.update();

                    taskNumber = 3;
                break;

                case 1: // Drive backwards 4 inches
                    telemetry.addData("Case", taskNumber);
                    endPositionR = startPositionR - (200 * multR);
                    endPositionL = startPositionL - (200 * multL);
                    while (opModeIsActive() && robot.leftMotor.getCurrentPosition() >= endPositionL) {
                        robot.rightMotor.setPower(-40 * multR);
                        robot.leftMotor.setPower(-40 * multL);
                    }
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);
                    taskNumber = 5;
                break;

                case 2: // Drive forwards 4 inches
                    telemetry.addData("Case", taskNumber);
                    startPositionR = robot.rightMotor.getCurrentPosition();
                    telemetry.addData("Start", startPositionR);
                    telemetry.update();
                    endPositionR = startPositionR + (200 * multR);
                    endPositionL = startPositionL + (200 * multL);
                    while (opModeIsActive() && robot.rightMotor.getCurrentPosition() <= endPositionR) {
                        robot.rightMotor.setPower(40 * multR);
                        robot.leftMotor.setPower(40 * multL);
                        telemetry.addData("Current", robot.rightMotor.getCurrentPosition());
                    }
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);
                    taskNumber = 6;
                break;

                case 3: // wait for 5 seconds
                    telemetry.addData("Case", taskNumber);
                    while (opModeIsActive() && Time < 5) {
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
                    telemetry.addData("Case", taskNumber);

                    if (robot.FruitySensor.red() >= 6) {
                        taskNumber = 2;
                    } else {
                        taskNumber = 1;
                    }
                    telemetry.addData("Task: ", taskNumber);
                    telemetry.update();
                break;

                default: // stop the program in case default
                break;

                case 5:
                    telemetry.addData("Case", taskNumber);
                    telemetry.addData("Move:", "Forward");
                    startPositionR = robot.rightMotor.getCurrentPosition();
                    telemetry.addData("Start", startPositionR);
                    telemetry.update();
                    endPositionR = startPositionR + (700 * multR);
                    endPositionL = startPositionL + (700 * multL);
                    while (opModeIsActive() && robot.rightMotor.getCurrentPosition() <= endPositionR) {
                        robot.rightMotor.setPower(50 * multR);
                        robot.leftMotor.setPower(50 * multL);
                        telemetry.addData("Current", robot.rightMotor.getCurrentPosition());
                    }
                    taskNumber = 7;
                break;

                case 6:
                    telemetry.addData("Case", taskNumber);
                    telemetry.addData("Move:", "Forward");
                    startPositionR = robot.rightMotor.getCurrentPosition();
                    telemetry.addData("Start", startPositionR);
                    telemetry.update();
                    endPositionR = startPositionR + (500 * multR);
                    endPositionL = startPositionL + (500 * multL);
                    while (opModeIsActive() && robot.rightMotor.getCurrentPosition() <= endPositionR) {
                        robot.rightMotor.setPower(50 * multR);
                        robot.leftMotor.setPower(50 * multL);
                        telemetry.addData("Current", robot.rightMotor.getCurrentPosition());
                    }
                    taskNumber = 7;
                break;

                case 7: // Turn right
                    startPositionL = robot.leftMotor.getCurrentPosition();
                    endPositionL = startPositionL + (500 * multR);
                    while (opModeIsActive() && robot.leftMotor.getCurrentPosition() <= endPositionL) {
                        robot.rightMotor.setPower(-70 * multR);
                        robot.leftMotor.setPower(70 * multL);
                    }
                    robot.leftMotor.setPower(0);
                    robot.rightMotor.setPower(0);
                    taskNumber = 8;
                break;

                case 8:
                    startPositionR = robot.rightMotor.getCurrentPosition();
                    endPositionR = startPositionR + (500 * multR);
                    while (opModeIsActive() && robot.rightMotor.getCurrentPosition() <= endPositionR) {
                        robot.rightMotor.setPower(80 * multR);
                        robot.leftMotor.setPower(80 * multL);
                    }
                    robot.rightMotor.setPower(0);
                    robot.leftMotor.setPower(0);
                    taskNumber = 10;
                break;

                case 9:
                    robot.bigfoot.setPosition(MID_SERVO);
                    taskNumber = 9999;
                break;

                case 10: // Turn left
                    startPositionR = robot.rightMotor.getCurrentPosition();
                    endPositionR = startPositionR + (500 * multR);
                    while (opModeIsActive() && robot.rightMotor.getCurrentPosition() <= endPositionL) {
                        robot.rightMotor.setPower(70 * multR);
                        robot.leftMotor.setPower(-70 * multL);
                    }
                    robot.leftMotor.setPower(0);
                    robot.rightMotor.setPower(0);
                    taskNumber = 9;
                break;


                case 40: //find left right center

                    /* For Vuforia
                     * See if any of the instances of {@link relicTemplate} are currently visible.
                     * {@link RelicRecoveryVuMark} is an enum which can have the following values:
                     * UNKNOWN, LEFT, CENTER, and RIGHT. When a VuMark is visible, something other than
                     * UNKNOWN will be returned by {@link RelicRecoveryVuMark#from(VuforiaTrackable)}.
                     */
                    RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
                    if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                       /* Found an instance of the template. In the actual game, you will probably
                        * loop until this condition occurs, then move on to act accordingly depending
                        * on which VuMark was visible. */
                        telemetry.addData("VuMark", "%s visible", vuMark);

                       /* For fun, we also exhibit the navigational pose. In the Relic Recovery game,
                        * it is perhaps unlikely that you will actually need to act on this pose information, but
                        * we illustrate it nevertheless, for completeness. */
                        OpenGLMatrix pose = ((VuforiaTrackableDefaultListener)relicTemplate.getListener()).getPose();
                        telemetry.addData("Pose", format(pose));

                      /* We further illustrate how to decompose the pose into useful rotational and
                       * translational components */
                        if (pose != null) {
                            VectorF trans = pose.getTranslation();
                            Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                            // Extract the X, Y, and Z components of the offset of the target relative to the robot
                            double tX = trans.get(0);
                            double tY = trans.get(1);
                            double tZ = trans.get(2);

                            // Extract the rotational components of the target relative to the robot
                            double rX = rot.firstAngle;
                            double rY = rot.secondAngle;
                            double rZ = rot.thirdAngle;
                        }
                    }
                    else {
                        telemetry.addData("VuMark", "not visible");
                    }
                    telemetry.addData("hi",vuMark);
                    telemetry.update();

                break;
            }



        }
    }

    String format(OpenGLMatrix transformationMatrix) {
        return (transformationMatrix != null) ? transformationMatrix.formatAsTransform() : "null";
    }

}
