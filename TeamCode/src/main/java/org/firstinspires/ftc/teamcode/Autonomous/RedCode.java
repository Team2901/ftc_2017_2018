package org.firstinspires.ftc.teamcode.Autonomous;

        import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

/**
 * Created by gallagherb20503 on 11/7/2017.
 */

@Autonomous (name = "RedCode")
public class RedCode extends BaseCode {

    public void runOpMode() {
        teamColor = "red";
        super.runOpMode();

    }
}