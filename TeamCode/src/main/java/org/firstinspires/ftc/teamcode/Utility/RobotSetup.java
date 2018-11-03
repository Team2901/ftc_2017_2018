package org.firstinspires.ftc.teamcode.Utility;

import android.os.Environment;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcontroller.internal.JewelFinder;
import org.firstinspires.ftc.robotcontroller.internal.LinearOpModeJewelCamera;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by gallagherb20503 on 1/10/2018.
 */
@Autonomous(name = "RobotSetup2")
public class RobotSetup extends LinearOpModeJewelCamera {
    VuforiaLocalizer vuforia;
    File sd = Environment.getExternalStorageDirectory();
    File sampleBox = new File(sd + "/team", "sampleBox.txt");
    ModernRoboticsI2cRangeSensor rangeSensor;



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
    parameters.vuforiaLicenseKey = "AQQpWjP/////AAABmWf3iVzlb0FUp3bUlTfyu04cg6nObJiyAcRVvdXnI9UGwJLT8PeUmQnawxjoZEpxQX4SACGC67Ix1pI2PTCBBrPOug9cDMLwL3g2TKSlKCfpMru3ooxbXaZ9ulWIc0rzWGCzLfmYN1mijxVwJPELqB2klhfU4FJMNGAZsHbkUJQqtCYhd5+psmXGukR9DUVFPFlAk/SJrpyCuLPZYgcqlOgqhvHH4PCFQqwHFpTKqnF/cgsNbrhiEpGhh6eWq2vvY+pP+/E8BxzM65XzIgKgUj2Uce6nYsD4oCTOpsmLywPxTExDflqSYtkfC+rLL8j601v3TsFI26x/UlE+YZg1UQkQo/eJI5aTEDL6ypVAmuZe";
    parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
    this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
    vuforia.setFrameQueueCapacity(1);
    Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565,true);
    VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
    VuforiaTrackable relicTemplate = relicTrackables.get(0);
    relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

    rangeSensor = hardwareMap.get(ModernRoboticsI2cRangeSensor.class, "rangeSensor");

    waitForStart();

    while (opModeIsActive()) {
        telemetry.addData("cm", "%.2f cm", rangeSensor.getDistance(DistanceUnit.CM));
        telemetry.addData("inches", "%.2f inches", rangeSensor.getDistance(DistanceUnit.INCH));
        telemetry.addData("target", "34.00cm");
        telemetry.update();
    }
    saveConfigFile();

}

public void saveConfigFile(){
    try (BufferedWriter writer = new BufferedWriter (new FileWriter(sampleBox))){
        JewelFinder jewel = getJewel();

        writer. write(String.format("%03d", jewel.getBoxLeftXPct()), 0, 3);
        writer.newLine ();
        writer. write(String.format("%03d", jewel.getBoxTopYPct()), 0, 3);
        writer.newLine ();
        writer. write(String.format("%03d", jewel.getBoxRightXPct()), 0, 3);
        writer.newLine ();
        writer. write(String.format("%03d", jewel.getBoxBotYPct()), 0, 3);
        writer.newLine ();
    } catch (Exception e){
        telemetry.addData ("ERROR WRITING TO FILE", e.getMessage());
    }
}
}
