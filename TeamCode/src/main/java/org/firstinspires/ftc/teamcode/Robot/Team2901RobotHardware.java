package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by gallagherb20503 on 9/30/2017.
 */

public class Team2901RobotHardware {

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

        leftMotor.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.FORWARD);
        liftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setPower(0);
        rightMotor.setPower(0);
        liftMotor. setPower(0);
    }

    public void initTeleOp(HardwareMap ahwMap) {
        init(ahwMap);

        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void initAutonomous (HardwareMap ahwMap) {
        // Save reference to Hardware map
        init(ahwMap);

        openBottomClaw();
        openTopClaw();

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


    public void closeBottomClaw(){
        bottomLeftClaw.setPosition(.7);
        bottomRightClaw.setPosition(.7);
    }
    public void openBottomClaw(){
        bottomLeftClaw.setPosition(.1);
        bottomRightClaw.setPosition(.1);
    }
    public void closeTopClaw(){
        topLeftClaw.setPosition(.7);
        topRightClaw.setPosition(.7);
    }
    public void openTopClaw(){
        topLeftClaw.setPosition(.1);
        topRightClaw.setPosition(.1);
    }

    public void raiseLift(){
        liftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setTargetPosition(100);
        liftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        liftMotor.setPower(.5);
        while (liftMotor.isBusy()){
        }
    }

}