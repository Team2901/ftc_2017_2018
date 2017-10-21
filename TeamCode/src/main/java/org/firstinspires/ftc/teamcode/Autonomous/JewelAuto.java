package org.firstinspires.ftc.teamcode.Autonomous;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcontroller.internal.LinearOpModeCamera;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by gallagherb20503 on 10/21/2017.
 */

@Autonomous
public class JewelAuto extends LinearOpModeCamera{
   String filePath= "Pictures";
    String imageName = "TestImage.JPEG"; //TODO come back//
    @Override
    public void runOpMode() throws InterruptedException {
        if (isCameraAvailable()) {
           startCamera();
            telemetry.addData(String.valueOf(width), height);
            telemetry.update();
            waitForStart();
           Bitmap rgbImage= convertYuvImageToRgb(yuvImage, width, height, 0);
            stopCamera();
            File sd = Environment.getExternalStorageDirectory();
            File image = new File(sd+"/"+filePath, imageName);
            try {
                OutputStream outStream = new FileOutputStream(image);
                //rgbImage.compress(Bitmap.CompressFormat.JPEG, 0, outStream);
                yuvImage.compressToJpeg(new Rect(0, 0, width, height), 0, outStream);
                outStream.flush();
                outStream.close();
            } catch (Exception e){
                telemetry.addData("NEED TO FIX", e.getMessage());
            }
        }

    }
}
