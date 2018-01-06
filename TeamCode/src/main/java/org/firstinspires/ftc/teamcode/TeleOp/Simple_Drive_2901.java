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

    public static final double LIFT_UP_POWER    =  .3 ;
    public static final double LIFT_DOWN_POWER  = -0.5 ;

    final double LIFT_SPEED = 0.05;
    double liftOffset = 0.0;

    @Override
    public void init () {

        robot.initTeleOp(hardwareMap);
    }


    @Override
    public void loop() {
        double LeftDrive;
        double RightDrive;
        double drivePower=1;


        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        LeftDrive = -gamepad1.left_stick_y;
        RightDrive = -gamepad1.right_stick_y;
        if (gamepad1.a) {
            drivePower= .25;
        }
        if (gamepad1.x) {
            drivePower= .5;
        }
        if (gamepad1.y) {
            drivePower= .75;
        }
        if (gamepad1.b) {
            drivePower=1.0;
        }

        robot.liftMotor.setPower(gamepad2.right_stick_y* -1);


        robot.leftMotor.setPower(LeftDrive*drivePower);
        robot.rightMotor.setPower(RightDrive* drivePower);


       double targetPosition = robot.clawServo.getPosition();
        if (gamepad2.b) {
            telemetry.addData("","pressed b");
            targetPosition= targetPosition + .01;
            robot.clawServo.setPosition(targetPosition);
        }
        else if (gamepad2.x) {
            telemetry.addData("","pressed x");
            targetPosition= targetPosition - .01;
            robot.clawServo.setPosition(targetPosition);
        }

        targetPosition= robot.armServo.getPosition();
        if (gamepad2.left_trigger > .5 ) {
           robot.armServo.setPosition(.75);
        }

        telemetry.addData("ClawServo",robot.clawServo.getPosition());
        telemetry.addData("LiftMotor",robot.liftMotor.getCurrentPosition());
        telemetry.update();
    }

}