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

@TeleOp(name="colorSensorTest", group="Dromedary")
// @Disabled
public class colorSensorTest extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareInnov8Duck robot = new HardwareInnov8Duck();   // Use a Innov8's hardware


    @Override
    public void runOpMode() throws InterruptedException {
        double left = 0;
        double right = 0;
        double max;
        double END_SERVO = 1; // all the way up
        double START_SERVO = 0; // all the way down
        double MID_SERVO = 0.5;
        double lift = 0;
        double turn = 0;
        double reduceDriveSpeed = 0.4;
        double rightDirection = -1;
        double leftDirection = 1;
        double correctR = 1;
        double correctL = 0.7;
        /* Initialize the hardware variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            if (leftDirection == 1)
            {
                left  = -gamepad1.left_stick_y;
            }

            else {
                left  = gamepad1.left_stick_y;
            }

            if (rightDirection == 1)
            {
                right  = -gamepad1.right_stick_y;
            }

            else {
                right  = gamepad1.right_stick_y;
            }
            // Run wheels in POV mode (note: The joystick goes negative when pushed forwards, so negate it)
            // In this mode the Left stick moves the robot fwd and back, the Right stick turns left and right.
            /* left  = gamepad1.left_stick_y;
            right = gamepad1.right_stick_y; */

            // Normalize the values so neither exceed +/- 1.0
            max = Math.max(Math.abs(left), Math.abs(right));
            if (max > 1.0)
            {
                left /= max;
                right /= max;
            }

            // For driving
            robot.leftMotor.setPower(left*leftDirection*correctL*reduceDriveSpeed);
            robot.rightMotor.setPower(right*rightDirection*correctR*reduceDriveSpeed);


            if (gamepad1.dpad_up)
            {
                reduceDriveSpeed = 0.6;
            }

            if (gamepad1.dpad_down)
            {
                reduceDriveSpeed = 0.2;
            }



            // Use gamepad buttons to get color sensor reading
            if (gamepad1.b) {
                robot.leftFruity.enableLed(true);
                robot.rightFruity.enableLed(true);
            }

            lift  = gamepad2.left_stick_y;
            turn = -gamepad2.right_stick_y;

            // Normalize the values so neither exceed +/- 1.0
            max = Math.max(Math.abs(left), Math.abs(right));
            if (max > 1.0)
            {
                lift /= max;
                turn /= max;
            }


            telemetry.addData("LeftG", robot.leftFruity.green());
            telemetry.addData("LeftB", robot.leftFruity.blue());
            telemetry.addData("LeftR", robot.leftFruity.red());
            telemetry.addData("RightG", robot.rightFruity.green());
            telemetry.addData("RightB", robot.rightFruity.blue());
            telemetry.addData("RightR", robot.rightFruity.red());
            telemetry.update();

            // Pause for metronome tick.  40 mS each cycle = update 25 times a second.
            robot.waitForTick(40);
        }
    }
}
