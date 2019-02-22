package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.RED_DEPOT;

@Disabled
@Autonomous(name = "RoverRuckus Red Depot", group = "RoverRuckus")
public class RoverRuckusAutonomousRedDepot extends BaseRoverRuckusAuto {

    public RoverRuckusAutonomousRedDepot() {
        super(RED_DEPOT);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        runOpModeDepotCorner();

        while(opModeIsActive()) {
            idle();
        }
    }
}
