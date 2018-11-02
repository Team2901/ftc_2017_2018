package org.firstinspires.ftc.teamcode.IntroToProgramming;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.Hardware.CoachBotHardware;

@TeleOp(name="AdamCoachBot")

public class AdamCoachBotTeleOp extends OpMode {
    CoachBotHardware robot = new CoachBotHardware();
    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        double leftMotor = -gamepad1.left_stick_y;
        double rightMotor = -gamepad1.right_stick_y;
        robot.leftMotor.setPower(leftMotor);
        robot.rightMotor.setPower(rightMotor);


        if(gamepad1.left_trigger > .02){
            if(robot.lift.getCurrentPosition() <= 5000) {
                robot.lift.setPower(-1);
            } else {
                robot.lift.setPower(0);
            }
        } else if (gamepad1.left_bumper){
            if(robot.lift.getCurrentPosition() > 0) {
                robot.lift.setPower(1);
            } else {
                robot.lift.setPower(0);
            }
        } else {
            robot.lift.setPower(0);
        }
        telemetry.addData("lift position", robot.lift.getCurrentPosition());
    }
}