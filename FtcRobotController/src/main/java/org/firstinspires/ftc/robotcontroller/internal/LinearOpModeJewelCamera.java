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
public abstract class LinearOpModeJewelCamera extends MotoLinearOpMode {


  protected JewelFinder jewel;
  public JewelFinder jewelLeft;

  public JewelFinder jewelLeft(){
    return jewelLeft;
  }
  public JewelFinder jewelMiddle;
  public JewelFinder jewelMiddle(){
    return jewelMiddle;
  }
  public JewelFinder jewelRight;
  public JewelFinder jewelRight(){
    return jewelRight;
  }



  public JewelFinder getJewel() {
    return jewel;
  }
}
