package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import static org.firstinspires.ftc.teamcode.Autonomous.BaseRoverRuckusAuto.StartCorner.BLUE_DEPOT;

@Autonomous ( name = "AutoTest", group = "Blue")
public class AutoTest extends BaseRoverRuckusAuto {

    public AutoTest() {
        super(BLUE_DEPOT);
            }
    @Override
    public void runOpMode()  {
        robot.init(hardwareMap);
        waitForStart();
      goToDistance(120,.75, "test");
        while(opModeIsActive()){idle();}
    }
}