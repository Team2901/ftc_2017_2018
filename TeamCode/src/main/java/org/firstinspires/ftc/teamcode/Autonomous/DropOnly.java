package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.BLUE_DEPOT;

@Autonomous ( name = "Drop Only", group = "RoverRuckusPartial")
public class DropOnly extends BaseRoverRuckusAuto {

    public DropOnly() {
        super(BLUE_DEPOT,
                DEFAULT_GOLD_POSITION,
                true,
                false,
                false);
            }
    @Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();

        while(opModeIsActive()){
            idle();
        }
    }
}