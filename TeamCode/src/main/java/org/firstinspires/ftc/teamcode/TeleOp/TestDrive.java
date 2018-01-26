package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.Robot.Team2901RobotHardware;

import static java.lang.Thread.sleep;

@TeleOp(name="TestDrive", group="TeleOp")
public class TestDrive extends OpMode {
    final private static int MAJOR_TICK_INC = 250;
    final private static int MINOR_TICK_INC = 50;
    final private static double POWER_INC = .05;

    private Team2901RobotHardware robot = new Team2901RobotHardware();
    private DcMotor leftMotor;
    private DcMotor rightMotor;

    private int tickCount = 1500;
    private double power = 0.5;
    private String mode = "IDLE";

    @Override
    public void init() {
        robot.initTeleOp(hardwareMap);
        rightMotor = robot.rightMotor;
        leftMotor = robot.leftMotor;
    }

    @Override
    public void loop() {
        robot.armServo.setPosition(1);

        if (rightMotor.isBusy() || leftMotor.isBusy()) {
            // DO NOTHING
        } else {
             if (gamepad1.a) {
                mode = "BACKWARDS";
                runTo(-tickCount,-tickCount, power);
            } else if(gamepad1.x) {
                mode = "LEFT";
                runTo(-tickCount,tickCount, power);
            } else if(gamepad1.y) {
                mode = "FORWARD";
                runTo(tickCount,tickCount, power);
            } else if(gamepad1.b) {
                mode = "RIGHT";
                runTo(tickCount,-tickCount, power);
            } else {
                 mode = "IDLE";
                 try {
                 if (gamepad1.left_bumper) {
                     tickCount += MAJOR_TICK_INC;
                     sleep(50);
                 } else if (gamepad1.left_trigger > .5) {
                     tickCount -= MAJOR_TICK_INC;
                     sleep(50);
                 }

                 else if (gamepad1.right_bumper) {
                     tickCount += MINOR_TICK_INC;
                     sleep(50);

                 } else if (gamepad1.right_trigger > .5) {
                     tickCount -= MINOR_TICK_INC;
                     sleep(50);
                 }

                 else if (gamepad1.dpad_up) {
                     power += POWER_INC;
                     sleep(50);
                 } else if (gamepad1.dpad_down) {
                     power -= POWER_INC;
                     sleep(50);
                 }
                 } catch (InterruptedException e) {

                 }
             }
        }

        tickCount = Range.clip(Math.abs(tickCount) , 0, 99999999);
        power = Range.clip(Math.abs(power), 0.0, 1.0);

        telemetry.addData("Power ", "%.2f", power);
        telemetry.addData("State ", "%9s", mode);
        telemetry.addData("Ticks ", "%5d", tickCount);
        telemetry.addData("Left  ", "%5d", leftMotor.getCurrentPosition());
        telemetry.addData("Right ", "%5d", rightMotor.getCurrentPosition());
        telemetry.update();
    }

    private void runTo(final int leftPosition,
                       final int rightPosition,
                       final double power) {
        leftMotor.setPower(0);
        rightMotor.setPower(0);

        leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        leftMotor.setTargetPosition(leftPosition);
        rightMotor.setTargetPosition(rightPosition);

        leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        leftMotor.setPower(power);
        rightMotor.setPower(power);
    }
}
