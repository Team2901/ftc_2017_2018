package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robot.Team2901RobotHardware;

/**
 * Created by gallagherb20503 on 9/30/2017.
 */

@TeleOp(name="SimpleDrive", group="TeleOp")
public class Simple_Drive_2901 extends OpMode     {

    Team2901RobotHardware robot       = new Team2901RobotHardware();

    public static final double LIFT_UP_POWER    =  .15 ;
    public static final double LIFT_DOWN_POWER  = -0.5 ;

    final double LIFT_SPEED = 0.05;
    double liftOffset = 0.0;

    @Override
    public void init () {

        robot.init(hardwareMap);
    }


    @Override
    public void loop() {
        double LeftDrive;
        double RightDrive;
        double LiftMotor = gamepad1. right_trigger;


        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        LeftDrive = -gamepad1.left_stick_y;
        RightDrive = -gamepad1.right_stick_x;

        // Use gamepad buttons to move the arm up (Y) and down (A)
        if (gamepad1.y)
            robot.LiftMotor.setPower(LIFT_UP_POWER);
        else if (gamepad1.a)
            robot.LiftMotor.setPower(LIFT_DOWN_POWER);
        else
            robot.LiftMotor.setPower(0.0);

        // Use gamepad left & right Bumpers to open and close the claw
        if (gamepad1.right_bumper)
            liftOffset += LIFT_SPEED;
        else if (gamepad1.left_bumper)
            liftOffset -= LIFT_SPEED;

        robot.leftMotor.setPower(LeftDrive);
        robot.rightMotor.setPower(RightDrive);
        robot.LiftMotor.setPower(LiftMotor);
    }
}