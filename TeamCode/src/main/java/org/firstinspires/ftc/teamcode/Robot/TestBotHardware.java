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
    public Servo armServo= null;

    public Servo bottomLeftClaw= null;
    public Servo bottomRightClaw=null;
    public Servo topLeftClaw=null;
    public Servo topRightClaw=null;

    private HardwareMap hwMap = null;

    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        leftMotor = hwMap.dcMotor.get("LeftDrive");
        rightMotor = hwMap.dcMotor.get("RightDrive");
        liftMotor = hwMap.dcMotor.get("LiftMotor");
        armServo = hwMap.servo.get ("ArmServo");
        bottomLeftClaw = hwMap.servo.get("BottomLeftClaw");
        bottomRightClaw = hwMap.servo.get("BottomRightClaw");
        topLeftClaw = hwMap.servo.get("TopLeftClaw");
        topRightClaw = hwMap.servo.get("TopRightClaw");

        bottomLeftClaw.setDirection(Servo.Direction.FORWARD);
        topLeftClaw.setDirection(Servo.Direction.FORWARD);
        bottomRightClaw.setDirection(Servo.Direction.REVERSE);
        topRightClaw.setDirection(Servo.Direction.REVERSE);

        leftMotor.setDirection(DcMotor.Direction.FORWARD); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.REVERSE);

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setPower(0);
        rightMotor.setPower(0);
        liftMotor. setPower(0);

    }

    public void initTeleOp(HardwareMap ahwMap) {
        init(ahwMap);

        bottomLeftClaw.setPosition(0);
        bottomRightClaw.setPosition(0);
        topLeftClaw.setPosition(0);
        topRightClaw.setPosition(0);

        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void initAutonomous (HardwareMap ahwMap) {
        // Save reference to Hardware map
        init(ahwMap);

        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void lowerJewelKnocker(){
        armServo.setPosition(0);
    }

    public void raiseJewelKnocker(){
        armServo.setPosition(1);
    }

}

