package org.firstinspires.ftc.teamcode.Utility;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

public class PolarCoord {
    public double x;
    public double y;
    public double theta;


    public PolarCoord(double x, double y, double theta) {
        this.x = x;
        this.y = y;
        this.theta = theta;
    }

    public PolarCoord(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public PolarCoord(final OpenGLMatrix location) {
        VectorF translation = location.getTranslation();
        Orientation orientation = Orientation.getOrientation(location,
                AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

        this.x = (translation.get(0) * VuforiaUtilities.MM_TO_INCHES);
        this.y = (translation.get(1) * VuforiaUtilities.MM_TO_INCHES);
        this.theta = orientation.thirdAngle;
    }
    public static double getDistanceBetween(PolarCoord startPolarCoord, PolarCoord goalPolarCoord){
        return Math.sqrt((Math.pow((goalPolarCoord.x - startPolarCoord.x), 2) +
                Math.pow((goalPolarCoord.y - startPolarCoord.y), 2)));
    }
}