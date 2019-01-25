package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.BaseRRHardware;
import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;

@TeleOp(name="GoForward")
public class GoForwardTeleOp extends OpMode {
    private static final double MODIFIER = .1;

    private BaseRRHardware robot = new RoverRuckusBotHardware();
    private double dpadSpeed = 1;
    private boolean isAPressed = false;
    private boolean isBPressed = false;

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        double power;

        if (gamepad1.a) {
            if (!isAPressed) {
                isAPressed = true;
                dpadSpeed = Math.min(dpadSpeed + MODIFIER, 1);
            }
        } else {
            isAPressed = false;
        }

        if (gamepad1.b) {
            if (!isBPressed) {
                isBPressed = true;
                dpadSpeed = Math.max(dpadSpeed - MODIFIER, 1);
            }
        } else {
            isBPressed = false;
        }

        if (gamepad1.dpad_up) {
            power = dpadSpeed;
        } else if (gamepad1.dpad_down) {
            power = -dpadSpeed;
        } else {
            power = -gamepad1.left_stick_y;
        }

        robot.goStraight(power);

        telemetry.addData("D-pad Power" , dpadSpeed);
        telemetry.addData("Joystick Power" , -gamepad1.left_stick_y);
        telemetry.addData("Motor Power" , power);
        telemetry.update();
    }
}