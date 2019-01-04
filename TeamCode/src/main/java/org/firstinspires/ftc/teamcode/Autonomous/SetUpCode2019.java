package org.firstinspires.ftc.teamcode.Autonomous;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Environment;

//import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cRangeSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcontroller.internal.JewelFinder;
import org.firstinspires.ftc.robotcontroller.internal.LinearOpModeJewelCamera;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Utility.BitmapUtilities;
import org.firstinspires.ftc.teamcode.Utility.FileUtilities;
import org.firstinspires.ftc.teamcode.Utility.RoverRuckusUtilities;
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;

import java.io.File;

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
        activity.setupPreviewLayout(cameraMonitorViewId, this);

        waitForStart();

        saveConfigFile();
        Bitmap bitmap = BitmapUtilities.getVuforiaImage(vuforia);
        try {
            FileUtilities.writeBitmapFile(jewelBitmap, bitmap);

            FileUtilities.writeHueFile("jewelHuesBig.txt", bitmap);

            int[] leftHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigLeft,
                    jewelBitmapLeft, "jewelHuesLeft.txt");
            int[] middleHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigMiddle,
                    jewelBitmapMiddle, "jewelHuesMiddle.txt");
            int[] rightHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigRight,
                    jewelBitmapRight, "jewelHuesRight.txt");


            String winner = BitmapUtilities.findWinnerLocation(leftHueTotal[0], middleHueTotal[0], rightHueTotal[0]);
            FileUtilities.writeWinnerFile(winner, leftHueTotal[0], middleHueTotal[0], rightHueTotal[0]);

             winner = BitmapUtilities.findWinnerLocation(middleHueTotal[0], rightHueTotal[0]);
            FileUtilities.writeWinnerFile(winner, middleHueTotal[0], rightHueTotal[0]);




        } catch (Exception e) {
            telemetry.addData("ERROR WRITING TO FILE JEWEL BITMAP", e.getMessage());
            telemetry.update();
        }
        activity .removeJewelFinder(this);


    }


    public void saveConfigFile() {

        try {
            JewelFinder jewelLeft = jewelLeft();
            FileUtilities.writeConfigFile(jewelConfigLeft,jewelLeft.getBoxPct() );

        } catch (Exception e) {
            telemetry.addData("ERROR WRITING TO FILE JEWEL LEFT", e.getMessage());
            telemetry.update();
        }

        try {
            JewelFinder jewelMiddle = jewelMiddle();
            FileUtilities.writeConfigFile(jewelConfigMiddle,jewelMiddle.getBoxPct() );

        } catch (Exception e) {
            telemetry.addData("ERROR WRITING TO FILE JEWEL MIDDLE", e.getMessage());
            telemetry.update();
        }

        try  {
            JewelFinder jewelRight = jewelRight();
            FileUtilities.writeConfigFile(jewelConfigRight, jewelRight.getBoxPct());

        } catch (Exception e) {
            telemetry.addData("ERROR WRITING TO FILE JEWEL RIGHT", e.getMessage());
            telemetry.update();
        }
    }



}


