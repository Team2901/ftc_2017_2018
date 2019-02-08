package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.BLUE_CRATER;

@Autonomous(name = "RoverRuckusAutonomous Blue Crater", group = "Blue")
public class RoverRuckusAutonomousBlueCrater extends BaseRoverRuckusAuto {

    public RoverRuckusAutonomousBlueCrater() {
        super(BLUE_CRATER);
    }
}
