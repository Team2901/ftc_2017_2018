package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by gallagherb20503 on 11/7/2017.
 */

public class RedCode {

    public DcMotor leftMotor = null;
    public DcMotor rightMotor = null;

    private ElapsedTime period = null;
    private HardwareMap hwMap = null;

    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware Map
        hwMap = ahwMap;
        // Define and Intitialize Motors

    }


}
