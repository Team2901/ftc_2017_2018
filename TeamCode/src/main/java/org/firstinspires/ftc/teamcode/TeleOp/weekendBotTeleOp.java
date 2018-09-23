package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.weekendBot;

@TeleOp(name = "WeekendBo ", group = "TeleOp")
public class weekendBotTeleOp extends OpMode {

    weekendBot robot = new weekendBot();

    public void init() {

        robot.init(hardwareMap);

    }

    public void loop() {

        double leftPower = -gamepad1.left_stick_y;
        double rightPower = -gamepad1.right_stick_y;

        robot.left.setPower(leftPower);
        robot.right.setPower(rightPower);
    }

}