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

public abstract class BaseRRHardware {
    protected HardwareMap hardwareMap = null;

    public static final double ENCODER_COUNTS_PER_REV = 1120; //for never rest 40s

    public DcMotor lift;

    public Servo marker;

    public BNO055IMU imu;
    public IntegratingGyroscope gyroscope;
    public double offset = 0;

    public double markerInitPosition = 0;
    public double markerDropPosition = 0;
    public double inchesPerRotation = 0;

    public BaseRRHardware(double markerInitPosition, double markerDropPosition, double inchesPerRotation) {
        this.markerInitPosition = markerInitPosition;
        this.markerDropPosition = markerDropPosition;
        this.inchesPerRotation = inchesPerRotation;
    }

    public void init(HardwareMap ahwMap) {
        hardwareMap = ahwMap;

        lift = hardwareMap.dcMotor.get("lift");

        lift.setDirection(DcMotorSimple.Direction.FORWARD);

        lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        marker = hardwareMap.servo.get("marker");
        marker.setPosition(markerInitPosition);

        imu = ahwMap.get(BNO055IMU.class, "imu");
        gyroscope = (IntegratingGyroscope) imu;

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        imu = ahwMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    public double getAngle() {
        Orientation orientation = gyroscope.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return AngleUnit.normalizeDegrees(orientation.firstAngle + offset);
    }

    public abstract void goStraight(double power);

    public abstract void turn(double power);

    public abstract void resetEncoderCounts();
    public abstract void setMode(DcMotor.RunMode runMode);

    public abstract void setTargetPosition(int targetPosition);
    public abstract boolean isLeftBusy();
    public abstract int getLeftCurrentPosition();

    public double getInchesToEncoderCounts() {
        return ((1 / inchesPerRotation) * ENCODER_COUNTS_PER_REV);
    }
}