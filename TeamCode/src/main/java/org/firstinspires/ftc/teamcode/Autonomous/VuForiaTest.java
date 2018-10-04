package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.PIXEL_FORMAT;
import com.vuforia.Vuforia;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;


@Autonomous (name = "VuForiaTest")
public class VuForiaTest extends LinearOpMode {

    VuforiaLocalizer vuforia;

    public final double MM_TO_INCHES = 0.0393701;
    public final double FIELD_RADIUS = 1828.8;


    public OpenGLMatrix phoneLocation = getMatrix(0, 0, -90, 0, 0 ,0);

    @Override
    public void runOpMode() {


        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AW/DxXD/////AAAAGYJtW/yP3kG0pVGawWtQZngsJNFQ8kp1Md8CaP2NP72Q0on4mGKPLt/lsSnMnUkCFNymrXXOjs0eHMDTvijWRIixEe/sJ4KHEVf1fhf0kqUB29+dZEvh4qeI7tlTU6pIy/MLW0a/t9cpqMksBRFqXIrhtR/vw7ZnErMTZrJNNXqmbecBnRhDfLncklzgH2wAkGmQDn0JSP7scEczgrggcmerXy3v6flLDh1/Tt2QZ8l/bTcEJtthE82i8/8p0NuDDhUyatFK1sZSSebykRz5A4PDUkw+jMTV28iUytrr1QLiQBwaTX7ikl71a1XkBHacnxrqyY07x9QfabtJf/PYNFiU17m/l9DB6Io7DPnnIaFP";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);
        vuforia.setFrameQueueCapacity(1);
        Vuforia.setFrameFormat(PIXEL_FORMAT.RGB565, true);
        VuforiaTrackables roverRuckus = this.vuforia.loadTrackablesFromAsset("RoverRuckus");

        VuforiaTrackable blueTrackable = roverRuckus.get(0);
        VuforiaTrackable redTrackable = roverRuckus.get(1);
        VuforiaTrackable frontTrackable = roverRuckus.get(2);
        VuforiaTrackable backTrackable = roverRuckus.get(3);

        blueTrackable.setName("blue");
        redTrackable.setName("red");
        frontTrackable.setName("front");
        backTrackable.setName("back");




        OpenGLMatrix blueTrackablePosition = getMatrix(90, 0, -90, (float) FIELD_RADIUS, 0, (float) 152.4);
        OpenGLMatrix redTrackablePosition = getMatrix(90, 0, 90 ,(float) -FIELD_RADIUS,0 ,(float) 152.4 );
        OpenGLMatrix frontTrackablePosition = getMatrix(90,  0, 0, 0 , (float)-FIELD_RADIUS , (float) 152.4);
        OpenGLMatrix backTrackablePosition = getMatrix(90, 0, 180, 0, (float) -FIELD_RADIUS , (float) 152.4);


        blueTrackable.setLocation(blueTrackablePosition);
        redTrackable.setLocation(redTrackablePosition);
        frontTrackable.setLocation(frontTrackablePosition);
        backTrackable.setLocation(backTrackablePosition);

        ((VuforiaTrackableDefaultListener) blueTrackable.getListener()).setPhoneInformation(phoneLocation, parameters.cameraDirection);

        waitForStart();

        roverRuckus.activate();

        OpenGLMatrix location = null;

        while (location == null){
            location = ((VuforiaTrackableDefaultListener)
                    blueTrackable.getListener()).getUpdatedRobotLocation();

            idle();
        }

        VectorF translation = location.getTranslation();

        Orientation orientation = Orientation.getOrientation(location,
                AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

        double x = (translation.get(0) * MM_TO_INCHES);
        double y = (translation.get(1) * MM_TO_INCHES);
        double z = (translation.get(2) * MM_TO_INCHES);
        float angle = orientation.thirdAngle;

        telemetry.addData("X" ,  "%.2f", x);
        telemetry.addData("Y" ,  "%.2f", y);
        telemetry.addData("Z" ,  "%.2f", z);
        telemetry.addData("Angle" ,  "%.2f", angle);
        telemetry.update();

        while (opModeIsActive()){idle();}
    }

    public OpenGLMatrix getMatrix(float ax, float ay, float az, float dx, float dy, float dz) {

        return OpenGLMatrix.translation(dx, dy, dz).multiplied
                (Orientation.getRotationMatrix(AxesReference.EXTRINSIC,
                        AxesOrder.XYZ, AngleUnit.DEGREES, ax, ay, az));
    }

    public OpenGLMatrix getLocation (){
        
    }
}