package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "RoverRuckusAutonomous Red Crater", group = "Red")
public class RoverRuckusAutonomousRedCrater extends BaseRoverRuckusAuto {

    public RoverRuckusAutonomousRedCrater() {
        super();
        startCorner = StartCorner.RED_CRATER;
        xStart = -18;
        yStart = 18;
        angleStart = 135;
    }
}
