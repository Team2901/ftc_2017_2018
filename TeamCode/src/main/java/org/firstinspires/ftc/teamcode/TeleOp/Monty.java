package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.teamcode.Robot.ClawbotHardware;
import org.firstinspires.ftc.teamcode.Robot.FlagBotHardware;
import org.firstinspires.ftc.teamcode.Robot.Team2901Robot;

/**
 * Created by butterss21317 on 12/12/2017.
 */

@TeleOp(name="RobotTemplate", group="TeleOp")
public class RobotTemplate extends OpMode  {

    Team2901Robot robot = new Team2901Robot();

    @Override
    public void init() {

        robot.init(hardwareMap);

    }

    double lMotor = -(gamepad1.left_stick_y);
    double rMotor = -(gamepad1.right_stick_y);

    robot.leftMotor.
    robot.rightMotor.

    @Override
    public void loop() {
        double left;
        double right;
        int turbo = 0;

        if (gamepad1.right_trigger > .1 && turbo== 0){
            turbo = 1;
        }
        else if (gamepad1.right_trigger > .1 && turbo ==1) {
            turbo = 0;
        }
        // Run wheels in tank mode (note: The joystick goes negative when pushed forwards, so negate it)

        if (turbo == 0) {
            left = -gamepad1.left_stick_y;
            right = -gamepad1.right_stick_y;
            robot.leftMotor.setPower(left / 2);
            robot.rightMotor.setPower(right / 2);

        }
        if (turbo == 1) {
            left = -gamepad1.left_stick_y;
            right = -gamepad1.right_stick_y;
            robot.leftMotor.setPower(left);
            robot.rightMotor.setPower(right);
        }
        // Do not let the servo position go too wide or the plastic gears will come unhinged.

        // Move both servos to new position.

        // Use gamepad buttons to move the arm up (Y) and down (A)
        if (gamepad1.a)
            robot.spinMotor.setPower(.75);
        else if (gamepad1.b)
            robot.spinMotor.setPower(-.75);
        else
            robot.spinMotor.setPower(0.0);

        // Send telemetry message to signify robot running;

    }
}


  /*    public void robotInit() {

        double frontLeft = -(gamepad1.left_stick_y);
        double frontRight = -(gamepad1.right_stick_y);
        double rearLeft = -(gamepad1.left_stick_y);
        double rearRight = -(gamepad1.right_stick_y);

        robot.   .setPower(frontLeft);_
        robot.   .setPower(frontRight);_
        robot.   .setPower(rearLeft);_
        robot.   .setPower(rearRight);_