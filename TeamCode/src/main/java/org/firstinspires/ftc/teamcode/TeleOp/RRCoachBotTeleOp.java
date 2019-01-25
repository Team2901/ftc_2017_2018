package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.RRCoachBotHardware;

@Disabled
@TeleOp (name = "RRCoachBotTeleOp")
public class RRCoachBotTeleOp extends OpMode {

    RRCoachBotHardware robot = new RRCoachBotHardware();

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        double g1LeftStick = -gamepad1.left_stick_y;
        double g1RightStick = -gamepad1.right_stick_y;



        //Tank COntrols on gamepad 1 sticks
        robot.left.setPower(g1LeftStick);
        robot.right.setPower(g1RightStick);

        telemetry.addData("leftMotor", robot.left.getCurrentPosition());
        telemetry.addData("rightMotor", robot.right.getCurrentPosition());
        telemetry.addData("lift", robot.lift.getCurrentPosition());
        telemetry.addData("Servo", robot.marker.getPosition());
        telemetry.update();
    }
    }
