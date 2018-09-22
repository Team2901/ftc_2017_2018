package org.firstinspires.ftc.robotcontroller.internal;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.util.Log;
import android.widget.FrameLayout;
import org.firstinspires.ftc.robotcontroller.internal.FtcRobotControllerActivity;
import com.qualcomm.ftcrobotcontroller.R;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * TeleOp Mode
 * <p/>
 * Enables control of the robot via the gamepad
 */
public class LinearOpModeJewelCamera extends LinearOpModeCamera {


  // protected JewelFinder jewel;
  protected JewelFinder jewelLeft;
  public JewelFinder jewelLeft(){
    int jewelLeft = FtcRobotControllerActivity.COLOR_BLUE;
    return jewelLeft();
  }
  protected JewelFinder jewelMiddle;
  public JewelFinder jewelMiddle(){
    int jewelMiddle = FtcRobotControllerActivity.COLOR_GREEN;
    return jewelMiddle();
  }
  protected JewelFinder jewelRight;
  public JewelFinder jewelRight(){
    int jewelRight = FtcRobotControllerActivity.COLOR_PINK;
    return jewelRight();
  }



 // public JewelFinder getJewel() {
 //   return jewel;
 // }
}
