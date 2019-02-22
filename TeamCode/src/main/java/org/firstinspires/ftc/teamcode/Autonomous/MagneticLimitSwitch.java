package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DigitalChannel;

@Disabled
@Autonomous (name = "MagneticLimitSwitch")
public class MagneticLimitSwitch extends OpMode {

    DigitalChannel magneticLimitSwitch;

    public void init() {
        magneticLimitSwitch = hardwareMap.get(DigitalChannel.class, "magneticLimitSwitch");

        magneticLimitSwitch.setMode(DigitalChannel.Mode.INPUT);
    }

    public void loop() {

        telemetry.addData("State" , magneticLimitSwitch.getState());
        telemetry.update();
    }
}
