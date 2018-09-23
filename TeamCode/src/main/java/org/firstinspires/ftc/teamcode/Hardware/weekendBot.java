package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;


public class weekendBot {



    private HardwareMap hardwareMap = null;

    public  DcMotor left;
    public DcMotor right;

    public DcMotor shoulder;
    public DcMotor elbow;
    public DcMotor pedipalps;

    public void init(HardwareMap ahwMap) {
        hardwareMap = ahwMap;

        left = hardwareMap.dcMotor.get("left");
        right = hardwareMap.dcMotor.get("right");
        shoulder = hardwareMap.dcMotor.get("shoulder");
        elbow = hardwareMap.dcMotor.get("elbow");
        pedipalps = hardwareMap.dcMotor.get("pedipalps");

        left.setDirection(DcMotorSimple.Direction.REVERSE);
        right.setDirection(DcMotorSimple.Direction.FORWARD);

        left.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        right.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}

