package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Robot.Team2901RobotHardware;
import org.firstinspires.ftc.teamcode.Robot.TestBotHardware;

/**
 * Created by gallagherb20503 on 9/30/2017.
 */

@TeleOp(name="TestBotDrive", group="TeleOp")
public class Test_Device_2901 extends OpMode     {

    TestBotHardware robot       = new TestBotHardware();

    public static final double LIFT_UP_POWER    =  .3 ;
    public static final double LIFT_DOWN_POWER  = -0.5 ;

    public static final double CLAW_MIN = 0;
    public static final double CLAW_MAX = .7;
    final double LIFT_SPEED = 0.05;
    double liftOffset = 0.0;
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

        robot.liftMotor.setPower(gamepad2.left_stick_y* .5);


        robot.leftMotor.setPower(-LeftDrive*drivePower);
        robot.rightMotor.setPower(-RightDrive* drivePower);


        if (gamepad2.left_trigger > .5) {
            telemetry.addData("","pressed lt");
            robot.bottomLeftClaw.setPosition(Range.clip(robot.bottomLeftClaw.getPosition() - .01, CLAW_MIN, CLAW_MAX));
            robot.bottomRightClaw.setPosition(Range.clip(robot.bottomRightClaw.getPosition() - .01, CLAW_MIN, CLAW_MAX));
        }
        else if (gamepad2.right_trigger > .5) {
            telemetry.addData("", "pressed rt");
            robot.bottomLeftClaw.setPosition(Range.clip(robot.bottomLeftClaw.getPosition() + .01, CLAW_MIN, CLAW_MAX));
            robot.bottomRightClaw.setPosition(Range.clip(robot.bottomRightClaw.getPosition() + .01, CLAW_MIN, CLAW_MAX));
        }

        if (gamepad2.left_bumper) {
            telemetry.addData("","pressed lb");
            robot.topLeftClaw.setPosition(Range.clip(robot.topLeftClaw.getPosition() - .01,CLAW_MIN, CLAW_MAX));
            robot.topRightClaw.setPosition(Range.clip(robot.topRightClaw.getPosition() - .01, CLAW_MIN, CLAW_MAX));
        }
        else if (gamepad2.right_bumper) {
            telemetry.addData("","pressed rb");
            robot.topLeftClaw.setPosition(Range.clip(robot.topLeftClaw.getPosition() + .01, CLAW_MIN, CLAW_MAX));
            robot.topRightClaw.setPosition(Range.clip(robot.topRightClaw.getPosition() + .01, CLAW_MIN, CLAW_MAX));
        }

        telemetry.addData("BottomLeftClaw",robot.bottomLeftClaw.getPosition());
        telemetry.addData("BottomRightClaw",robot.bottomRightClaw.getPosition());
        telemetry.addData("TopLeftClaw",robot.topLeftClaw.getPosition());
        telemetry.addData("TopRightClaw",robot.topRightClaw.getPosition());
        telemetry.addData("LiftMotor",robot.liftMotor.getCurrentPosition());
        telemetry.addData("drivePower",drivePower);

        telemetry.update();
    }



}