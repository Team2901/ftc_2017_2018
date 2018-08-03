package org.firstinspires.ftc.teamcode.Autonomous;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Environment;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by piscullin18641 on 10/23/2017.
 */


@Autonomous(name = "RobotSetUP")
public class RobotSetUp extends BaseCombinedAutonomous {

    VuforiaLocalizer vuforia;

    private ElapsedTime runtime = new ElapsedTime();

    String teamColor = "red";
    int teamPosition = 1;

    File sd = Environment.getExternalStorageDirectory();
    File sampleBox = new File(sd + "/team", "sampleBox.txt");

    @Override
    public void runOpMode() throws InterruptedException {



        if (!isCameraAvailable()) {
            telemetry.addData("camera is not available", "");
            telemetry.update();
            waitForStart();
        }
        startCamera();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }

        telemetry.addData(String.valueOf(width), height);
        telemetry.update();

        //**********************************************************************************************

        int[] boxCords = readConfigurationFile();
        while (jewel == null)
            sleep(1);
        this.jewel.moveBox(boxCords[0], boxCords[1]);
        this.jewel.sampleLeftXPct = boxCords[0];
        this.jewel.sampleTopYPct = boxCords[1];
        this.jewel.sampleRightXPct = boxCords[2];
        this.jewel.sampleBotYPct = boxCords[3];

        telemetry.addData("", "x position = %f", jewel.getX());
        telemetry.addData("", "y position = %f", jewel.getY());

        telemetry.addData("", "x-cords = %d", ((boxCords[0]) / 100 * jewel.pWidth));
        telemetry.addData("", "y-cords = %d", ((boxCords[1]) / 100 * jewel.pHeight));

        telemetry.update();

        waitForStart();

        takePicture();
        saveConfigFile();
        createBoxBitmap();
        readConfigurationFile();

        stopCamera();

        telemetry.update();
        //&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&


        OpenGLMatrix lastLocation = null;


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parametersv = new VuforiaLocalizer.Parameters(cameraMonitorViewId);


        parametersv.vuforiaLicenseKey = "AW/DxXD/////AAAAGYJtW/yP3kG0pVGawWtQZngsJNFQ8kp1Md8CaP2NP72Q0on4mGKPLt/lsSnMnUkCFNymrXXOjs0eHMDTvijWRIixEe/sJ4KHEVf1fhf0kqUB29+dZEvh4qeI7tlTU6pIy/MLW0a/t9cpqMksBRFqXIrhtR/vw7ZnErMTZrJNNXqmbecBnRhDfLncklzgH2wAkGmQDn0JSP7scEczgrggcmerXy3v6flLDh1/Tt2QZ8l/bTcEJtthE82i8/8p0NuDDhUyatFK1sZSSebykRz5A4PDUkw+jMTV28iUytrr1QLiQBwaTX7ikl71a1XkBHacnxrqyY07x9QfabtJf/PYNFiU17m/l9DB6Io7DPnnIaFP";


        parametersv.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parametersv);


        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        relicTrackables.activate();


        int mark = 0;

// This can be used to identify the pictograph and this loop will run until it is found and it'll store the mark

        while (opModeIsActive()) {
            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                if (vuMark == RelicRecoveryVuMark.RIGHT) {

                    telemetry.addData("RIGHT", "");
                } else if (vuMark == RelicRecoveryVuMark.CENTER) {
                    telemetry.addData("CENTER", "");

                } else if (vuMark == RelicRecoveryVuMark.LEFT) {
                    telemetry.addData("Left", "");

                }

                telemetry.update();
            }
        }
    }


    public void takePicture() {

        String filePath = "Pictures";
        String imageName = "RobotSetUp.JPEG";
        Bitmap rgbImage = convertYuvImageToRgb(yuvImage, width, height, 0);

        savePic(imageName, rgbImage);
    }

    public void createBoxBitmap() {
        Bitmap rgbImage = convertYuvImageToRgb(yuvImage, width, height, 0);
        drawSamplingBox(rgbImage);
    }

    public void drawSamplingBox(Bitmap bitmap) {
        double xPercent = (bitmap.getWidth()) / 100.0;
        double yPercent = (bitmap.getHeight()) / 100.0;
        Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Canvas c = new Canvas(mutableBitmap);
        Paint p = new Paint();
        p.setARGB(100, 0, 200, 0);
        c.drawRect((int) (jewel.sampleLeftXPct * xPercent),
                (int) (jewel.sampleTopYPct * yPercent),
                (int) (jewel.sampleRightXPct * xPercent),
                (int) (jewel.sampleBotYPct * yPercent), p);
        savePic("RobotSetUpPreviewImage.png", mutableBitmap);
        //saveFile();
    }

    public void savePic(String filename, Bitmap bitmap) {
        File sd = Environment.getExternalStorageDirectory();
        File image = new File(sd + "/" + "Pictures", filename);
        try

        {
            OutputStream outStream = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outStream);
            outStream.flush();
            outStream.close();
        } catch (Exception e)

        {
            telemetry.addData("NEED TO FIX", e.getMessage());
        }
    }

    public void saveConfigFile() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(sampleBox))) {

            writer.write(teamColor);
            writer.newLine();
            writer.write(String.format("%03d", teamPosition));
            writer.newLine();
            writer.write(String.format("%03d", jewel.sampleLeftXPct), 0, 3);
            writer.newLine();
            writer.write(String.format("%03d", jewel.sampleTopYPct), 0, 3);
            writer.newLine();
            writer.write(String.format("%03d", jewel.sampleRightXPct), 0, 3);
            writer.newLine();
            writer.write(String.format("%03d", jewel.sampleBotYPct), 0, 3);
            writer.newLine();


        } catch (IOException yee) {
            telemetry.addData("ERROR WRITING TO FILE", yee.getMessage());
        }
    }

    public int[] readConfigurationFile() {

        int sampleBox_x1 = 0;
        int sampleBox_y1 = 0;
        int sampleBox_x2 = 0;
        int sampleBox_y2 = 0;

        String text = null;

        try (BufferedReader reader = new BufferedReader(new FileReader(sampleBox))) {
            text = reader.readLine();
            teamColor = text;
            text = reader.readLine();
            teamPosition = Integer.parseInt(text);
            text = reader.readLine();
            sampleBox_x1 = Integer.parseInt(text);
            text = reader.readLine();
            sampleBox_y1 = Integer.parseInt(text);
            text = reader.readLine();
            sampleBox_x2 = Integer.parseInt(text);
            text = reader.readLine();
            sampleBox_y2 = Integer.parseInt(text);
        } catch (IOException e) {
            e.printStackTrace();
            telemetry.addData("", "couldn't read");

            int[] defaultCords = {50, 50, 70, 70};
            return defaultCords;
        }
        telemetry.addData("", "start x: %d", sampleBox_x1);
        telemetry.addData("", "start y: %d", sampleBox_y1);
        telemetry.addData("", "end x: %d", sampleBox_x2);
        telemetry.addData("", "end y: %d", sampleBox_y2);

        int[] boxCords = {sampleBox_x1, sampleBox_y1, sampleBox_x2, sampleBox_y2};

        return boxCords;
    }

}