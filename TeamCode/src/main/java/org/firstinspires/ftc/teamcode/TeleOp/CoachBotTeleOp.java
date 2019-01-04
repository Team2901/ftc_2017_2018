package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware.RRCoachBotHardware;

@TeleOp(name="CoachBot")
public class CoachBotTeleOp extends OpMode {
    RRCoachBotHardware robot = new RRCoachBotHardware();
    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        double leftMotor = -gamepad1.left_stick_y;
        double rightMotor = -gamepad1.right_stick_y;
        /*
        robot.leftFront.setPower(leftMotor);
        robot.leftBack.setPower(leftMotor);
        robot.rightFront.setPower(rightMotor);
        robot.rightBack.setPower(rightMotor);
        */

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
            //Arm goes down
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
        /*
        if (gamepad2.left_bumper){
            robot.elbow.setPower(0.3);
        } else if(gamepad2.left_trigger > .02) {
            robot.elbow.setPower(-0.3);
        } else {
            robot.elbow.setPower(0);
        }
        */

        if (gamepad2.x) {
            robot.marker.setPosition(0);
        } else if(gamepad2.b) {
            robot.marker.setPosition(1);
        } else if (gamepad2.a) {
            robot.marker.setPosition(.5);
        }

        /*
        telemetry.addData("leftFrontMotor" , robot.leftFront.getCurrentPosition());
        telemetry.addData("leftBackMotor" , robot.leftBack.getCurrentPosition());
        telemetry.addData("rightFrontMotor" , robot.rightFront.getCurrentPosition());
        telemetry.addData("rightBackMotor" , robot.rightBack.getCurrentPosition());
        */
        telemetry.addData("lift" , robot.lift.getCurrentPosition());
        telemetry.addData("markerServo" , robot.marker.getPosition());
        telemetry.update();
    }

}