package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by butterss21317 on 9/19/2017.
 */

public class TestBotHardware {



    public DcMotor leftMotor = null;
    public DcMotor rightMotor = null;
    public DcMotor liftMotor= null;
    public Servo bottomLeftClaw= null;
    public Servo bottomRightClaw=null;
    public Servo topLeftClaw=null;
    public Servo topRightClaw=null;


    private ElapsedTime period  = new ElapsedTime();
    private HardwareMap hwMap = null;

    public void initTeleOp(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftMotor = hwMap.dcMotor.get("LeftDrive");
        rightMotor = hwMap.dcMotor.get("RightDrive");
        liftMotor= hwMap.dcMotor.get("LiftMotor");
        bottomLeftClaw= hwMap.servo.get ("BottomLeftClaw");
        bottomRightClaw= hwMap.servo.get ("BottomRightClaw");
        topLeftClaw= hwMap.servo.get ("TopLeftClaw");
        topRightClaw= hwMap.servo.get ("TopRightClaw");

        bottomRightClaw.setDirection(Servo.Direction.REVERSE);
        topRightClaw.setDirection(Servo.Direction.REVERSE);

        bottomLeftClaw.setPosition(0);
        bottomRightClaw.setPosition(0);
        topLeftClaw.setPosition(0);
        topRightClaw.setPosition(0);

        leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        leftMotor.setPower(0);
        rightMotor.setPower(0);
        liftMotor. setPower(0);


        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // armServo.setPosition(0);

    }


    public void initAutonomous (HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftMotor = hwMap.dcMotor.get("LeftDrive");
        rightMotor = hwMap.dcMotor.get("RightDrive");
        liftMotor= hwMap.dcMotor.get("LiftMotor");
        bottomLeftClaw= hwMap.servo.get ("BottomLeftClaw");
        bottomRightClaw= hwMap.servo.get ("BottomRightClaw");
        topLeftClaw= hwMap.servo.get ("TopLeftClaw");
        topRightClaw= hwMap.servo.get ("TopRightClaw");

        leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        leftMotor.setPower(0);
        rightMotor.setPower(0);
        liftMotor. setPower(0);


        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);




    }
    public void moveTo (double power, int inches ){

        leftMotor.setTargetPosition(570*inches);
        rightMotor.setTargetPosition(570*inches);

        leftMotor.setPower(power);
        rightMotor.setPower(power);
    }

    public void raiseLift(int encodercount){
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setTargetPosition(encodercount);
        liftMotor.setPower(.5);

    }


}

