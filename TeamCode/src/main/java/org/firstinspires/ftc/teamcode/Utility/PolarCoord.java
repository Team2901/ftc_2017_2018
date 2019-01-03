package org.firstinspires.ftc.teamcode.Utility;

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
    public static double getDistanceBetween(PolarCoord startPolarCoord, PolarCoord goalPolarCoord){
        return Math.sqrt((Math.pow((goalPolarCoord.x - startPolarCoord.x), 2) +
                Math.pow((goalPolarCoord.y - startPolarCoord.y), 2)));
    }
}