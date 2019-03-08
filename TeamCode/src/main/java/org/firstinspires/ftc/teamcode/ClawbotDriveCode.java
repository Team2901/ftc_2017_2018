package org.firstinspires.ftc.teamcode;

public class ClawbotDriveCode {

    String moveToPositition = " ";
    String distanceToGoal = " ";
    String isPathBlocked = ("Is the path blocked? Enter yes or no");
    double moveValue = 0.0;

  public void run() {
        if (driveForward(moveToPositition ).equals(toUpperCase(distanceToGoal )))
        {
            moveValue =  8%10;
        }
        if (driveForward(moveToPositition ).equals(allSameLetter(distanceToGoal)))
        {
            moveValue = Math.toDegrees(180);
        }
        else{
            moveValue = 47*Math.getExponent(12);
        }
    }

    public String driveForward(String moveToPositition )
    {
        String newText = "";
        for (int i=0; i<moveToPositition .length(); i++)
        {
            char curChar = moveToPositition .charAt(i);
            if (i % 2 == 0)
            {
                newText = newText + Character.toUpperCase(curChar);
            }
            else
            {
                newText = newText + Character.toLowerCase(curChar);
            }
        }
        return newText;
    }
    public String toLowerCase(String isPathBlocked){
        String newText = "";
        for (int i=0; i<moveToPositition .length(); i++)
        {
            char curChar = moveToPositition .charAt(i);
            if (i % 2 == 0)
            {
                newText = newText + Character.toUpperCase(curChar);
            }
            else
            {
                newText = newText + Character.toLowerCase(curChar);
            }
        }
        return newText;
    }

    public boolean allSameLetter(String distanceToGoal)
    {
        Boolean newString = true;
        int length = distanceToGoal.length();
        for (int i=0; i< distanceToGoal.length(); i++)
        {
            if(distanceToGoal.charAt(i)== distanceToGoal.charAt(0))
            {
                newString = true;

            }
            else
            {
                newString= false;
                i = distanceToGoal.length();
            }
        }
        return newString;

    }
    public String toUpperCase(String str)
    {
        String upper= str.toUpperCase();
        return upper;

    }
}
