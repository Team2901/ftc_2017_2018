package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class OtherRRHardware {
    public DcMotor left;
    public DcMotor right;

    public void init(HardwareMap ahwMap)
    {
        left = ahwMap.dcMotor.get("left");
        right = ahwMap.dcMotor.get("right");
    }
}
