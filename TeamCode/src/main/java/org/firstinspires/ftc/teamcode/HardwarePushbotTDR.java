package org.firstinspires.ftc.teamcode;

import android.graphics.Color;

import com.qualcomm.hardware.adafruit.AdafruitBNO055IMU;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class HardwarePushbotTDR
{
    /* Public OpMode members. */
    public DcMotor  MotorL   = null;
    public DcMotor  MotorR  = null;
    public DcMotor ShooterUp = null;
    public DcMotor ShooterDown = null;
    public DcMotor Conveyor = null;
    public DcMotor Lift = null;
    public Servo PressServoR = null;
    public Servo PressServoL = null;
    public Servo LiftServo = null;
    public Servo ShotFeeder = null;
    public Servo ConveyorServo = null;
    public Servo TouchServo = null;
    public ColorSensor ColSensor = null;
    public TouchSensor TouSensor = null;
    public ColorSensor FruitySensor = null;

// Test was good

 /*
    public DcMotor  armMotor    = null;
    public Servo    leftClaw    = null;
    public Servo    rightClaw   = null;

    public static final double MID_SERVO       =  0.5 ;
    public static final double ARM_UP_POWER    =  0.45 ;
    public static final double ARM_DOWN_POWER  = -0.45 ;
*/
    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwarePushbotTDR(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        MotorL = hwMap.dcMotor.get("MotorL");
        MotorR = hwMap.dcMotor.get("MotorR");
        ShooterUp = hwMap.dcMotor.get("ShooterUp");
        ShooterDown = hwMap.dcMotor.get("ShooterDown");
        Conveyor = hwMap.dcMotor.get("Conveyor");
        Lift = hwMap.dcMotor.get("Lift");
        LiftServo = hwMap.servo.get("LiftServo");//6
        PressServoR = hwMap.servo.get("PressServoR");//1
        PressServoL = hwMap.servo.get("PressServoL");//5
        ConveyorServo = hwMap.servo.get("ConveyorServo");//2
        ShotFeeder = hwMap.servo.get("ShotFeeder");//3
        TouchServo = hwMap.servo.get("TouchServo");
        ColSensor = hwMap.colorSensor.get("ColorSensor");//0, 12c, can't make it ColorSensor
        TouSensor = hwMap.touchSensor.get("TouchSensor");//0, digital, can't make it TouchSensor
        FruitySensor = hwMap.colorSensor.get("FruitySensor");//, 12c

/*;
        armMotor    = hwMap.dcMotor.get("left_arm");
*/
        MotorR.setDirection(DcMotor.Direction.REVERSE); //changed 12/5
        MotorL.setDirection(DcMotor.Direction.FORWARD); //changed 12/5

        // Set all motors to zero power
        MotorL.setPower(0);
        MotorR.setPower(0);
/*
        armMotor.setPower(0);
*/
        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        MotorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        MotorR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
/*
        armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Define and initialize ALL installed servos.
        leftClaw = hwMap.servo.get("left_hand");
        rightClaw = hwMap.servo.get("right_hand");
        leftClaw.setPosition(MID_SERVO);
        rightClaw.setPosition(MID_SERVO);
*/
    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *.
     * @param periodMs  Length of wait cycle in mSec.
     * @throws InterruptedException
     */
    public void waitForTick(long periodMs) throws InterruptedException {

        long  remaining = periodMs - (long)period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}


