package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;



/**
 * Created by Troy on 10/01/16.
 This works

 */
//LinearOpMode

@Autonomous(name = "Info", group = "Auto")

public class Info extends LinearOpMode {

    HardwarePushbotTDR robot = new HardwarePushbotTDR();
    public DcMotor motorR;
    public DcMotor motorL;
    private ElapsedTime runtime = new ElapsedTime();
    public ColorSensor ColSensor;

    double vl = 0.75;
    double vr = 1.0;
    int step = 0;

    /*
        public Info(){

        }
    */
    @Override
    public void runOpMode() throws InterruptedException {

        robot.init(hardwareMap);

        double startPosR = robot.MotorR.getCurrentPosition();

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();


        while (opModeIsActive() ) {
            telemetry.addData("TouSensor.isPressed()", robot.TouSensor.isPressed());
            telemetry.addData("currentPosR", robot.MotorR.getCurrentPosition());
            telemetry.addData("currentPosPressServoR", robot.PressServoR.getPosition());
            telemetry.addData("currentPosPressServoL", robot.PressServoL.getPosition());
            telemetry.addData("ColSensor, blue:", robot.ColSensor.blue());
            telemetry.addData("ColSensor, green:", robot.ColSensor.green());
            telemetry.addData("ColSensor, red:", robot.ColSensor.red());
            telemetry.addData("FruitySensor, blue:", robot.FruitySensor.blue());
            telemetry.addData("FruitySensor, red:", robot.FruitySensor.red());
            telemetry.addData("FruitySensor, green:", robot.FruitySensor.green());
            telemetry.update();
            //idle();
        }





    }
}

