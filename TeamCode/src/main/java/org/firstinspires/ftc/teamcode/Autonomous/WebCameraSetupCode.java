package org.firstinspires.ftc.teamcode.Autonomous;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcontroller.internal.JewelFinder;
import org.firstinspires.ftc.robotcontroller.internal.LinearOpModeJewelCamera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Utility.BitmapUtilities;
import org.firstinspires.ftc.teamcode.Utility.FileUtilities;
import org.firstinspires.ftc.teamcode.Utility.RoverRuckusUtilities;
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;

import java.io.IOException;

@SuppressLint("DefaultLocale")
@Autonomous(name = "WebCameraSetupCode", group = "Setup Code")
public class WebCameraSetupCode extends LinearOpModeJewelCamera {
    VuforiaLocalizer vuforia;
    String jewelConfigLeft  = "jewelConfigLeft.txt";
    String jewelConfigMiddle = "jewelConfigMiddle.txt";
    String jewelConfigRight =  "jewelConfigRight.txt";
    String jewelBitmap = "jewelBitmap.png";
    String jewelBitmapLeft = "jewelBitmapLeft.png";
    String jewelBitmapMiddle = "jewelBitmapMiddle.png";
    String jewelBitmapRight = "jewelBitmapRight.png";

    BaseRoverRuckusAuto.GoldPosition winner;

    @Override
    public void runOpMode() throws InterruptedException {
        final FtcRobotControllerActivity activity = (FtcRobotControllerActivity) hardwareMap.appContext;


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        WebcamName webcam = hardwareMap.get(WebcamName.class, "webcam");
        telemetry.addData("Webcam Attached", webcam.isAttached());
        telemetry.update();

        try {
            leftConfig = FileUtilities.readIntegerConfigFile(jewelConfigLeft);
        } catch (IOException e) {
            leftConfig = null;
        }
        try {
            middleConfig = FileUtilities.readIntegerConfigFile(jewelConfigMiddle);
        } catch (IOException e) {
            middleConfig = null;
        }
        try {
            rightConfig = FileUtilities.readIntegerConfigFile(jewelConfigRight);
        } catch (IOException e) {
            rightConfig = null;
        }

        VuforiaLocalizer.Parameters parameters = VuforiaUtilities.getWebCameraParameters(hardwareMap, webcam);

        vuforia = VuforiaUtilities.getVuforia(parameters);

        telemetry.addData("Webcam Attached", webcam.isAttached());
        telemetry.addData("Vuforia has camera", vuforia.getCamera() != null);
        telemetry.update();

        activity.setupPreviewLayout(cameraMonitorViewId, this);

        waitForStart();

        saveConfigFile();

        Bitmap bitmap = BitmapUtilities.getVuforiaImage(vuforia);
        if (bitmap == null) {
            telemetry.addData("Webcam Attached", webcam.isAttached());
            telemetry.addData("Vuforia has camera", vuforia.getCamera() != null);
            telemetry.addData("Error", "Bitmap is null");
            telemetry.update();
        } else {
            try {
                FileUtilities.writeBitmapFile(jewelBitmap, bitmap);
                //FileUtilities.writeHueFile("jewelHuesBig.txt", bitmap);

                int[] leftHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigLeft,
                        jewelBitmapLeft, "jewelHuesLeft.txt", this);
                int[] middleHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigMiddle,
                        jewelBitmapMiddle, "jewelHuesMiddle.txt", this);
                int[] rightHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigRight,
                        jewelBitmapRight, "jewelHuesRight.txt", this);

                winner = BitmapUtilities.findWinnerLocation(leftHueTotal, middleHueTotal, rightHueTotal);
                FileUtilities.writeWinnerFile(winner, leftHueTotal, middleHueTotal, rightHueTotal);

                winner = BitmapUtilities.findWinnerLocation(middleHueTotal, rightHueTotal);
                FileUtilities.writeWinnerFile(winner, middleHueTotal, rightHueTotal);

            } catch(Exception e){
                telemetry.addData("Webcam Attached", webcam.isAttached());
                telemetry.addData("Vuforia has camera", vuforia.getCamera() != null);
                telemetry.addData("ERROR WRITING TO FILE JEWEL BITMAP", e.getMessage());
                telemetry.update();
            }
        }

        activity.removeJewelFinder(this);

        telemetry.addData("Webcam Attached", webcam.isAttached());
        telemetry.addData("Vuforia has camera", vuforia.getCamera() != null);
        telemetry.addData("SUCCESS! Winner=", winner);
        telemetry.update();

        while (opModeIsActive()) {
            idle();
        }
    }


    public void saveConfigFile() {

        try {
            JewelFinder jewelLeft = jewelLeft();
            FileUtilities.writeConfigFile(jewelConfigLeft,jewelLeft.getBoxPct() );

        } catch (Exception e) {
            telemetry.addData("ERROR WRITING TO CONFIG FILE JEWEL LEFT", e.getMessage());
            telemetry.update();
        }

        try {
            JewelFinder jewelMiddle = jewelMiddle();
            FileUtilities.writeConfigFile(jewelConfigMiddle,jewelMiddle.getBoxPct() );

        } catch (Exception e) {
            telemetry.addData("ERROR WRITING TO CONFIG FILE JEWEL MIDDLE", e.getMessage());
            telemetry.update();
        }

        try  {
            JewelFinder jewelRight = jewelRight();
            FileUtilities.writeConfigFile(jewelConfigRight, jewelRight.getBoxPct());

        } catch (Exception e) {
            telemetry.addData("ERROR WRITING TO CONFIG FILE JEWEL RIGHT", e.getMessage());
            telemetry.update();
        }
    }
}


