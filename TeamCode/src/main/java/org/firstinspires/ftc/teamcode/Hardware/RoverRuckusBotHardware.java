package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class RoverRuckusBotHardware extends BaseRRHardware{

    public DcMotor left;
    public DcMotor right;
    public DcMotor elbow;
    public DcMotor shoulder;

    public CRServo intake;

    //for never rest 40s
    public static final double ENCODER_COUNTS_PER_REV = 1120;
    //measured may not be exact
    public static final double INCHES_PER_ROTATION = 9.35;

    public RoverRuckusBotHardware() {
        super(0, 1, INCHES_PER_ROTATION);
    }

    public void init(HardwareMap ahwMap) {
        super.init(ahwMap);


        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");
        elbow = hardwareMap.dcMotor.get("elbow");
        shoulder = hardwareMap.dcMotor.get("shoulder");

        intake = hardwareMap.crservo.get("intake");


        left.setDirection(DcMotorSimple.Direction.REVERSE);
        right.setDirection(DcMotorSimple.Direction.FORWARD);

        resetEncoderCounts();

        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shoulder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    @Override
    public void goStraight(double power) {
        left.setPower(power);
        right.setPower(power);
    }

    @Override
    public void turn(double power) {
        left.setPower(power);
        right.setPower(-power);
    }
    @Override
    public void resetEncoderCounts() {
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shoulder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void setMode(DcMotor.RunMode runMode) {
        left.setMode(runMode);
        right.setMode(runMode);
    }

    @Override
    public void setTargetPosition(int targetPosition) {
        left.setTargetPosition(targetPosition);
        right.setTargetPosition(targetPosition);
    }

    @Override
    public boolean isLeftBusy() {
        return left.isBusy();
    }

    @Override
    public int getLeftCurrentPosition() {
        return left.getCurrentPosition();
    }
}