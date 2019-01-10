package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "RoverRuckusAutonomous Red Depot", group = "Red")
public class RoverRuckusAutonomousRedDepot extends BaseRoverRuckusAuto {

    public RoverRuckusAutonomousRedDepot() {
        super();
        startCorner = StartCorner.RED_DEPOT;
        xStart = -12;
        yStart = -12;
        angleStart = -135;
    }
}
