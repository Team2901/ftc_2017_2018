package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.WeekendBotHardware;

@TeleOp(name = "WeekendBo ", group = "TeleOp")
@Disabled
public class WeekendBotTeleOp extends OpMode {

    WeekendBotHardware robot = new WeekendBotHardware();

    public void init() {
        robot.init(hardwareMap);
    }

    public void loop() {

        double leftPower = gamepad1.left_stick_y;
        double rightPower = gamepad1.right_stick_y;

        robot.left.setPower(leftPower);
        robot.right.setPower(rightPower);

        if (gamepad2.right_trigger > .02) {
            robot.shoulder.setPower(gamepad2.right_trigger);
        } else if (gamepad2.left_trigger > .02) {
            robot.shoulder.setPower(-gamepad2.left_trigger);
        }

        if (gamepad2.right_bumper) {
            robot.elbow.setPower(.25);
        } else if (gamepad2.left_bumper) {
            robot.elbow.setPower(-.25);
        }

        if (gamepad2.a) {
            robot.pedipalps.setPower(.25);
        } else if (gamepad2.b) {
            robot.pedipalps.setPower(-.25);
        }
    }
}
