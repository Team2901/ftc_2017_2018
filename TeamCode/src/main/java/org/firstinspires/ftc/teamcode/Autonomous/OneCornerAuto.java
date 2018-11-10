package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;
import org.firstinspires.ftc.teamcode.Presentation.PresentationBotHardware;
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;

import java.util.ArrayList;


@Autonomous (name = "OneCornerAuto")
public class OneCornerAuto extends LinearOpMode {

    RoverRuckusBotHardware robot = new RoverRuckusBotHardware();

    WebcamName webcam;
    VuforiaLocalizer vuforia;

    VuforiaTrackable blue;
    VuforiaTrackable red;
    VuforiaTrackable front;
    VuforiaTrackable back;


    @Override
    public void runOpMode() {


        webcam = hardwareMap.get(WebcamName.class, "webcam");
        VuforiaLocalizer.Parameters parameters = VuforiaUtilities.getWebcamParameters(hardwareMap, webcam);
        vuforia = VuforiaUtilities.getVuforia(parameters);



        VuforiaTrackables roverRuckus =  VuforiaUtilities.setUpTrackables( vuforia , parameters);
        blue = roverRuckus.get(0);
        red = roverRuckus.get(1);
        front = roverRuckus.get(2);
        back = roverRuckus.get(3);
    }
}
