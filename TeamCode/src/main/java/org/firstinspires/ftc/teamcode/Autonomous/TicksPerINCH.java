package org.firstinspires.ftc.teamcode.Autonomous;

import org.firstinspires.ftc.teamcode.Hardware.BaseRRHardware;
import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous( name = "TicksPerINCH")
public class TicksPerINCH extends LinearOpMode {

    BaseRRHardware robot = new RoverRuckusBotHardware();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        robot.resetEncoderCounts();

        robot.setTargetPosition(1000);

        waitForStart();

        robot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.goStraight(1);

        while(robot.isLeftBusy());
    }
}
