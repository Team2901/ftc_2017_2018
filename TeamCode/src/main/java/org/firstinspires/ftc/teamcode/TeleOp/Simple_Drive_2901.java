package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Hardware.Team2901RobotHardware;

/**
 * Created by gallagherb20503 on 9/30/2017.
 */

@TeleOp(name="SimpleDrive", group="TeleOp")
public class Simple_Drive_2901 extends OpMode     {

    Team2901RobotHardware robot = new Team2901RobotHardware();

    public static final double CLAW_MIN = 0;
    public static final double CLAW_MAX = .7;
    double drivePower;

    @Override
    public void init () {
        robot.initTeleOp(hardwareMap);
    }

    @Override
    public void start() {

    }

    @Override
    public void loop() {
        double LeftDrive;
        double RightDrive;

        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        LeftDrive = -gamepad1.left_stick_y;
        RightDrive = -gamepad1.right_stick_y;
        if (gamepad1.a) {
            drivePower= .25;
        } else if (gamepad1.x) {
            drivePower= .5;
        } else if (gamepad1.y) {
            drivePower= .75;
        } else if (gamepad1.b) {
            drivePower=1.0;
        }

        robot.liftMotor.setPower(gamepad2.left_stick_y* .75);

        robot.leftMotor.setPower(-LeftDrive*drivePower);
        robot.rightMotor.setPower(-RightDrive* drivePower);

        double bottomLeftPosition = robot.bottomLeftClaw.getPosition();
        double bottomRightPosition = robot.bottomRightClaw.getPosition();
        double topLeftPosition = robot.topLeftClaw.getPosition();
        double topRightPosition = robot.topRightClaw.getPosition();

        if (gamepad2.left_trigger > .5) {
            telemetry.addData("","pressed lt");
            bottomLeftPosition -= .01;
            bottomRightPosition -= .01;
        }
        else if (gamepad2.left_bumper) {
            telemetry.addData("", "pressed rt");
            bottomLeftPosition += .01;
            bottomRightPosition += .01;
        }

        if (gamepad2.right_trigger > .5) {
            telemetry.addData("","pressed lb");
            topLeftPosition -= .01;
            topRightPosition -= .01;
        }
        else if (gamepad2.right_bumper) {
            telemetry.addData("","pressed rb");
            topLeftPosition += .01;
            topRightPosition += .01;
        }

        double targetPosition = robot.armServo.getPosition();
        if (gamepad2.a) {
            targetPosition = targetPosition + .01;
            robot.armServo.setPosition(targetPosition);
        } else if (gamepad2.b) {
            targetPosition = targetPosition - .01;
            robot.armServo.setPosition(targetPosition);
        }

        robot.bottomLeftClaw.setPosition(Range.clip(bottomLeftPosition, CLAW_MIN, CLAW_MAX));
        robot.bottomRightClaw.setPosition(Range.clip(bottomRightPosition, CLAW_MIN, CLAW_MAX));
        robot.topLeftClaw.setPosition(Range.clip(topLeftPosition, CLAW_MIN, CLAW_MAX));
        robot.topRightClaw.setPosition(Range.clip(topRightPosition, CLAW_MIN, CLAW_MAX));

        telemetry.addData("BottomLeftClaw",robot.bottomLeftClaw.getPosition());
        telemetry.addData("BottomRightClaw",robot.bottomRightClaw.getPosition());
        telemetry.addData("TopLeftClaw",robot.topLeftClaw.getPosition());
        telemetry.addData("TopRightClaw",robot.topRightClaw.getPosition());
        telemetry.addData("LiftMotor",robot.liftMotor.getCurrentPosition());
        telemetry.addData("drivePower",drivePower);
        telemetry.addData("LeftMotor",robot.leftMotor.getCurrentPosition());
        telemetry.addData("RightMotote",robot.rightMotor.getCurrentPosition());

        telemetry.update();
    }
}