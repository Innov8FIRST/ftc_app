package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "Innov8_Red_Sensor_Calibration", group = "Auto")

public class Innov8_Red_Sensor_Calibration extends LinearOpMode { // color sensor test

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


        for (Numbers = 0; Numbers < 10; Numbers++) { // getting 10 different readings to put into an array
            telemetry.addData("blue", robot.OrangeySensor.blue());
            telemetry.addData("red", robot.OrangeySensor.red());
            telemetry.addData("green", robot.OrangeySensor.green());
            telemetry.addData("BlueFruit", robot.FruitySensor.blue());
            telemetry.addData("RedFruit", robot.FruitySensor.red());
            telemetry.addData("GreenFruit", robot.FruitySensor.green());
            r[Numbers] = robot.OrangeySensor.red();
            b[Numbers] = robot.OrangeySensor.blue();
            g[Numbers] = robot.OrangeySensor.green();
            telemetry.addData("number", Numbers);
            Time = 0;
            while (opModeIsActive() && Time < 1000) {  // timer while telemetry updates
                telemetry.addData("blue", robot.OrangeySensor.blue());
                telemetry.addData("red", robot.OrangeySensor.red());
                telemetry.addData("green", robot.OrangeySensor.green());
                telemetry.addData("BlueFruit", robot.FruitySensor.blue());
                telemetry.addData("RedFruit", robot.FruitySensor.red());
                telemetry.addData("GreenFruit", robot.FruitySensor.green());
                telemetry.addData("snickersnack", Time);
                telemetry.update();
                Time = Time + 1;
            }
            telemetry.update();
        }
        ar = (r[0] + r[1] + r[2] + r[3] + r[4] + r[5] + r[6] + r[7] + r[8] + r[9]) / 10; //red average
        ab = (b[0] + b[1] + b[2] + b[3] + b[4] + b[5] + b[6] + b[7] + b[8] + b[9]) / 10; //blue average
        ag = (g[0] + g[1] + g[2] + g[3] + g[4] + g[5] + g[6] + g[7] + g[8] + g[9]) / 10; //green average

        if (ar > 100 && ab < 70 && ag > 55) { //red
            Time = 0;
            while (opModeIsActive() && Time < 100) {
                telemetry.addData("RED", Time);
                telemetry.update();
            }
            robot.jewelArm.setPosition(0.55);
        }
        if (ar < 100 && ab > 70 && ag < 55) { // blue
            Time = 0;
            while (opModeIsActive() && Time < 100) {
                telemetry.addData("BLUE", Time);
                telemetry.update();
            }
            robot.jewelArm.setPosition(0.55);
        }
        else {
            robot.jewelArm.setPosition(0.55);
        }

        robot.leftJewelArm.setPosition(0.38);
    }
}
