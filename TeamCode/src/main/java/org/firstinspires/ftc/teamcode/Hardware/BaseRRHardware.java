package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IntegratingGyroscope;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.Utility.AngleUtilities;

public abstract class BaseRRHardware {

    public static final double ENCODER_COUNTS_PER_REV = 1120; // For neverRest 40s

    public HardwareMap hardwareMap = null;
    public DcMotor left;
    public DcMotor right;
    public DcMotor lift;
    public Servo marker;
    public WebcamName webcam = null;
    public BNO055IMU imu;
    public IntegratingGyroscope gyroscope;
    public double offset = 0;
    public double tiltOffset = 0;

    public final double markerInitPosition;
    public final double markerDropPosition;
    public final double inchesPerRotation;

    public BaseRRHardware(double markerInitPosition, double markerDropPosition, double inchesPerRotation) {
        this.markerInitPosition = markerInitPosition;
        this.markerDropPosition = markerDropPosition;
        this.inchesPerRotation = inchesPerRotation;
    }

    public void init(HardwareMap ahwMap) {
        hardwareMap = ahwMap;

        left = hardwareMap.dcMotor.get("left");
        left.setDirection(DcMotorSimple.Direction.REVERSE);
        left.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        right = hardwareMap.dcMotor.get("right");
        right.setDirection(DcMotorSimple.Direction.FORWARD);
        right.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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

        try {
            webcam = hardwareMap.get(WebcamName.class, "webcam");
        } catch (IllegalArgumentException e) {
            webcam = null;
        }
    }

    public double getAngle() {
        return AngleUtilities.getNormalizedAngle(getRawAngle() + offset);
    }

    public double getRawAngle() {
        Orientation orientation = gyroscope.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return AngleUtilities.getNormalizedAngle(orientation.firstAngle);
    }

    public double getTilt() {
        return AngleUtilities.getNormalizedAngle(getRawTilt() + tiltOffset);
    }

    public double getRawTilt() {
        Orientation orientation = gyroscope.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        return AngleUtilities.getNormalizedAngle(orientation.secondAngle);
    }

    public boolean isTiltedToRedCard() {
        if (Math.abs(getTilt()) >= 15) {
            goStraight(0);
            return true;
        }
        return false;
    }

    public void goStraight(double power) {
        left.setPower(power);
        right.setPower(power);
    }

    public void turn(double power) {
        left.setPower(power);
        right.setPower(-power);
    }

    public void setMode(DcMotor.RunMode runMode) {
        left.setMode(runMode);
        right.setMode(runMode);
    }

    public void setTargetPosition(int targetPosition) {
        left.setTargetPosition(targetPosition);
        right.setTargetPosition(targetPosition);
    }

    public double getInchesToEncoderCounts() {
        return ((1 / inchesPerRotation) * ENCODER_COUNTS_PER_REV);
    }

    public abstract void armOut(LinearOpMode linearOpMode);
}