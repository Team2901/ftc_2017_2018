package org.firstinspires.ftc.teamcode.Robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Created by gallagherb20503 on 9/30/2017.
 */

public class Team2901RobotHardware {

    public DcMotor leftMotor = null;
    public DcMotor rightMotor = null;
    public DcMotor liftMotor= null;
    public Servo clawServo= null;

    private ElapsedTime period  = new ElapsedTime();
    private HardwareMap hwMap = null;

    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftMotor = hwMap.dcMotor.get("LeftDrive");
        rightMotor = hwMap.dcMotor.get("RightDrive");
        liftMotor= hwMap.dcMotor.get("LiftMotor");
        clawServo= hwMap.servo.get ("ClawServo");

        leftMotor.setDirection(DcMotor.Direction.REVERSE); // Set to REVERSE if using AndyMark motors
        rightMotor.setDirection(DcMotor.Direction.FORWARD);

        leftMotor.setPower(0);
        rightMotor.setPower(0);
        liftMotor. setPower(0);


        leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        liftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);



    }

}
