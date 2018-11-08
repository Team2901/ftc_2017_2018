package org.firstinspires.ftc.teamcode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

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
import org.firstinspires.ftc.teamcode.Utility.VuforiaUtilities;


@Autonomous (name = "VuForiaTest")
public class VuForiaTest extends LinearOpMode {

    VuforiaLocalizer vuforia;

    public final double MM_TO_INCHES = 0.0393701;
    public final double INCHES_TO_MM = 25.4;
    public final double FIELD_RADIUS = 1828.8;


    public OpenGLMatrix phoneLocation = getMatrix(90, -0, -90, 0, 0, 0);

    @Override
    public void runOpMode() {

        VuforiaLocalizer.Parameters parameters = VuforiaUtilities.getBackCameraParameters(hardwareMap);
        vuforia = VuforiaUtilities.getVuforia(parameters);
        VuforiaTrackables roverRuckus = this.vuforia.loadTrackablesFromAsset("RoverRuckus");

        VuforiaTrackable blue = roverRuckus.get(0);
        VuforiaTrackable red = roverRuckus.get(1);
        VuforiaTrackable front = roverRuckus.get(2);
        VuforiaTrackable back = roverRuckus.get(3);

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

        waitForStart();

        roverRuckus.activate();

        OpenGLMatrix location = null;
        while (opModeIsActive()) {
            location = getLocation(blue, red, front, back);

            VectorF translation = location.getTranslation();

            Orientation orientation = Orientation.getOrientation(location,
                    AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

            double x = (translation.get(0) * MM_TO_INCHES);
            double y = (translation.get(1) * MM_TO_INCHES);
            double z = (translation.get(2) * MM_TO_INCHES);
            float angle = orientation.thirdAngle;

            telemetry.addData("X", "%.2f", x);
            telemetry.addData("Y", "%.2f", y);
            telemetry.addData("Z", "%.2f", z);
            telemetry.addData("Angle", "%.2f", angle);
            telemetry.update();


            idle();
        }
    }

    public OpenGLMatrix getMatrix(float ax, float ay, float az, float dx, float dy, float dz) {

        return OpenGLMatrix.translation(dx, dy, dz).multiplied
                (Orientation.getRotationMatrix(AxesReference.EXTRINSIC,
                        AxesOrder.XYZ, AngleUnit.DEGREES, ax, ay, az));
    }

    public OpenGLMatrix getLocation(VuforiaTrackable blue, VuforiaTrackable red,
                                    VuforiaTrackable front, VuforiaTrackable back) {
        OpenGLMatrix location = null;
        OpenGLMatrix blueLocation = null;
        OpenGLMatrix redLocation = null;
        OpenGLMatrix backLocation = null;
        OpenGLMatrix frontLocation = null;

        while (location == null && opModeIsActive()) {
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
            idle();
        }
        return location;
    }
}