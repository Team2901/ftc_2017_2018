package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by gallagherb20503 on 11/7/2017.
 */

@Autonomous (name = "BlueCode")
public class BlueCode extends BaseCode {

    public void runOpMode() {
        teamColor = "blue";
        super.runOpMode();

    }
}