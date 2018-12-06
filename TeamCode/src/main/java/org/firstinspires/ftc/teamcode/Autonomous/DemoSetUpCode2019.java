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
@Autonomous(name = "DemoSetUpCode2019")
public class DemoSetUpCode2019 extends LinearOpModeJewelCamera {
    VuforiaLocalizer vuforia;
    File sd = Environment.getExternalStorageDirectory();
    String jewelConfigLeft  = "jewelConfigLeft.txt";
    String jewelConfigMiddle = "jewelConfigMiddle.txt";
    String jewelConfigRight =  "jewelConfigRight.txt";
    String jewelBitmap = "jewelBitmap.png";
    String jewelBitmapLeft = "jewelBitmapLeft.png";
    String jewelBitmapMiddle = "jewelBitmapMiddle.png";
    String jewelBitmapRight = "jewelBitmapRight.png";

    int leftHueTotal = 0;
    int middleHueTotal = 0;
    int rightHueTotal = 0;
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


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = VuforiaUtilities.getBackCameraParameters(hardwareMap);
        vuforia = VuforiaUtilities.getVuforia(parameters);
        activity.setupPreviewLayout(cameraMonitorViewId);
        activity.removeJewelFinder(this);
        activity.addJewelFinder(this);

        waitForStart();

        saveConfigFile();
        Bitmap bitmap =  BitmapUtilities.getVuforiaImage(vuforia);
        try {
            FileUtilities.writeBitmapFile(jewelBitmap, bitmap);

            FileUtilities.writeHueFile("jewelHuesBig.txt", bitmap);

            leftHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigLeft, jewelBitmapLeft, "jewelHuesLeft.txt");
            middleHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigMiddle, jewelBitmapMiddle, "jewelHuesMiddle.txt");
            rightHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigRight, jewelBitmapRight, "jewelHuesRight.txt");

            JewelFinder winningJewel = null;
            String winner = BitmapUtilities.findWinnerLocation(leftHueTotal, middleHueTotal, rightHueTotal);
            FileUtilities.writeWinnerFile(winner,leftHueTotal, middleHueTotal, rightHueTotal);
            if(winner.equals("L"))
            {
                //turn left box yellow
                winningJewel = this.jewelLeft();
            }
            if(winner.equals("R"))
            {
                //turn right box yellow
                winningJewel = this.jewelRight();
            }
            if(winner.equals("M"))
            {
                //turn middle box yellow
                winningJewel = this.jewelMiddle();
            }

            //turn all boxes white
            jewelLeft.post(new Runnable( ) { public void run() {
                jewelLeft.setText(Integer.toString(leftHueTotal));
                jewelLeft.setBackgroundColor(0x55ffffff);}});
            jewelMiddle.post(new Runnable( ) { public void run() {
                jewelMiddle.setText(Integer.toString(leftHueTotal));
                jewelMiddle.setBackgroundColor(0x55ffffff);}});
            jewelRight.post(new Runnable( ) { public void run() {
                jewelRight.setText(Integer.toString(leftHueTotal));
                jewelRight.setBackgroundColor(0x55ffffff);}});

            //winningJewel.post(new Runnable( ) { public void run() {jewel.setBackgroundColor(0x55ffff00);}});

        } catch (Exception e) {
            telemetry.addData("ERROR WRITING TO FILE JEWEL BITMAP", e.getMessage());
            telemetry.update();
        }
        activity.removeJewelFinder(this);

        while(opModeIsActive())
        {
            idle();
        }

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