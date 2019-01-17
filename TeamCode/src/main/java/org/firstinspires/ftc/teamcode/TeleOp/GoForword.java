package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;

@TeleOp(name="GoForword")
public class GoForword extends OpMode {
    RoverRuckusBotHardware robot = new RoverRuckusBotHardware();

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    double modifier = .1;
    boolean isAPressed = false;
    boolean isBPressed = false;

    @Override
    public void loop() {

        double left = gamepad1.left_stick_y;
        robot.left.setPower(left);
        robot.right.setPower(left);
    }

}