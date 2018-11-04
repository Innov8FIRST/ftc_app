package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 * <p>
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 * <p>
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 * <p>
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 */
public class HardwareInnov8Tinkerbell {
    /* Public OpMode members. */
    public DcMotor leftMotor = null;
    public DcMotor rightMotor = null;
    public DcMotor leftChain = null;
    //public DcMotor rightChain = null;

    // public Servo    handL        = null;
    // public Servo    handR        = null;
    // public Servo    jewelArm     = null;
    // public Servo    leftJewelArm = null;
    // public Servo    bigfoot      = null;
    public Servo hook = null;

    //  public Servo    claw         = null;

    //public ColorSensor leftFruity = null;
    //public ColorSensor rightFruity = null;

    public static final double MID_SERVO = 0.5;
    public static final double ARM_UP_POWER = 0.45;
    public static final double ARM_DOWN_POWER = -0.45;
    public static final double START_SERVO = 0; // all the way down
    public static final double END_SERVO = 1; // all the way up

    /* local OpMode members. */
    HardwareMap hwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    public HardwareInnov8Tinkerbell() {

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftMotor = hwMap.dcMotor.get("leftMotor");
        rightMotor = hwMap.dcMotor.get("rightMotor");
        leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.REVERSE);// Set to FORWARD if using AndyMark motors
        //leftFruity = hwMap.colorSensor.get("leftFruity");
        //rightFruity = hwMap.colorSensor.get("rightFruity");
        leftChain = hwMap.dcMotor.get("leftChain");
        leftChain.setDirection(DcMotor.Direction.FORWARD);
        // rightChain = hwMap.dcMotor.get("rightChain");
        //rightChain.setDirection(DcMotor.Direction.FORWARD);

        // Set all motors to zero power
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        leftChain.setPower(0);
        //rightChain.setPower(0);

        // Set all motors to run without encoders.

        // May want to use RUN_USING_ENCODERS if encoders are installed.
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftChain.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
      //  rightChain.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Define and initialize ALL installed servos.
        //  jewelArm = hwMap.servo.get("jewelArm");
        //  jewelArm.setPosition(START_SERVO);
        //  leftJewelArm = hwMap.servo.get("leftJewelArm");
        //  leftJewelArm.setPosition(START_SERVO);
        //  bigfoot = hwMap.servo.get("bigfoot");
        //  bigfoot = hwMap.servo.get("bigfoot");
        //  handL = hwMap.servo.get("handL");
        //  handL.setPosition(0);
        //  handR = hwMap.servo.get("handR");
        //  handR.setPosition(0.4);
        hook = hwMap.servo.get("hook");
        hook.setPosition(START_SERVO);
    }

    /***
     *
     * waitForTick implements a periodic delay. However, this acts like a metronome with a regular
     * periodic tick.  This is used to compensate for varying processing times for each cycle.
     * The function looks at the elapsed cycle time, and sleeps for the remaining time interval.
     *
     * @param periodMs  Length of wait cycle in mSec.
     * @throws InterruptedException
     */
    public void waitForTick(long periodMs) throws InterruptedException {

        long remaining = periodMs - (long) period.milliseconds();

        // sleep for the remaining portion of the regular cycle period.
        if (remaining > 0)
            Thread.sleep(remaining);

        // Reset the cycle clock for the next pass.
        period.reset();
    }
}

