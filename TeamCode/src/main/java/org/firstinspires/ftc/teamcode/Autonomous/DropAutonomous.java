package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.TARGET_LIFT_TICKS;

@Autonomous(name = "DropAutonomous")
public class DropAutonomous extends LinearOpMode {
    public final RoverRuckusBotHardware robot = new RoverRuckusBotHardware();
    public void dropFromLander() {
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setTargetPosition(TARGET_LIFT_TICKS);
        robot.lift.setPower(1);
        while (robot.lift.isBusy()) {
            idle();
        }
    }

    @Override
    public void runOpMode() throws InterruptedException {
        while (opModeIsActive()) {
            idle();
        }
    }
}
