package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Hardware.OtherRRHardware;

public class OtherRRTeleOp extends OpMode {
    OtherRRHardware robot = new OtherRRHardware();

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        if(gamepad1.dpad_up)
        {
            robot.left.setPower(1);
            robot.right.setPower(1);
        }
        else if(gamepad1.dpad_down)
        {
            robot.left.setPower(-1);
            robot.right.setPower(-1);
        }
    }
}
