/*
Copyright (c) 2016 Robert Atkinson

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.vuforia.HINT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

/**
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 * The code is structured as a LinearOpMode
 *
 * This particular OpMode executes a POV Game style Teleop for a PushBot
 * In this mode the left stick moves the robot FWD and back, the Right stick turns left and right.
 * It raises and lowers the claw using the Gampad Y and A buttons respectively.
 * It also opens and closes the claws slowly using the left and right Bumper buttons.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name = "Innob8Teleop_Tinkerbell", group = "Tinkerbell")
// @Disabled
public class Innov8Teleop_Tinkerbell extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareInnov8Tinkerbell robot = new HardwareInnov8Tinkerbell();   // Use a Innov8's hardware map

    @Override
    public void runOpMode() throws InterruptedException {
        double left = 0;
        double right = 0;
        double left2 = 0;
        double right2 = 0;
        double max;
        double END_SERVO = 1; // all the way up
        double START_SERVO = 0; // all the way down
        double MID_SERVO = 0.5;
        double lift = 0;
        double turn = 0;
        double OPEN_HAND_L = 0.5;
        double CLOSE_HAND_L = 0.1;
        double OPEN_HAND_R = 0.5;
        double CLOSE_HAND_R = 0.9;
        double startPositionGlypht = 0;
        double reduceSpeedArm = 0.5;
        double reduceDriveSpeed = 0.4;
        double rightDirection = -1;
        double leftDirection = -1;
        double correctR = 0.9;
        double correctL = 1;
        VuforiaLocalizer.Parameters params = new VuforiaLocalizer.Parameters(R.id.cameraMonitorViewId);
        params.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        params.vuforiaLicenseKey = "AQ1M9Hr/////AAAAGWU4/VA9CkB2qgx3FSD3/fw7bv2rFbgag0jODlm8OJlj9coE/2ldwiRYwteK6A0INCjLtDrsBB5m+hphfYZjNLTb6BU7+9qGCnprf45lWm/bnlvIyB7dptTblbZ2K1Se8LSTKNhIXpA5r9cThAjUkg8PbiUfG7Qj5fD5lq3w+q+RHnIyNv2l6RjqlhHw5IF2aGQxEFmsPPa8YDjTDSGF0CFEWokxNTSe55H3etnQVysmx7mTUws0VZu7rnSgeN26RRZg91PB5xmEHi/zS7KVjLKDgktZenAht5kLHpvs2bWrHDcu6Yk+dP4I2YfPgd6gTYhxeY9Ge5rUXkufQ3y3XIffUSbhjWMRdTVNolB7/WAZ";
        params.cameraMonitorFeedback = VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;

        VuforiaLocalizer vuforia = ClassFactory.createVuforiaLocalizer(params);
        Vuforia.setHint(HINT.HINT_MAX_SIMULTANEOUS_IMAGE_TARGETS, 1);

        VuforiaTrackables minerals = vuforia.loadTrackablesFromAsset("Minerals_OT");
        minerals.get(0).setName("block");

        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();


        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        minerals.activate();
        telemetry.addData("hello", "you fool");


        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            for (VuforiaTrackable mineral : minerals) {  //For each loop for vuforia minerals
                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) mineral.getListener()).getPose();

                if (pose != null) {
                    VectorF translation = pose.getTranslation();

                    telemetry.addData(mineral.getName() + "-Translation", translation);

                    double degreesToTurn = Math.toDegrees(Math.atan2(translation.get(1), translation.get(2)));

                    telemetry.addData(mineral.getName() + "-Degrees", degreesToTurn);
                    telemetry.update();


                }
            }


            if (leftDirection == 1) {
                left = gamepad1.left_stick_y;
            } else {
                left = -gamepad1.left_stick_y;
            }

            if (rightDirection == 1) {
                right = gamepad1.right_stick_y;
            } else {
                right = -gamepad1.right_stick_y;
            }

            while (gamepad1.right_bumper) {
                robot.hook.setPosition(robot.hook.getPosition() + 0.005);
            }

            while (gamepad1.left_bumper) {
                robot.hook.setPosition(robot.hook.getPosition() - 0.005);
            }
            // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
            // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
            left = gamepad1.left_stick_y;
            right = gamepad1.right_stick_y;

            // Normalize the values so neither exceed +/- 1.0
            max = Math.max(Math.abs(left), Math.abs(right));
            if (max > 1.0)
            {
                left /= max;
                right /= max;
            }

            if (gamepad2.left_stick_y < 0) {
                robot.leftChain.setPower(20);
            }
            else {
                robot.leftChain.setPower(0);
            }
            // For driving
            robot.leftMotor.setPower(left*leftDirection*correctL*reduceDriveSpeed);
            robot.rightMotor.setPower(right*rightDirection*correctR*reduceDriveSpeed);

            /*

            if (gamepad1.left_bumper) {
                robot.handL.setPosition(OPEN_HAND_L);
                robot.handR.setPosition(OPEN_HAND_R);
            }

            if (gamepad1.right_bumper) {
                robot.handL.setPosition(CLOSE_HAND_L);
                robot.handR.setPosition(CLOSE_HAND_R);
            }

            */

            if (gamepad1.dpad_up)
            {
                reduceDriveSpeed = 0.6;
            }

            if (gamepad1.dpad_down)
            {
                reduceDriveSpeed = 0.2;
            }

            if (leftDirection == 1)
            {
                left2  = gamepad2.left_stick_y;
            }

            else {
                left2  = -gamepad2.left_stick_y;
            }

            if (rightDirection == 1)
            {
                right2  = gamepad2.right_stick_y;
            }

            else {
                right2  = -gamepad2.right_stick_y;
            }
            // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
            // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
            left2  = gamepad2.left_stick_y;
            right2 = gamepad2.right_stick_y;

            // Normalize the values so neither exceed +/- 1.0
            max = Math.max(Math.abs(left2), Math.abs(right2));
            if (max > 1.0)
            {
                left2 /= max;
                right2 /= max;
            }

            // For driving
            robot.leftChain.setPower(left2 * leftDirection * correctL * reduceDriveSpeed);
            //robot.rightChain.setPower(right2 * rightDirection * correctR * reduceDriveSpeed);

            /*

            if (gamepad1.dpad_right) { // down
                robot.leftJewelArm.setPosition(0.38);
                robot.jewelArm.setPosition(0.1);
            }

            if (gamepad1.dpad_left) { //up
                robot.leftJewelArm.setPosition(END_SERVO);
                robot.jewelArm.setPosition(END_SERVO);
            }

            */

            if (gamepad1.y) {
                rightDirection = rightDirection * -1;
                leftDirection = leftDirection * -1;
            }


            /*
            while(gamepad1.y) { // opens hand

//                if(robot.handL.getPosition()<0.4){
//                  STOP
//                }
//               if(robot.handR.getPosition()>0.8) {
//                    STOP
//                }
                robot.handL.setPosition(robot.handL.getPosition()+0.005);
                robot.handR.setPosition(robot.handR.getPosition()-0.005);
            }

            while(gamepad1.a) { // closes hand
                robot.handL.setPosition(robot.handL.getPosition()-0.005);
                robot.handR.setPosition(robot.handR.getPosition()+0.005);
            }
            */

            // Use gamepad buttons to get color sensor reading
            //if (gamepad1.b) {
            //    robot.leftFruity.enableLed(true);
            //}

            lift  = gamepad2.left_stick_y;
            turn = -gamepad2.right_stick_y;

            // Normalize the values so neither exceed +/- 1.0
            max = Math.max(Math.abs(left), Math.abs(right));
            if (max > 1.0) {
                lift /= max;
                turn /= max;
            }


            /*robot.glypht.setPower(lift);
            robot.arm.setPower(turn*reduceSpeedArm);

            startPositionGlypht = robot.glypht.getCurrentPosition();


            if (gamepad2.left_bumper) {
                robot.handL.setPosition(OPEN_HAND_L);
                robot.handR.setPosition(OPEN_HAND_R);
            }

            if (gamepad2.right_bumper) {
                robot.handL.setPosition(CLOSE_HAND_L);
                robot.handR.setPosition(CLOSE_HAND_R);
            }

            if (gamepad2.x) {
                robot.handL.setPosition(END_SERVO);
                robot.handR.setPosition(START_SERVO);
            }

            if (gamepad2.b) {
                robot.bigfoot.setPosition(START_SERVO);
            }

            if (gamepad2.y) {
                robot.bigfoot.setPosition(MID_SERVO);
            }


            if (gamepad2.dpad_right)
            {
                robot.bigfoot.setPosition(.7);
            }
            */

            // Send telemetry message to signify robot running;
            telemetry.addData("lift", "%.2f", left);
            telemetry.addData("turn", "%.2f", right);
            /*
            telemetry.addData("LeftFruityBlue", robot.leftFruity.blue());
            telemetry.addData("LeftFruityRed", robot.leftFruity.red());
            telemetry.addData("LeftFruityGreen", robot.leftFruity.green());
            telemetry.addData("RightFruityBlue", robot.rightFruity.blue());
            telemetry.addData("RightFruityRed", robot.rightFruity.red());
            telemetry.addData("RightFruityGreen", robot.rightFruity.green());
            */
            // telemetry.addData("startGlypht ", startPositionGlypht);
            // telemetry.addData("CurrentGlypht", robot.glypht.getCurrentPosition());
            telemetry.addData("LDir: ", leftDirection);
            telemetry.addData("RDir: ", rightDirection);
            telemetry.addData("rightM", robot.rightMotor.getPower());
            telemetry.addData("leftM", robot.leftMotor.getPower());
            telemetry.addData("right2", right2);
            telemetry.addData("left2", left2);

            telemetry.update();

            // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
            robot.waitForTick(40);
        }
    }
}
