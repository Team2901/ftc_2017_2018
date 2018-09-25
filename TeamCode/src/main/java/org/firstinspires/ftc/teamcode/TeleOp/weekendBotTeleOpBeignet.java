package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.weekendBot;

@TeleOp(name="WeekendBotBeignet")

public class weekendBotTeleOpBeignet extends OpMode {
    weekendBot robot = new weekendBot();
    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        double leftMotor=gamepad1.left_stick_y;
        double rightMotor=gamepad1.right_stick_y;
        robot.left.setPower(leftMotor);
        robot.right.setPower(rightMotor);

        if (gamepad2.right_trigger > 0.2){
            robot.shoulder.setPower(1);
        }
        else if (gamepad2.left_trigger > .2){
            robot.shoulder.setPower(-1);
        }
        else {
            robot.shoulder.setPower(0);
        }

        if (gamepad2.right_bumper) {
            robot.elbow.setPower(0.25);
        }
        else if (gamepad2.left_bumper) {
            robot.elbow.setPower(-0.25);
        }
        else {
            robot.elbow.setPower(0);
        }

        if (gamepad2.a) {
            robot.pedipalps.setPower(0.25);
        }
        else if(gamepad2.b) {
            robot.pedipalps.setPower(-0.25);
        }
        else {
            robot.pedipalps.setPower(0);
        }
    }
}