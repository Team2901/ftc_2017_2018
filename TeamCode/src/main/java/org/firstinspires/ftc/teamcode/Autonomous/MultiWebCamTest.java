package org.firstinspires.ftc.teamcode.Autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcontroller.internal.MotoLinearOpMode;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;

@Disabled
@Autonomous(name = "MultiWebCamTest")
public class MultiWebCamTest extends MotoLinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        WebcamName webcam = hardwareMap.get(WebcamName.class, "webcam");


        VuforiaLocalizer.Parameters parameters1 = VuforiaUtilities.getWebCameraParameters(hardwareMap, webcam);
        VuforiaLocalizer.Parameters parameters2 = VuforiaUtilities.getBackCameraParameters(hardwareMap);

        VuforiaLocalizer vuforia = VuforiaUtilities.getVuforia(parameters1);
        vuforia.getCamera().close();

        waitForStart();

        VuforiaUtilities.getVuforia(parameters2);
    }
}
