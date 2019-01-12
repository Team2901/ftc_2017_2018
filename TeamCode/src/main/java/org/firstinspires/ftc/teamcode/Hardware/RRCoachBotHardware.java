package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class RRCoachBotHardware extends BaseRRHardware{

    public DcMotor left;
    public DcMotor right;
    public RRCoachBotHardware() {
        super(0.5, 1,12.5 / 2);
    }

    @Override

    public void init(HardwareMap ahwMap) {
        super.init(ahwMap);


        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");



        left.setDirection(DcMotorSimple.Direction.REVERSE);
        right.setDirection(DcMotorSimple.Direction.FORWARD);

        resetEncoderCounts();

        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


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