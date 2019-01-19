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
import com.qualcomm.robotcore.hardware.DcMotor;
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

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DigitalChannel;

/**
 * This OpMode uses the common Pushbot hardware class to define the devices on the robot.
 * All device access is managed through the HardwarePushbot class.
 * The code is structured as a LinearOpMode
 * <p>
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name = "Innov8Teleop_Tinkerbell", group = "Tinkerbell")
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
        double startLift = 0;
        double currentLift = 0;
        double endLift = 0;
        double liftPower = 0;
        double reduceSpeedArm = 0.5;
        double reduceDriveSpeed = 1;
        double rightDirection = 1;
        double leftDirection = 1;
        double rPower = 0;
        double lPower = 0;
        double correctR = 0.9;
        double correctL = 1;
        double mStartPos = 0;   //michael start and end positions
        double mEndPos = 0;
        double michaelPower = 2;
        double nanaPower = 0;
        boolean gamepady = false;
        double nanaFor = 0;
        double michaelPos = 0;
        double leftTrigger = 0;
        double rightTrigger = 0;
        double wendyPower = 0;


        robot.init(hardwareMap);


        // Send telemetry message to signify robot waiting
        startLift = robot.liftMotor.getCurrentPosition();
        currentLift = robot.liftMotor.getCurrentPosition();
        endLift = robot.liftMotor.getCurrentPosition() - 22600;
        telemetry.addData("startLift", startLift);
        telemetry.addData("currentLift", currentLift);
        telemetry.addData("Say", "Hello Driver");
        telemetry.addData("michaelPower", robot.michael.getCurrentPosition());
        telemetry.addData("wendyPower", robot.wendy.getCurrentPosition());
        telemetry.update();


        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // while the op mode is active, loop and read the light levels.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
        while (opModeIsActive()) {

            // send the info back to driver station using telemetry function.
            // if the digital channel returns true it's HIGH and the button is unpressed.
            // run until the end of the match (driver presses STOP)

            // Hook
            while (gamepad1.right_bumper) {
                robot.hook.setPosition(robot.hook.getPosition() + 0.005);
            }

            while (gamepad1.left_bumper) {
                robot.hook.setPosition(robot.hook.getPosition() - 0.005);
            }

            // Driving
            if (gamepad1.left_stick_button) {
                leftDirection = leftDirection * -1;
            }

            if (gamepad1.right_stick_button) {
                rightDirection = rightDirection * -1;
            }

            // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
            // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
            left = gamepad1.left_stick_y;
            right = gamepad1.right_stick_y;

            // Normalize the values so neither exceed +/- 1.0
            max = Math.max(Math.abs(left), Math.abs(right));
            if (max > 1.0) {
                left /= max;
                right /= max;
            }


            lPower = left * leftDirection * correctL * reduceDriveSpeed;
            rPower = right * rightDirection * correctR * reduceDriveSpeed;
            robot.leftMotor.setPower(lPower);
            robot.rightMotor.setPower(rPower);

            if (robot.smee.getState() == false && gamepad2.left_stick_y > 0) {
                liftPower = 0;
                robot.liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                robot.liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            } else {
                liftPower = (gamepad2.left_stick_y);
            }

            robot.liftMotor.setPower(liftPower);
            currentLift = robot.liftMotor.getCurrentPosition();


            if (gamepad1.x) {
                robot.croc.setPosition(1);
            }

            if (gamepad1.b) {
                robot.croc.setPosition(0.5);
            }


            if (gamepad1.dpad_up) {
                reduceDriveSpeed = 1;
            }

            if (gamepad1.dpad_down) {
                reduceDriveSpeed = 0.4;
            }

            if (gamepad1.dpad_right) {
                reduceDriveSpeed = 1.5;
            }

            if (gamepad1.dpad_left) {
                reduceDriveSpeed = 0.75;
            }
            if (gamepad2.dpad_up) {
                while (robot.liftMotor.getCurrentPosition() > endLift) {
                    robot.liftMotor.setPower(-20);
                }
                robot.liftMotor.setPower(0);
            }

            // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
            // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.


            // Normalize the values so neither exceed +/- 1.0
            max = Math.max(Math.abs(left2), Math.abs(right2));
            if (max > 1.0) {
                left2 /= max;
                right2 /= max;
            }


            if (gamepad1.y) {
                rightDirection = rightDirection * -1;
                leftDirection = leftDirection * -1;
            }

            nanaFor = -gamepad2.right_stick_y;

            // Normalize the values so neither exceed +/- 1.0
            max = Math.max(Math.abs(left), Math.abs(right));
            if (max > 1.0) {
                nanaFor /= max;
            }

            robot.nana.setPower(nanaFor);

            //michaelPos = robot.michael.getCurrentPosition();


            //opens michael
            if (gamepad2.left_trigger > 0.3) {
                leftTrigger = gamepad2.left_trigger;
                robot.michael.setPower(leftTrigger*2);

            } else if (gamepad2.right_trigger > 0.3) {
                rightTrigger = (gamepad2.right_trigger * -1);
                robot.michael.setPower(rightTrigger*2);
            } else {
                rightTrigger = 0;
                robot.michael.setPower(0);
            }

            //extends wendy; wendy is all powerful arm

            if (gamepad2.dpad_right) {
                robot.wendy.setPower(0.3);
            } else if (gamepad2.dpad_left) {
                robot.wendy.setPower(-0.3);
            } else {
                robot.wendy.setPower(0);
            }


            while (gamepad2.x) {
                robot.hook.setPosition(robot.hook.getPosition() + 0.005);
            }

            while (gamepad2.b) {
                robot.hook.setPosition(robot.hook.getPosition() - 0.005);
            }

            // Send telemetry message to signify robot running;
            telemetry.addData("lift", "%.2f", left);
            telemetry.addData("turn", "%.2f", right);
            telemetry.addData("michaelPower", robot.michael.getCurrentPosition());
            telemetry.addData("LDir: ", leftDirection);
            telemetry.addData("RDir: ", rightDirection);
            telemetry.addData("rPower: ", rPower);
            telemetry.addData("lPower: ", lPower);
            telemetry.addData("reduceDriveSpeed: ", reduceDriveSpeed);
            telemetry.addData("rightM: ", robot.rightMotor.getPower());
            telemetry.addData("leftM: ", robot.leftMotor.getPower());
            telemetry.addData("left", left);
            telemetry.addData("right", right);
            telemetry.addData("Lift: ", robot.liftMotor.getCurrentPosition());
            telemetry.addData("LiftSpeed: ", robot.liftMotor.getPower());
            telemetry.addData("right2: ", right2);
            telemetry.addData("left2: ", left2);
            telemetry.addData("righten", robot.rightMotor.getCurrentPosition());
            telemetry.addData("leften", robot.leftMotor.getCurrentPosition());
            telemetry.addData("startLift", startLift);
            telemetry.addData("currentLift", currentLift);
            telemetry.addData("endLift", endLift);
            telemetry.addData("liftPower", liftPower);
            telemetry.addData("leftTrigger", leftTrigger);
            telemetry.addData("rightTrigger", rightTrigger);
            if (robot.smee.getState() == true) {
                telemetry.addData("smee", "Is Not Pressed");
            } else {
                telemetry.addData("smee", "Is Pressed");
            }
            telemetry.update();

            // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
            robot.waitForTick(40);
        }
    }
}
