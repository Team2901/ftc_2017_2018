package org.firstinspires.ftc.teamcode.Hardware;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class SonarRobotThing {
    public DcMotor leftMotor = null;
    public DcMotor rightMotor = null;
    public ModernRoboticsI2cRangeSensor sonarSensor= null;

    public void init(HardwareMap ahwmap){
        ahwmap.dcMotor.get("leftMotor");

        leftMotor = ahwmap.dcMotor.get("leftMotor");
        rightMotor = ahwmap.dcMotor.get("rightMotor");
        sonarSensor = ahwmap.get(ModernRoboticsI2cRangeSensor.class, "sonarSensor");
leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

}
