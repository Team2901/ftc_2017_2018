package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.RED_DEPOT;

@Autonomous(name = "RoverRuckusAutonomous Red Depot", group = "Red")
public class RoverRuckusAutonomousRedDepot extends BaseRoverRuckusAuto {

    public RoverRuckusAutonomousRedDepot() {
        super(RED_DEPOT);
    }
}
