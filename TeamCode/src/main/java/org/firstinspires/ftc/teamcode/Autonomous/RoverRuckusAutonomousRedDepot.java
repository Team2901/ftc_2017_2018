package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "RoverRuckusAutonomous Red Depot", group = "Red")
public class RoverRuckusAutonomousRedDepot extends BaseRoverRuckusAuto {

    public RoverRuckusAutonomousRedDepot() {
        super();
        initialPosition = StartPosition.RED_DEPOT;
    }
}
