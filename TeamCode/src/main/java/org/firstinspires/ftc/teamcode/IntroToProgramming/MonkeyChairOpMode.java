package org.firstinspires.ftc.teamcode.IntroToProgramming;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name="MonkeyChairOpMode")
public class MonkeyChairOpMode extends LinearOpMode{
 public SonarRobotThing sonarRobotThing=new SonarRobotThing();
    @Override
    public void runOpMode() throws InterruptedException {
        sonarRobotThing.init(hardwareMap);
        sonarRobotThing.leftMotor.setPower(.5);
        sonarRobotThing.rightMotor.setPower(.5);

    }
}
