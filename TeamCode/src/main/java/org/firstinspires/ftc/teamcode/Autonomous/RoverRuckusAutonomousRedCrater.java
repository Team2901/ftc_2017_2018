package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "RoverRuckusAutonomous Red Crater", group = "Red")
public class RoverRuckusAutonomousRedCrater extends BaseRoverRuckusAuto {

    public RoverRuckusAutonomousRedCrater() {
        super();
        initialPosition = StartPosition.RED_CRATER;
        xStart = -12;
        yStart = 12;
        angleStart = 135;
    }
}
