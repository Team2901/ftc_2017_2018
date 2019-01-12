package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.BLUE_DEPOT;

@Autonomous ( name = "RoverRuckusAutonomous Blue Depot", group = "Blue")
public class RoverRuckusAutonomousBlueDepot extends BaseRoverRuckusAuto {

    public RoverRuckusAutonomousBlueDepot() {
        super();
        startCorner = BLUE_DEPOT;
        xStart = 12;
        yStart = 12;
        angleStart = 45;
    }
}
