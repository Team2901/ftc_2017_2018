package org.firstinspires.ftc.teamcode.Utility;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;

public class VuforiaUtilities {
    public final static String VUFORIA_KEY = "AQQpWjP/////AAABmWf3iVzlb0FUp3bUlTfyu04cg6nObJiyAcRVvd" +
            "XnI9UGwJLT8PeUmQnawxjoZEpxQX4SACGC67Ix1pI2PTCBBrPOug9cDMLwL3g2TKSlKCfpMru3ooxbXaZ9ulWIc0" +
            "rzWGCzLfmYN1mijxVwJPELqB2klhfU4FJMNGAZsHbkUJQqtCYhd5+psmXGukR9DUVFPFlAk/SJrpyCuLPZYgcql" +
            "OgqhvHH4PCFQqwHFpTKqnF/cgsNbrhiEpGhh6eWq2vvY+pP+/E8BxzM65XzIgKgUj2Uce6nYsD4oCTOpsmLywPxT" +
            "ExDflqSYtkfC+rLL8j601v3TsFI26x/UlE+YZg1UQkQo/eJI5aTEDL6ypVAmuZe";

    public static VuforiaLocalizer.Parameters getBackCameraParameters(HardwareMap hardwareMap) {
        int cameraMoniterViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMoniterViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        return parameters;
    }

    public static VuforiaLocalizer.Parameters getWebcamParameters(HardwareMap hardwareMap, WebcamName webcam) {
        int cameraMoniterViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMoniterViewId);
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = webcam;
        return parameters;
    }

}

