package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "RoverRuckusAutonomous Blue Crater", group = "Blue")
public class RoverRuckusAutonomousBlueCrater extends BaseRoverRuckusAuto {

    public RoverRuckusAutonomousBlueCrater() {
        super();
        startPosition = StartPosition.BLUE_CRATER;
        xStart = 12;
        yStart = -12;
        angleStart  = -45;

    }
}
