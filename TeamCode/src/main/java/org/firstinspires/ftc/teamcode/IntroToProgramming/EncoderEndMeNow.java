package org.firstinspires.ftc.teamcode.IntroToProgramming;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "EncoderEndMeNow")
public class EncoderEndMeNow extends LinearOpMode {

    DcMotor rightMotor;
    DcMotor leftMotor;

    static final double  COUNTS_PER_MOTOR_REV   = 1120;
    static final double DRIVE_GEAR_RATIO   = 0.5 ;
    static final double WHEEL_DIAMETER_INCHES  = 4.0;
    static final int DESIRED_ENCODER_COUNTS = (int)(60*(WHEEL_DIAMETER_INCHES*3.1415)*DRIVE_GEAR_RATIO*COUNTS_PER_MOTOR_REV);
    static final double DRIVE_SPEED                                                                 = 0.6;
    //What is love?//
    //Baby don't hurt me
    // Don't hurt me
    // No more
    // Baby don't hurt me, don't hurt me
    // No more
    // What is love?
    // Yeah
    // I don't know why you're not fair
    // I give you my love, but you don't care
    // So what is right and what is wrong?
    // Gimme a sign
    // What is love?
    // Baby don't hurt me
    // Don't hurt me
    // No more
    // What is love?
    // Baby don't hurt me
    // Don't hurt me
    // No more
    // Oh, I don't know, what can I do?
    // What else can I say, it's up to you
    // I know we're one, just me and you
    // I can't go on - What is love?
    // Baby don't hurt me
    // Don't hurt me
    // No more
    // What is love?
    // Baby don't hurt me
    // Don't hurt me
    // No more
    // What is love?
    // What is love?
    // What is love?
    // Baby don't hurt me
    // Don't hurt me
    // No more
    // Don't hurt me
    // Don't hurt me
    // I want no other, no other lover
    // This is our life, our time
    // We are together I need you forever
    // Is it love?
    // What is love?
    // Baby don't hurt me
    // Don't hurt me
    // No more
    // What is love?
    // Baby don't hurt me
    // Don't hurt me
    // No more
    // Yeah, yeah, (woah-woah-woah, oh, oh)
    // (Woah-woah-woah, oh, oh)
    // What is love?
    // Baby don't hurt me
    // Don't hurt me
    // No more
    // What is love?
    // Baby don't hurt me
    // Don't hurt me
    // No more
    // Baby don't hurt me
    // Don't hurt me
    // No more
    // Baby don't hurt me
    // Don't hurt me
    // No more
    // What is love?

    @Override
    public void runOpMode() {
            rightMotor=hardwareMap.get(DcMotor.class, "rightMotor");
            leftMotor=hardwareMap.get(DcMotor.class, "leftMotor");
            rightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            waitForStart();
            rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightMotor.setTargetPosition(DESIRED_ENCODER_COUNTS);
            leftMotor.setTargetPosition(DESIRED_ENCODER_COUNTS);
            leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            leftMotor.setPower(DRIVE_SPEED);
            rightMotor.setPower(DRIVE_SPEED);
            while 
    }
}