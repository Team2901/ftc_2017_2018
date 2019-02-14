package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.BLUE_DEPOT;

@Autonomous ( name = "Drop Only", group = "Blue")
public class DropOnly extends BaseRoverRuckusAuto {

    public DropOnly() {
        super(BLUE_DEPOT);
            }
    @Override
    public void runOpMode()  {
        robot.init(hardwareMap);
        waitForStart();
        dropFromLander();
        while(opModeIsActive()){idle();}
    }
}