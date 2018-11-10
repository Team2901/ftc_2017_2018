package org.firstinspires.ftc.teamcode.IntroToProgramming;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "EncoderBeignet")
@Disabled

public class EncoderBeignet extends LinearOpMode {

    DcMotor rightMotor;
    DcMotor leftMotor;

    static final double     COUNTS_PER_MOTOR_REV    = 1120 ;
    static final double     DRIVE_GEAR_RATIO        = 2.0 ;
    static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;
    static final int        DESIRED_ENCODER_COUNTS  = (int)((60 / (WHEEL_DIAMETER_INCHES * 3.1415) * DRIVE_GEAR_RATIO) * COUNTS_PER_MOTOR_REV) ;
    static final double     DRIVE_SPEED             = 0.6;

    @Override
    public void runOpMode() {


        leftMotor = hardwareMap.get(DcMotor.class, "leftMotor");
        rightMotor = hardwareMap.get(DcMotor.class, "rightMotor");
        rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        waitForStart();

        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);




        rightMotor.setTargetPosition(DESIRED_ENCODER_COUNTS);
        leftMotor.setTargetPosition(DESIRED_ENCODER_COUNTS);

        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        rightMotor.setPower(DRIVE_SPEED);
        leftMotor.setPower(DRIVE_SPEED);
        while (leftMotor.isBusy() && rightMotor.isBusy()){
            telemetry.addData("encoder counts", "%d counts on right motor", rightMotor.getCurrentPosition());
            telemetry.addData("encoder counts", "%d counts on left motor", leftMotor.getCurrentPosition());
            telemetry.addData("desired encoder counts", "%d", DESIRED_ENCODER_COUNTS);
            telemetry.update();
            // idle();
        }
        rightMotor.setPower(0);
        leftMotor.setPower(0);




    }
}
