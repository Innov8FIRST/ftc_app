package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PWMOutput;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created on 10/01/16.
 This works

 */
//LinearOpMode

@TeleOp(name = "TeleOpJKB", group = "JKB")

public class TeleOpJKB extends OpMode{

    HardwarePushbotTDR         robot   = new HardwarePushbotTDR();
    public DcMotor motorR;
    public DcMotor motorL;
    private ElapsedTime runtime = new ElapsedTime();
    double vl = 1;
    double vr = 1;
    int direction = 1;
    int shot = 0;
    double step = 0;
    double loadStep = 0;
    double shotspeed = .44;
    double startPosR;
    boolean backdone = false;
    double reduceSpeed = 1;

    /*
        public TeleOpJKB(){

        }
    */
    @Override
    public void init() {

        robot.init(hardwareMap);
        robot.LiftServo.setPosition(.15);
        robot.ShotFeeder.setPosition(.9);
        robot.PressServoR.setPosition(1);
        robot.PressServoL.setPosition(0);
        robot.ConveyorServo.setPosition(0);//in
        robot.TouchServo.setPosition(0);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Ready to run");    //
        telemetry.update();

    }

    @Override
    public void loop(){
        telemetry.addData("Loop", "Running");


        //DRIVING
        if(gamepad1.b){
            reduceSpeed = 1;
        }
        if(gamepad1.x){
            reduceSpeed = .5;
        }
        telemetry.addData("reduceSpeed", reduceSpeed);


        if(gamepad1.right_stick_button){
            direction = -1;//forward to push buttons
            telemetry.addData("Buttons Direction", direction);
        }

        telemetry.addData("Direction", direction);


        if(gamepad1.left_stick_button){
            direction = 1;//forward to shoot, lift
            telemetry.addData("Lift Direction", direction);
        }

        double l;
        double r;


        l = -gamepad1.left_stick_y;
        r = -gamepad1.right_stick_y;

        telemetry.addData("l value:", l);
        telemetry.addData("r value:", r);

        //LEFT JOYSTICK
        if (l <-0.05 || l > 0.05){
            if(direction == 1){
                robot.MotorL.setPower(l*vl*direction*reduceSpeed);
            }
            else if (direction ==-1){
                robot.MotorR.setPower(l*vr*direction*reduceSpeed);
            }
        }
        else{
            if(direction == 1){
                robot.MotorL.setPower(0);
            }
            else if (direction ==-1){
                robot.MotorR.setPower(0);
            }
        }

        //RIGHT JOYSTICK
        if (r <-0.05 || r > 0.05){
            if(direction == 1){
                robot.MotorR.setPower(r*vr*direction*reduceSpeed);
            }
            else if (direction ==-1){
                robot.MotorL.setPower(r*vl*direction*reduceSpeed);
            }
        }
        else {
            if(direction == 1){
                robot.MotorR.setPower(0);
            }
            else if (direction ==-1){
                robot.MotorL.setPower(0);
            }
        }


        //SHOOTING
        if(gamepad1.a){
            shotspeed = .44;
        }
        if(gamepad1.y){
            shotspeed = .64;
        }

        if(gamepad2.y && robot.LiftServo.getPosition() < .8){//on
            robot.PressServoL.setPosition(1);
            shot = 1;
        }
        if(gamepad2.a){//off
            shot = 0;
        }
        if(shot == 1){
            if(step < (shotspeed - 0.05)){
                step += 0.02;
            }
            if(step > (shotspeed - 0.05)){
                step = shotspeed;
            }
        }
        if(shot == 0){
            if(step > 0.05 ){
                step -= 0.01;
            }
            if(step < 0.08){
                step -= 0.0025;//smaller increments for ending
            }
            if(step < 0.0025){
                step = 0;
            }

        }

        telemetry.addData("shot", shot);
        telemetry.addData("step", step);
        telemetry.addData("shotspeed", shotspeed);
        robot.ShooterDown.setPower(step);
        robot.ShooterUp.setPower(-step);

        //load shooter
        loadStep = robot.ShotFeeder.getPosition();
        if(gamepad2.right_bumper){
            runtime.reset();
            while(runtime.seconds()< 1.5){
                robot.ShotFeeder.setPosition(0);
            }
            robot.ShotFeeder.setPosition(.9);
        }



        /*
        //PROGRAMMED BACKUP
        if(gamepad1.dpad_down){
            startPosR = robot.MotorR.getCurrentPosition();
            backdone = false;
            while((robot.MotorR.getCurrentPosition() > startPosR - 2000) && !backdone){
                robot.MotorR.setPower(vr*.4);
                robot.MotorL.setPower(vr*.4);
                telemetry.addData("encoder units left", robot.MotorR.getCurrentPosition() - startPosR + 2000);
                telemetry.update();
                if(gamepad1.dpad_up){
                    backdone = true;
                }
            }
            robot.MotorR.setPower(0);
            robot.MotorL.setPower(0);
        }*/



        //CONVEYOR
        if(gamepad2.right_trigger > .5){
            robot.Conveyor.setPower(.7);
            robot.TouchServo.setPosition(.15);
        }

        else{
            robot.Conveyor.setPower(0);
        }

        if(gamepad2.b){
            robot.ConveyorServo.setPosition(1);//out
        }
        if(gamepad2.x){
            robot.ConveyorServo.setPosition(0);//in
        }





        //LIFT
        double h = -gamepad2.left_stick_y;
        telemetry.addData("h", h);
        if(((h > 0.05) || (h < -0.05)) && (robot.LiftServo.getPosition() == .85)){//does not lift unless servo out of way
            robot.Lift.setPower(h);
        }
        else{
            robot.Lift.setPower(0);
        }


        //LiftServo up
        if(gamepad2.left_bumper){
            robot.LiftServo.setPosition(.85);
        }
        if(gamepad2.left_trigger > .5){
            robot.LiftServo.setPosition(.05);//down
        }




        //PADDLES
        //left paddle out
        if(gamepad2.dpad_left){
            robot.TouchServo.setPosition(0);
            robot.PressServoL.setPosition(1);
        }

        //right paddle out
        if(gamepad2.dpad_right){
            robot.TouchServo.setPosition(0);
            robot.PressServoR.setPosition(0);
        }

        //both to initial
        if(gamepad2.dpad_down){
            robot.TouchServo.setPosition(0);
            robot.PressServoL.setPosition(0);
            robot.PressServoR.setPosition(1);
        }

        //both out
        if(gamepad2.dpad_up){
            robot.TouchServo.setPosition(0);
            robot.PressServoL.setPosition(0.8);
            robot.PressServoR.setPosition(0);
        }


    }

    @Override
    public void stop() {
        /*robot.PressServoR.setPosition(.88);
        //slow it down
        boolean done3 = false;
        while(!done3) {
            if (step > 0.05) {
                step -= 0.01;
            }
            if (step < 0.05) {
                step = 0;
                done3 = true;
            }
            telemetry.addData("step", step);
            robot.ShooterDown.setPower(step);
            robot.ShooterUp.setPower(-step);
        }*/
    }


}