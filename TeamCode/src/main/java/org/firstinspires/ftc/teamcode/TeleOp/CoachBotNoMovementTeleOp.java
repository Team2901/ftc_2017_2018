package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware.RRCoachBotHardware;

@Disabled
@TeleOp(name="CoachBotNoMovement")
public class CoachBotNoMovementTeleOp extends OpMode {
    RRCoachBotHardware robot = new RRCoachBotHardware();

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {

        if (gamepad1.y){
            robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        if(gamepad1.left_trigger > .02){
            if(robot.lift.getCurrentPosition() <= 27000 || gamepad1.y ) {
                robot.lift.setPower(1);
            } else {
                robot.lift.setPower(0);
                telemetry.addData("hit the limit", "!");
            }
        } else if (gamepad1.left_bumper){
            if(robot.lift.getCurrentPosition() >= 0  || gamepad1.y) {
                robot.lift.setPower(-1);
            } else {
                robot.lift.setPower(0);
                telemetry.addData("hit the limit", "!!");
            }
        } else {
            robot.lift.setPower(0);
        }

        /*
        if (gamepad2.right_bumper){
            robot.shoulder.setPower(0.3);
        } else if(gamepad2.right_trigger > .02) {
            robot.shoulder.setPower(-0.3);
        } else {
            robot.shoulder.setPower(0);
        }
        */

        if (gamepad2.left_bumper){
            // robot.elbow.setPower(0.3);
            telemetry.addData("left bumper hit", "");
        } else if(gamepad2.left_trigger > .02) {
            // robot.elbow.setPower(-0.3);
            telemetry.addData("left trigger hit", "");
        } else {
            // robot.elbow.setPower(0);
        }

        if (gamepad2.x) {
            robot.marker.setPosition(0);
        } else if(gamepad2.b) {
            robot.marker.setPosition(1);
        } else if (gamepad2.a) {
            robot.marker.setPosition(.5);
        }

        // telemetry.addData("shoulder" , robot.shoulder.getCurrentPosition());
        // telemetry.addData("elbow" , robot.elbow.getCurrentPosition());
        telemetry.addData("lift" , robot.lift.getCurrentPosition());
        telemetry.addData("markerServo" , robot.marker.getPosition());
        telemetry.update();
    }

}