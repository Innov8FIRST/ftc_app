package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Innov8_Blue_Sensor_Calibration", group = "Auto")

public class Innov8_Blue_Sensor_Calibration extends LinearOpMode { // color sensor test

    /* Declare OpMode members. */
    HardwareInnov8Auto robot = new HardwareInnov8Auto();   // Use a Innov8's hardware

    /*Declare variables */
    double startPositionR = 0;
    double startPositionL = 0;
    double endPositionR = 0;
    double endPositionL = 0;
    double MID_SERVO = 0.3;  // mid position of the servo
    double START_SERVO = 0;  // "down" position of the servo
    double END_SERVO = 0.9; // "up" position of the servo
    int Numbers = 0;  // how many values have been created
    int FDrive = 2;
    int multR = 1; // multiplier used if wheels are positioned or initialized backwards
    int multL = 1;
    int[] r = new int[10]; // creates new values for red, green, blue
    int[] g = new int[10];
    int[] b = new int[10];
    int Time = 0; // Used to make a timer
    int ar = 0; // averages of red, green, and blue values from OrangeySensor
    int ag = 0;
    int ab = 0;
    int[] fr = new int[10]; // creates new values for red, green, blue with FruitySensor
    int[] fg = new int[10];
    int[] fb = new int[10];
    int far = 0;
    int fag = 0;
    int fab = 0;

    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);

        waitForStart();


        while (opModeIsActive() && Time < 1000) {  // timer while telemetry updates

            robot.leftJewelArm.setPosition(0.38);

            for (Numbers = 0; Numbers < 10; Numbers++) { // getting data to put into an array
                telemetry.addData("BlueFruit", robot.FruitySensor.blue());
                telemetry.addData("RedFruit", robot.FruitySensor.red());
                telemetry.addData("GreenFruit", robot.FruitySensor.green());
                telemetry.addData("blue", robot.OrangeySensor.blue());
                telemetry.addData("red", robot.OrangeySensor.red());
                telemetry.addData("green", robot.OrangeySensor.green());
                fr[Numbers] = robot.FruitySensor.red();
                fb[Numbers] = robot.FruitySensor.blue();
                fg[Numbers] = robot.FruitySensor.green();
                Time = 0;
                while (opModeIsActive() && Time < 1000) {  // timer while telemetry updates
                    telemetry.addData("BlueFruit", robot.FruitySensor.blue());
                    telemetry.addData("RedFruit", robot.FruitySensor.red());
                    telemetry.addData("GreenFruit", robot.FruitySensor.green());
                    telemetry.addData("blue", robot.OrangeySensor.blue());
                    telemetry.addData("red", robot.OrangeySensor.red());
                    telemetry.addData("green", robot.OrangeySensor.green());
                    telemetry.addData("snickersnack", Time);
                    telemetry.update();
                    Time = Time + 1;
                }
                telemetry.update();
            }
            far = (fr[0] + fr[1] + fr[2] + fr[3] + fr[4] + fr[5] + fr[6] + fr[7] + fr[8] + fr[9]) / 10;
            fab = (fb[0] + fb[1] + fb[2] + fb[3] + fb[4] + fb[5] + fb[6] + fb[7] + fb[8] + fb[9]) / 10;
            fag = (fg[0] + fg[1] + fg[2] + fg[3] + fg[4] + fg[5] + fg[6] + fg[7] + fg[8] + fg[9]) / 10;

            if (far > 130 && fab < 100 && fag > 90) { //red
                Time = 0;
                while (opModeIsActive() && Time < 100) {
                    telemetry.addData("FRUITYRED", Time);
                    telemetry.update();
                }
                robot.leftJewelArm.setPosition(0.55);
            }
            if (far < 130 && fab > 100 && fag < 90) { // blue
                Time = 0;
                while (opModeIsActive() && Time < 100) {
                    telemetry.addData("FRUITYBLUE", Time);
                    telemetry.update();
                }
                robot.leftJewelArm.setPosition(0.55);
            } else {
                robot.leftJewelArm.setPosition(0.55);
            }
        }
    }
}
