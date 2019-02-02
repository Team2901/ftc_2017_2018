package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;

@TeleOp(name="RoverRuckusBot")
public class RoverRuckusBotTeleOp extends OpMode {
    RoverRuckusBotHardware robot = new RoverRuckusBotHardware();

    @Override
    public void init() {
        robot.init(hardwareMap);
    }

    double modifier = 1;
    boolean isAPressed = false;
    boolean isBackwards = false;

    @Override
    public void loop() {

        //Values of both gamepad's joystick
        double g1LeftStick = -gamepad1.left_stick_y;
        double g1RightStick = -gamepad1.right_stick_y;
        double g2RightStick = -gamepad2.right_stick_y;
        double g2LeftStick = -gamepad2.left_stick_y;


        if (gamepad1.dpad_up) {
            robot.left.setPower(1);
            robot.right.setPower(1 * modifier);
        } else if (gamepad1.dpad_down) {
            robot.left.setPower(-1);
            robot.right.setPower(-1);
        } else {
            robot.left.setPower(g1LeftStick);
            robot.right.setPower(g1RightStick * modifier);
        }


        if(gamepad2.a && !isAPressed){
            if(isBackwards = true){
                robot.left.setDirection(DcMotorSimple.Direction.REVERSE);
                robot.right.setDirection(DcMotorSimple.Direction.FORWARD);
                isBackwards = !isBackwards;
            }else{
                robot.left.setDirection(DcMotorSimple.Direction.FORWARD);
                robot.right.setDirection(DcMotorSimple.Direction.REVERSE);
                isBackwards = !isBackwards;
            }
        }
        isAPressed = gamepad2.a;
        /*
        Might Change but lift being limited between the physical restraints of the mechanism and
          being controlled by left and right trigger. Limit can be reset by hittig y
          */
        if (gamepad1.y) {
            robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.lift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        }

        if (gamepad1.left_bumper) {
            robot.lift.setPower(1);
        } else if (gamepad1.left_trigger > .2) {
            robot.lift.setPower(-1);
        } else {
            robot.lift.setPower(0);
        }

        /*Arm controls Currently: Gamepad 2 Right joystick controls elbow left joystick
        controls shoulder
        */


        robot.shoulder.setPower(-g2LeftStick / 4);
        robot.elbow.setPower(-g2RightStick / 4);

        //Intake Mechanism will be operated off gamepad 2 triggers
        if (gamepad2.right_trigger > .2) {
            robot.intake.setPower(gamepad2.right_trigger);
        } else if (gamepad2.left_trigger > .2) {
            robot.intake.setPower(-gamepad2.left_trigger);
        } else {
            robot.intake.setPower(0);
        }


        // telemetry.addData("lift position", robot.lift.getCurrentPosition());
        telemetry.addData("Modifier", modifier);
        telemetry.addData("Joystick Left", g1LeftStick);
        telemetry.addData("Joystick Right", g1RightStick);
        telemetry.addData("leftMotor", robot.left.getCurrentPosition());
        telemetry.addData("rightMotor", robot.right.getCurrentPosition());
        telemetry.addData("lift", robot.lift.getCurrentPosition());
        //telemetry.addData("Servo", robot.marker.getPosition());
        telemetry.update();
    }

}