package org.firstinspires.ftc.teamcode.Autonomous;

import android.graphics.Bitmap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.Hardware.RoverRuckusBotHardware;
import org.firstinspires.ftc.teamcode.Utility.RelicRecoveryUtilities;
import org.firstinspires.ftc.teamcode.Utility.RoverRuckusUtilities;
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;

@Autonomous(name = "RoverRuckusAutonomous")
public class RoverRuckusAutonomous extends LinearOpMode {
    RoverRuckusBotHardware robot = new RoverRuckusBotHardware();
    VuforiaLocalizer vuforia;
    WebcamName webcam;
    VuforiaTrackables trackables;
    public static final int TARGET_POSITION = 1;
    public String initialPosition = "depot";
    String jewelConfigLeft  = "jewelConfigLeft.txt";
    String jewelConfigMiddle = "jewelConfigMiddle.txt";
    String jewelConfigRight =  "jewelConfigRight.txt";
    String jewelBitmap = "jewelBitmap.png";
    String jewelBitmapLeft = "jewelBitmapLeft.png";
    String jewelBitmapMiddle = "jewelBitmapMiddle.png";
    String jewelBitmapRight = "jewelBitmapRight.png";
    enum GoldPosition {

        LEFT, MIDDLE, RIGHT;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        //step -2: initialize hardware
        robot.init(hardwareMap);
        // step -1: initialize vuforia
        initVuforia();
        //step 0: locate cheddar
        locateGoldJewel();
        //step 1: drop down from lander
        dropFromLander();
        //step 2: do vuforia to determine position
       GoldPosition position =  determinePosition();
        //step 3: go to the cheddar pivot point
        goToPivotPoint();
        //step 4:turn to face depot point
        faceDepotPoint();
        //step 5: gooooo
        goToDepot();
        //step 7: do corner action(if depot: drop team marker/if crater: park)
        doCornerAction();
    }

    public void initVuforia() {
        webcam = hardwareMap.get(WebcamName.class, "webcam");
        VuforiaLocalizer.Parameters parameters = VuforiaUtilities.getWebcamParameters(hardwareMap, webcam);
        vuforia = VuforiaUtilities.getVuforia(parameters);
        trackables = VuforiaUtilities.setUpTrackables( vuforia , parameters);

    }

    public void locateGoldJewel() {
        //TODO Brigitte
    }

    public void dropFromLander() {
        DcMotor.RunMode originalValue = robot.lift.getMode();
        robot.lift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.lift.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robot.lift.setTargetPosition(TARGET_POSITION);
        robot.lift.setPower(1);
        while (robot.lift.isBusy()) {
            idle();
        }
        robot.lift.setMode(originalValue);
    }

    public GoldPosition determinePosition() {

        Bitmap bitmap =  RelicRecoveryUtilities.getVuforiaImage(vuforia);
        try {
            RelicRecoveryUtilities.saveBitmap(jewelBitmap, bitmap);

            RelicRecoveryUtilities.saveHueFile("jewelHuesBig.txt", bitmap);

            int leftHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigLeft, jewelBitmapLeft, "jewelHuesLeft.txt");
            int middleHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigMiddle, jewelBitmapMiddle, "jewelHuesMiddle.txt");
            int rightHueTotal = RoverRuckusUtilities.getJewelHueCount(bitmap, jewelConfigRight, jewelBitmapRight, "jewelHuesRight.txt");

            RelicRecoveryUtilities.totalYellowHues(leftHueTotal, middleHueTotal, rightHueTotal);
            if(leftHueTotal > middleHueTotal && middleHueTotal > rightHueTotal){
                return GoldPosition.LEFT;
            } else if(rightHueTotal > middleHueTotal && rightHueTotal > leftHueTotal){
                return GoldPosition.RIGHT;
            } else{
                return GoldPosition.MIDDLE;
            }
        } catch (Exception e) {
            telemetry.addData("ERROR WRITING TO FILE JEWEL BITMAP", e.getMessage());
            telemetry.update();
            return GoldPosition.MIDDLE;
        }

    }

    public void goToPivotPoint() {
        //TODO Gillian
    }

    public void faceDepotPoint() {
        //TODO Gillian
    }

    public void goToDepot() {
        //TODO Gillian
    }

    public void doCornerAction() {
        if (initialPosition.equals("depot")) {
            robot.marker.setPosition(1);
        } else if(initialPosition.equals("crater")){
            ;
        } else{
            robot.left.setPower(1);
            final double currentTime = time;
            double targetTime = currentTime + 5;
            while(targetTime < time){
                idle();
            }
            robot.left.setPower(0);
        }
    }
}
