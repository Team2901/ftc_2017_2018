package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;

@TeleOp(name="RoverRuckusBot")

public class RoverRuckusBotTeleOp extends OpMode {
    RoverRuckusBotHardware robot = new RoverRuckusBotHardware();
    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        double leftMotor = -gamepad1.left_stick_y;
        double rightMotor = -gamepad1.right_stick_y;
        robot.left.setPower(leftMotor);
        robot.right.setPower(rightMotor);

        if (gamepad1.left_trigger > .02) {
            robot.lift.setPower(1);
        } else if (gamepad1.left_bumper) {
            robot.lift.setPower(-1);
        } else {
            robot.lift.setPower(0);
        }

        if (gamepad1.x) {
            robot.latch.setPosition(0);
        } else if(gamepad1.b) {
            robot.latch.setPosition(1);
        } else if (gamepad1.a) {
            robot.latch.setPosition(.5);
        }
    }
}