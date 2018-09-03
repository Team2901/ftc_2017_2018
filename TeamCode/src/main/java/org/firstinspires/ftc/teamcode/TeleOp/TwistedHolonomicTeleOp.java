package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Hardware.SlideDrive;
import org.firstinspires.ftc.teamcode.Hardware.TwistedHolonomicDrive;

/**
 * Created by Kearneyg20428 on 2/7/2017.
 */
@TeleOp(name="Twisted Holonomic", group="TeleOp")
public class TwistedHolonomicTeleOp extends OpMode {


    TwistedHolonomicDrive robot = new TwistedHolonomicDrive();

    @Override
    public void init() {

        robot.init(hardwareMap);

    }

    @Override
    public void loop() {
        double rightStick = gamepad1.right_stick_x;
        double xJoystick = gamepad1.left_stick_x;
        double yJoystick = -gamepad1.left_stick_y;

        if (rightStick > .2 || rightStick < -.2) {
            robot.leftFrontMotor.setPower(-rightStick);
            robot.leftBackMotor.setPower(-rightStick);
            robot.rightFrontMotor.setPower(-rightStick);
            robot.rightBackMotor.setPower(-rightStick);
        } else {


            robot.leftFrontMotor.setPower(xJoystick);
            robot.leftBackMotor.setPower(yJoystick);
            robot.rightFrontMotor.setPower(-yJoystick);
            robot.rightBackMotor.setPower(-xJoystick);
        }


    }
}
