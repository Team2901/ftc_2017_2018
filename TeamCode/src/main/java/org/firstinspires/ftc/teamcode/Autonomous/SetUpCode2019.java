package org.firstinspires.ftc.teamcode.Autonomous;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Environment;

//import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcontroller.internal.JewelFinder;
import org.firstinspires.ftc.robotcontroller.internal.LinearOpModeJewelCamera;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Utility.RelicRecoveryUtilities;
import org.firstinspires.ftc.teamcode.Utility.RoverRuckusUtilities;
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@SuppressLint("DefaultLocale")
@Autonomous(name = "SetUpCode2019")
public class SetUpCode2019 extends LinearOpModeJewelCamera {
    VuforiaLocalizer vuforia;
    File sd = Environment.getExternalStorageDirectory();
    String jewelConfigLeft  = "jewelConfigLeft.txt";
    String jewelConfigMiddle = "jewelConfigMiddle.txt";
    String jewelConfigRight =  "jewelConfigRight.txt";
    String jewelBitmap = "jewelBitmap.png";
    String jewelBitmapLeft = "jewelBitmapLeft.png";
    String jewelBitmapMiddle = "jewelBitmapMiddle.png";
    String jewelBitmapRight = "jewelBitmapRight.png";

    //left
    //middle
    //right

    /*
    add jewel finder to screen
    what till start is pressed
    after start jewel finder position is saved to finder
     */

    @Override
    public void runOpMode() throws InterruptedException {
        final FtcRobotControllerActivity activity =(FtcRobotControllerActivity )hardwareMap.appContext;


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = VuforiaUtilities.getBackCameraParameters(hardwareMap);
        vuforia = VuforiaUtilities.getVuforia(parameters);
        activity.setupPreviewLayout(cameraMonitorViewId);
        activity.removeJewelFinder(this);
        activity.addJewelFinder(this);

        waitForStart();

        saveConfigFile();
        Bitmap bitmap =  RelicRecoveryUtilities.getVuforiaImage(vuforia);
        try {
            RelicRecoveryUtilities.saveBitmap(jewelBitmap, bitmap);

            RelicRecoveryUtilities.saveHueFile("jewelHuesBig.txt", bitmap);

            int leftHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigLeft,
                    jewelBitmapLeft, "jewelHuesLeft.txt");
            int middleHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigMiddle,
                    jewelBitmapMiddle, "jewelHuesMiddle.txt");
            int rightHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigRight,
                    jewelBitmapRight, "jewelHuesRight.txt");


            RelicRecoveryUtilities.totalYellowHues(leftHueTotal, middleHueTotal, rightHueTotal);
            RelicRecoveryUtilities.winnerFrom2Bmaps(middleHueTotal, rightHueTotal);



        } catch (Exception e) {
            telemetry.addData("ERROR WRITING TO FILE JEWEL BITMAP", e.getMessage());
            telemetry.update();
        }
        activity .removeJewelFinder(this);


    }


    public void saveConfigFile() {

        try {
            JewelFinder jewelLeft = jewelLeft();
            RelicRecoveryUtilities.writeConfigFile(jewelConfigLeft,jewelLeft.getBoxPct() );

        } catch (Exception e) {
            telemetry.addData("ERROR WRITING TO FILE JEWEL LEFT", e.getMessage());
            telemetry.update();
        }

        try {
            JewelFinder jewelMiddle = jewelMiddle();
            RelicRecoveryUtilities.writeConfigFile(jewelConfigMiddle,jewelMiddle.getBoxPct() );

        } catch (Exception e) {
            telemetry.addData("ERROR WRITING TO FILE JEWEL MIDDLE", e.getMessage());
            telemetry.update();
        }

        try  {
            JewelFinder jewelRight = jewelRight();
            RelicRecoveryUtilities.writeConfigFile(jewelConfigRight, jewelRight.getBoxPct());

        } catch (Exception e) {
            telemetry.addData("ERROR WRITING TO FILE JEWEL RIGHT", e.getMessage());
            telemetry.update();
        }
    }



}


