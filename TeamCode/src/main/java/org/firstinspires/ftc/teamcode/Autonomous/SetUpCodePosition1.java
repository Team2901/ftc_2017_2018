package org.firstinspires.ftc.teamcode.Autonomous;

import android.annotation.SuppressLint;
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
@SuppressLint("DefaultLocale")
@Autonomous(name = "SetUpCodePosition1")
public class SetUpCodePosition1 extends LinearOpModeJewelCamera {
        VuforiaLocalizer vuforia;
        File sd = Environment.getExternalStorageDirectory();
        File jewelConfigLeft  = new File(sd + "/team", "jewelConfig.Left.txt");
        File jewelConfigMiddle = new File ( sd + "/team", "jewelConfig.Middle.txt");
        File jewelConfigRight = new File ( sd + "/team", "jewelConfig.Right.txt");




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
            Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);

            waitForStart();

            saveConfigFile();

        }

        public void saveConfigFile() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(jewelConfigLeft))) {
                JewelFinder jewelLeft = jewelLeft();

                writer.write(String.format("%03d", jewelLeft.getBoxLeftXPct()), 0, 3);
                writer.newLine();
                writer.write(String.format("%03d", jewelLeft.getBoxTopYPct()), 0, 3);
                writer.newLine();
                writer.write(String.format("%03d", jewelLeft.getBoxRightXPct()), 0, 3);
                writer.newLine();
                writer.write(String.format("%03d", jewelLeft.getBoxBotYPct()), 0, 3);
                writer.newLine();
            } catch (Exception e) {
                telemetry.addData("ERROR WRITING TO FILE JEWEL LEFT", e.getMessage());
                telemetry.update();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(jewelConfigMiddle))) {
                JewelFinder jewelMiddle = jewelMiddle();

                writer.write(String.format("%03d", jewelMiddle.getBoxLeftXPct()), 0, 3);
                writer.newLine();
                writer.write(String.format("%03d", jewelMiddle.getBoxTopYPct()), 0, 3);
                writer.newLine();
                writer.write(String.format("%03d", jewelMiddle.getBoxRightXPct()), 0, 3);
                writer.newLine();
                writer.write(String.format("%03d", jewelMiddle.getBoxBotYPct()), 0, 3);
                writer.newLine();
            } catch (Exception e) {
                telemetry.addData("ERROR WRITING TO FILE JEWEL MIDDLE", e.getMessage());
                telemetry.update();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(jewelConfigRight))) {
                JewelFinder jewelRight = jewelRight();

                writer.write(String.format("%03d", jewelRight.getBoxLeftXPct()), 0, 3);
                writer.newLine();
                writer.write(String.format("%03d", jewelRight.getBoxTopYPct()), 0, 3);
                writer.newLine();
                writer.write(String.format("%03d", jewelRight.getBoxRightXPct()), 0, 3);
                writer.newLine();
                writer.write(String.format("%03d", jewelRight.getBoxBotYPct()), 0, 3);
                writer.newLine();
            } catch (Exception e) {
                telemetry.addData("ERROR WRITING TO FILE JEWEL RIGHT", e.getMessage());
                telemetry.update();
            }
        }
    }


