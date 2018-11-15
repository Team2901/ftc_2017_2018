package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;

@TeleOp(name="RoverRuckusBotOverride")

public class RoverRuckusBotTeleOpWithOveride extends OpMode {
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

        if(gamepad1.left_trigger > .02){
            if(robot.lift.getCurrentPosition() <= 27000 || gamepad1.y) {
                robot.lift.setPower(1);
            } else {
                robot.lift.setPower(0);
                telemetry.addData("hit the limit", "!");
            }
            //Arm goes down
        } else if (gamepad1.left_bumper){
            if(robot.lift.getCurrentPosition() >= 0 || gamepad1.y) {
                robot.lift.setPower(-1);
            } else {
                robot.lift.setPower(0);
                telemetry.addData("hit the limit", "!!");
            }
        } else {
            robot.lift.setPower(0);
        }
        telemetry.addData("lift position", robot.lift.getCurrentPosition());

        if (gamepad1.x) {
            robot.latch.setPosition(0);
        } else if(gamepad1.b) {
            robot.latch.setPosition(1);
        } else if (gamepad1.a) {
            robot.latch.setPosition(.5);
            }

        telemetry.addData("leftMotor" , robot.left.getCurrentPosition());
        telemetry.addData("rightMotor" , robot.right.getCurrentPosition());
        telemetry.addData("lift" , robot.lift.getCurrentPosition());
        telemetry.addData("Servo" , robot.marker.getPosition());
        telemetry.update();
    }

}