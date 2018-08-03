

/**
 * Created by piscullin18641 on 1/30/2018.
 */

package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Hardware.HardwareDxm;


@TeleOp(name="DXM TeleOp_Redesign" , group = "TeleOp")
public class DXM_TeleOp_Redesign extends OpMode {

    HardwareDxm robot = new HardwareDxm();

    final static double SERVO_SHIFT = 0.01;
    //final static double PINCER_MAX = 1;
    //final static double PINCER_MIN = .25;
    public boolean speed = true;

    double liftUpSpeed = 1;
    double liftDownSpeed = .85;
    double leftY1;
    double leftX1;
    double rightX1;


    Servo.Direction bLeft;
    Servo.Direction bRight;
    Servo.Direction tLeft;
    Servo.Direction tRight;


    public void init() {
        robot.init(hardwareMap);
    }

    @Override
    public void loop() {
        //Mechanum Drive
        if (!speed) {
            leftY1 = Math.abs(gamepad1.left_stick_y) > 0.3 ? -gamepad1.left_stick_y : 0;
            leftX1 = Math.abs(gamepad1.left_stick_x) > 0.3 ? gamepad1.left_stick_x : 0;
            rightX1 = Math.abs(gamepad1.right_stick_x) > 0.3 ? gamepad1.right_stick_x : 0;
        } else {
            leftY1 = Math.abs(gamepad1.left_stick_y) > 0.3 ? -gamepad1.left_stick_y / 2 : 0;
            leftX1 = Math.abs(gamepad1.left_stick_x) > 0.3 ? gamepad1.left_stick_x / 2 : 0;
            rightX1 = Math.abs(gamepad1.right_stick_x) > 0.3 ? gamepad1.right_stick_x / 2 : 0;
        }
        double triggerR2 = gamepad2.right_trigger;
        double triggerL2 = gamepad2.left_trigger;

        //double leftY2 = Math.abs(gamepad2.left_stick_y) > 0.3 ? -gamepad2.left_stick_y : 0;

        boolean up = -gamepad2.left_stick_y > .4;
        boolean down = -gamepad2.left_stick_y < -.4;
        boolean bumperR2 = gamepad2.right_bumper;
        boolean bumperL2 = gamepad2.left_bumper;

        double[] wheelPower = wheelPower(leftX1, leftY1, rightX1);
        robot.fLeft.setPower(wheelPower[0]);
        robot.fRight.setPower(wheelPower[1]);
        robot.bLeft.setPower(wheelPower[2]);
        robot.bRight.setPower(wheelPower[3]);

//Speed modifier
        if (gamepad1.x) {
            speed = !speed;
        }
//Change the servo moving direction
        if (gamepad2.a) {
            robot.openBottom();
        } else if (gamepad2.b) {

            robot.bLeftPincer.setPosition(robot.bLeftPincer.getPosition() - SERVO_SHIFT);

            robot.bRightPincer.setPosition(robot.bRightPincer.getPosition() - SERVO_SHIFT);
        }
        if (gamepad2.dpad_down) {
            robot.openTop();

        } else if (gamepad2.dpad_left) {

            robot.tLeftPincer.setPosition(robot.bLeftPincer.getPosition() + SERVO_SHIFT);

            robot.tRightPincer.setPosition(robot.tRightPincer.getPosition() + SERVO_SHIFT);

        }


        telemetry.addData("left joystick y-value: ", leftY1);
        telemetry.addData("left joystick x-value: ", leftX1);
        telemetry.addData("right joystick x-value: ", rightX1);
        telemetry.addData("front left motor: ", wheelPower[0]);
        telemetry.addData("front right motor: ", wheelPower[1]);
        telemetry.addData("back left motor: ", wheelPower[2]);
        telemetry.addData("back right motor: ", wheelPower[3]);

//Jewel knock device control in case the arm is thought to be helpful in the TeleOp Match
        if (gamepad1.a) {
            robot.jewelKnockDevice.setPosition(1);
        } else if (gamepad1.b) {
            robot.jewelKnockDevice.setPosition(.25);
        }
//Arm Control
        if (up) {
            robot.lift.setPower(-liftUpSpeed);
        } else if (down) {
            robot.lift.setPower(liftDownSpeed);
        } else {
            robot.lift.setPower(0);
        }
//Opening and Closing of the servo pincers

        if (triggerL2 > 0.2) {
            robot.bLeftPincer.setPosition(robot.bLeftPincer.getPosition() + SERVO_SHIFT);
        }
        if (triggerR2 > .2) {
            robot.bRightPincer.setPosition(robot.bRightPincer.getPosition() + SERVO_SHIFT);
        }
        if (gamepad2.left_bumper) {
            robot.tLeftPincer.setPosition(robot.bLeftPincer.getPosition() + SERVO_SHIFT);
        }
        if (gamepad2.right_bumper) {
            robot.tRightPincer.setPosition(robot.tRightPincer.getPosition() + SERVO_SHIFT);
        }

//

//Allow to switch direction of the servos for telemetry purposes

        bLeft = robot.bLeftPincer.getDirection();
        bRight = robot.bRightPincer.getDirection();
        tLeft = robot.tLeftPincer.getDirection();
        tRight = robot.tRightPincer.getDirection();

        telemetry.addData("bLeft Direction", bLeft);
        telemetry.addData("bRight Direction", bRight);
        telemetry.addData("tLeft Direction", tLeft);
        telemetry.addData("tRight Direction", tRight);

        telemetry.addData("bLeft Position", robot.bLeftPincer.getPosition());
        telemetry.addData("bRight Position", robot.bRightPincer.getPosition());
        telemetry.addData("tLeft Position", robot.tLeftPincer.getPosition());
        telemetry.addData("tRight Position", robot.tRightPincer.getPosition());

//**************************************************************************************************
        telemetry.update();
    }


    public double[] wheelPower(double x, double y, double r) {

        double speed = speed(x, y);
        double angle = angle(x, y);
        double pFL = (speed * (Math.sin((angle) + ((Math.PI) / 4)))) + r;
        double pFR = (speed * (Math.cos((angle) + ((Math.PI) / 4)))) - r;
        double pBL = (speed * (Math.cos((angle) + ((Math.PI) / 4)))) + r;
        double pBR = (speed * (Math.sin((angle) + ((Math.PI) / 4)))) - r;
        double[] wP = {pFL, pFR, pBL, pBR};
        telemetry.addData("speed: ", speed);
        telemetry.addData("angle: ", angle);

        return wP;
    }

    public double speed(double x, double y) {
        return Math.sqrt((Math.pow(x, 2)) + (Math.pow(y, 2)));
    }

    public double angle(double x, double y) {
        if ((y == 0) && (x == 0))
            return 0;
        else
            return Math.atan2(x, y);
    }

    public void setPincer(double x) {
        robot.bLeftPincer.setPosition(x);
        robot.bRightPincer.setPosition(x);
        robot.tLeftPincer.setPosition(x);
        robot.tRightPincer.setPosition(x);
    }
}