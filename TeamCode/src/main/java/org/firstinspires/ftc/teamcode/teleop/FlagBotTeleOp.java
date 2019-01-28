package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.hardware.FlagBotHardware;

/**
 * Created by Kearneyg20428 on 2/7/2017.
 */
@TeleOp(name = "Flagbot", group = "TeleOp")
public class FlagBotTeleOp extends OpMode {

    final double CLAW_SPEED = 0.05;
    double clawOffset = 0.0;
    boolean turbo = false;
    final FlagBotHardware robot = new FlagBotHardware();

    boolean isTurboPressed = false;
    double speedRatio = 0.4;

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {

        if (gamepad1.left_trigger > .1) {
            if (!isTurboPressed) {
                if (speedRatio == 0.4) {
                    speedRatio = 0.6;
                } else if (speedRatio == 0.6){
                    speedRatio = 1.0;
                } else {
                    speedRatio = 0.4;
                }
            }
            isTurboPressed = true;
        } else {
            isTurboPressed = false;
        }

        double left = 0;
        double right = 0;
        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)
        if (gamepad1.dpad_up) {
            left = 1;
            right = 1;
        } else if (gamepad1.dpad_down) {
            left = -1;
            right = -1;
        } else if (gamepad1.dpad_left) {
            left = -1;
            right = 1;
        } else if (gamepad1.dpad_right) {
            left = 1;
            right = -1;
        } else {
            left = -gamepad1.left_stick_y;
            right = -gamepad1.right_stick_y;
        }

        robot.leftMotor.setPower(speedRatio * left);
        robot.rightMotor.setPower(speedRatio * right);

        // Do not let the servo position go too wide or the plastic gears will come unhinged.

        // Move both servos to new position.

        // Use gamepad buttons to move the arm up (Y) and down (A)
        if (gamepad1.a)
            robot.spinMotor.setPower(.75);
        else if (gamepad1.b)
            robot.spinMotor.setPower(-.75);
        else
            robot.spinMotor.setPower(0.0);

        // Send telemetry message to signify robot running;
        telemetry.addData("SpeedRatio", speedRatio);
        telemetry.update();
    }
}
