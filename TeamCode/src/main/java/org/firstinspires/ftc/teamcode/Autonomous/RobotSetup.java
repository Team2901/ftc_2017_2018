package org.firstinspires.ftc.teamcode.Autonomous;

import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcontroller.internal.LinearOpModeCamera;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by gallagherb20503 on 1/10/2018.
 */
@Autonomous(name = "RobotSetup")
public class RobotSetup extends LinearOpModeCamera{
    VuforiaLocalizer vuforia;
    File sd = Environment.getExternalStorageDirectory();
    File sampleBox = new File(sd + "/Team", "sampleBox.txt");



/*
add jewel finder to screen
what till start is pressed
after start jewel finder position is saved to finder
 */

@Override
    public void runOpMode() throws InterruptedException {
    ((FtcRobotControllerActivity) hardwareMap.appContext).addJewelFinder(this);


    int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
    VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
    parameters.vuforiaLicenseKey = "AW/DxXD/////AAAAGYJtW/yP3kG0pVGawWtQZngsJNFQ8kp1Md8CaP2NP72Q0on4mGKPLt/lsSnMnUkCFNymrXXOjs0eHMDTvijWRIixEe/sJ4KHEVf1fhf0kqUB29+dZEvh4qeI7tlTU6pIy/MLW0a/t9cpqMksBRFqXIrhtR/vw7ZnErMTZrJNNXqmbecBnRhDfLncklzgH2wAkGmQDn0JSP7scEczgrggcmerXy3v6flLDh1/Tt2QZ8l/bTcEJtthE82i8/8p0NuDDhUyatFK1sZSSebykRz5A4PDUkw+jMTV28iUytrr1QLiQBwaTX7ikl71a1XkBHacnxrqyY07x9QfabtJf/PYNFiU17m/l9DB6Io7DPnnIaFP";
    parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
    this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
    vuforia.setFrameQueueCapacity(1);
    Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565,true);
    VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
    VuforiaTrackable relicTemplate = relicTrackables.get(0);
    relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary


    waitForStart();



}

public void saveConfigFile(){
    try (BufferedWriter writer = new BufferedWriter (new FileWriter(sampleBox))){
        writer. write(String.format("%03d", jewel.sampleLeftXPct), 0, 3);
        writer.newLine ();
        writer. write(String.format("%03d", jewel.sampleTopYPct), 0, 3);
        writer.newLine ();
        writer. write(String.format("%03d", jewel.sampleRightXPct), 0, 3);
        writer.newLine ();
        writer. write(String.format("%03d", jewel.sampleBotYPct), 0, 3);
        writer.newLine ();
    } catch (Exception e){
        telemetry.addData ("ERROR WRITING TO FILE", e.getMessage());
    }
}
}
