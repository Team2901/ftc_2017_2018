package org.firstinspires.ftc.teamcode.Utility;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;



public class VuforiaUtilities {


    public static final double MM_TO_INCHES = 0.0393701;
    public static final double INCHES_TO_MM = 25.4;
    public static final double FIELD_RADIUS = 1828.8;
    //for never rest 40s
    public static final double ENCODER_COUNTS_PER_REV = 1120;
    //measured may not be exact
    public static final double INCHES_PER_ROTATION = 7.75;
    public static final double INCHES_TO_ENCODERCOUNTS = ((1 / INCHES_PER_ROTATION) * ENCODER_COUNTS_PER_REV);


    public static OpenGLMatrix phoneLocation = getMatrix(90, -0, -90,  (int)(-6*INCHES_TO_MM), (int)(1*INCHES_TO_MM),0);

    public final static String VUFORIA_KEY = "AQQpWjP/////AAABmWf3iVzlb0FUp3bUlTfyu04cg6nObJiyAcRVvd" +
            "XnI9UGwJLT8PeUmQnawxjoZEpxQX4SACGC67Ix1pI2PTCBBrPOug9cDMLwL3g2TKSlKCfpMru3ooxbXaZ9ulWIc0" +
            "rzWGCzLfmYN1mijxVwJPELqB2klhfU4FJMNGAZsHbkUJQqtCYhd5+psmXGukR9DUVFPFlAk/SJrpyCuLPZYgcql" +
            "OgqhvHH4PCFQqwHFpTKqnF/cgsNbrhiEpGhh6eWq2vvY+pP+/E8BxzM65XzIgKgUj2Uce6nYsD4oCTOpsmLywPxT" +
            "ExDflqSYtkfC+rLL8j601v3TsFI26x/UlE+YZg1UQkQo/eJI5aTEDL6ypVAmuZe";

    public static VuforiaLocalizer.Parameters getBackCameraParameters(HardwareMap hardwareMap) {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        return parameters;
    }

    public static VuforiaLocalizer.Parameters getWebcamParameters(HardwareMap hardwareMap, WebcamName webcam) {
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = webcam;
        return parameters;
    }

    public static VuforiaLocalizer getVuforia(VuforiaLocalizer.Parameters parameters) {
        VuforiaLocalizer vuforia = ClassFactory.getInstance().createVuforia(parameters);
        vuforia.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
        return vuforia;
    }

    public static VuforiaTrackables setUpTrackables(VuforiaLocalizer vuforia ,VuforiaLocalizer.Parameters parameters ) {

        VuforiaTrackables roverRuckus = vuforia.loadTrackablesFromAsset("RoverRuckus");
        VuforiaTrackable blue;
        VuforiaTrackable red;
        VuforiaTrackable front;
        VuforiaTrackable back;

        blue = roverRuckus.get(0);
        red = roverRuckus.get(1);
        front = roverRuckus.get(2);
        back = roverRuckus.get(3);

        blue.setName("blue");
        red.setName("red");
        front.setName("front");
        back.setName("back");


        OpenGLMatrix blueTrackablePosition = getMatrix(90, 0, -90, (float) FIELD_RADIUS, 0, (float) 152.4);
        OpenGLMatrix frontTrackablePosition = getMatrix(90, 0, 0, 0, (float) FIELD_RADIUS, (float) 152.4);
        OpenGLMatrix redTrackablePosition = getMatrix(90, 0, 90, (float) -FIELD_RADIUS, 0, (float) 152.4);
        OpenGLMatrix backTrackablePosition = getMatrix(90, 0, 180, 0, (float) -FIELD_RADIUS, (float) 152.4);


        blue.setLocation(blueTrackablePosition);
        red.setLocation(redTrackablePosition);
        front.setLocation(frontTrackablePosition);
        back.setLocation(backTrackablePosition);

        ((VuforiaTrackableDefaultListener) blue.getListener()).setPhoneInformation(phoneLocation, parameters.cameraDirection);
        ((VuforiaTrackableDefaultListener) red.getListener()).setPhoneInformation(phoneLocation, parameters.cameraDirection);
        ((VuforiaTrackableDefaultListener) front.getListener()).setPhoneInformation(phoneLocation, parameters.cameraDirection);
        ((VuforiaTrackableDefaultListener) back.getListener()).setPhoneInformation(phoneLocation, parameters.cameraDirection);



        return roverRuckus;
    }

    public static OpenGLMatrix getMatrix(float ax, float ay, float az, float dx, float dy, float dz) {

        return OpenGLMatrix.translation(dx, dy, dz).multiplied
                (Orientation.getRotationMatrix(AxesReference.EXTRINSIC,
                        AxesOrder.XYZ, AngleUnit.DEGREES, ax, ay, az));
    }

    public static OpenGLMatrix getLocation(VuforiaTrackable blue, VuforiaTrackable red,
                                           VuforiaTrackable front, VuforiaTrackable back) {
        OpenGLMatrix location = null;
        OpenGLMatrix blueLocation = null;
        OpenGLMatrix redLocation = null;
        OpenGLMatrix backLocation = null;
        OpenGLMatrix frontLocation = null;

        while (location == null) {
            blueLocation = ((VuforiaTrackableDefaultListener)
                    blue.getListener()).getUpdatedRobotLocation();
            redLocation = ((VuforiaTrackableDefaultListener)
                    red.getListener()).getUpdatedRobotLocation();
            backLocation = ((VuforiaTrackableDefaultListener)
                    back.getListener()).getUpdatedRobotLocation();
            frontLocation = ((VuforiaTrackableDefaultListener)
                    front.getListener()).getUpdatedRobotLocation();

            if (blueLocation != null) {
                location = blueLocation;
            } else if (redLocation != null) {
                location = redLocation;
            } else if (backLocation != null) {
                location = backLocation;
            } else if (frontLocation != null) {
                location = frontLocation;
            }

        }
        return location;
    }
}