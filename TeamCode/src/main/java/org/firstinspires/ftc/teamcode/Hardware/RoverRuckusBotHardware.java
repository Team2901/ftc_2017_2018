package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class RoverRuckusBotHardware extends BaseRRHardware{

    public static final double MARKER_INIT_POSITION = 0;
    public static final double MARKER_DROP_POSITION = 1;
    public static final double INCHES_PER_ROTATION = 9.35;

    public DcMotor elbow;
    public DcMotor shoulder;
    public CRServo intake;

    public RoverRuckusBotHardware() {
        super(MARKER_INIT_POSITION, MARKER_DROP_POSITION, INCHES_PER_ROTATION);
    }

    public void init(HardwareMap ahwMap) {
        super.init(ahwMap);

        intake = hardwareMap.crservo.get("intake");

        shoulder = hardwareMap.dcMotor.get("shoulder");
        shoulder.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shoulder.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        elbow = hardwareMap.dcMotor.get("elbow");
        elbow.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elbow.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}