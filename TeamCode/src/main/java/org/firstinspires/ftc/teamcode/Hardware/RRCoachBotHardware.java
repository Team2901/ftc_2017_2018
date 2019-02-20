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

    public static final double MARKER_INIT_POSITION = 0.5;
    public static final double MARKER_DROP_POSITION = 1;
    public static final double INCHES_PER_ROTATION = 6.25;

    public RRCoachBotHardware() {
        super(MARKER_INIT_POSITION, MARKER_DROP_POSITION, INCHES_PER_ROTATION);
    }

    @Override
    public void init(HardwareMap ahwMap) {
        super.init(ahwMap);
    }
}