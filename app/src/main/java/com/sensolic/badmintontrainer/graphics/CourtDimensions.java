package com.sensolic.badmintontrainer.graphics;

import android.content.res.Resources;
import android.widget.ImageView;

import com.sensolic.badmintontrainer.R;

public class CourtDimensions {

    private float[][] courtCoordinates = new float[4][2];   //This 2-dimensional array stores the coordinates of all 4 edges of the court (coordinates in px)
    private int courtWidth;
    private int courtHeight;
    private float marginCourt;
    private ImageView court;

    public CourtDimensions(ImageView courtReference, Resources resources){
        court = courtReference;
        marginCourt = resources.getDimension(R.dimen.margin_court_tablet);
        initializeDimensions();
    }

    public void initializeDimensions(){
        courtHeight = court.getHeight();
        courtWidth = court.getWidth();

        int[] loc = new int[2];
        court.getLocationOnScreen(loc);

        //Coordinates of left upper edge
        courtCoordinates[0][0] = loc[0];
        courtCoordinates[0][1] = loc[1];

        //Coordinates of right upper edge
        courtCoordinates[1][0] = courtCoordinates[0][0] + courtWidth;
        courtCoordinates[1][1] = courtCoordinates[0][1];

        //Coordinates of right lower edge
        courtCoordinates[2][0] = courtCoordinates[1][0];
        courtCoordinates[2][1] = courtCoordinates[1][1] + courtHeight;

        //Coordinates of left lower edge
        courtCoordinates[3][0] = courtCoordinates[0][0];
        courtCoordinates[3][1] = courtCoordinates[0][1] + courtHeight;
    }

    public float[] getPosOnCenter(ImageView image, float[] coordinates){
        int width = image.getWidth();
        int height = image.getHeight();

        if(coordinates.length != 2) return null;

        float[] result = new float[2];
        result[0] = coordinates[0] - ((float) width /2);
        result[1] = coordinates[1] - ((float) height/2);

        return result;
    }

    /**
     * This method checks whether the coordinates are in the court or not
     * @param coordinates Insert the coordinates here
     * @return True if the coordinates are in the court -> false if not
     */
    public boolean  isPartOfCourt(float[] coordinates){

        if(coordinates.length != 2) return false;   //Returns if coordinates are not 2 dimensional

        //Creating testPoint and testVector
        //testPoint has first the x-coordinate of the court and the y-coordinate of the coordinates to be checked
        float[] testPoint= new float[2], testVector = new float[2];
        testPoint[0] = courtCoordinates[0][0];
        testPoint[1] = coordinates[1];

        //Setting up a vector from the testPoint to the coordinates to be checked
        testVector[0] = coordinates[0]-testPoint[0];
        if(testVector[0] < 0) return false;     //If the x-coordinate is negative, then the coordinates to be checked are left outside the court -> return false
        testVector[1] = coordinates[1]-testPoint[1];

        //Calculating the length of the vector
        float vectorLength = (testVector[0]*testVector[0])+(testVector[1]*testVector[1]);
        vectorLength = (float) Math.sqrt((double) vectorLength);
        if(vectorLength > courtWidth) return false;     //If the length of the vector is greater then the courtWidth, then the coordinates to be checked are right outside the court -> return false

        //testPoint now gets the x-coordinate of the coordinates to be checked and the y-coordinate of the court
        testPoint[0] = coordinates[0];
        testPoint[1] = courtCoordinates[0][1];

        //Setting up the vector from the the testPoint to the coordinates to be checked
        testVector[0] = coordinates[0]-testPoint[0];
        testVector[1] = coordinates[1]-testPoint[1];
        if(testVector[1] < 0) return false;     //If the y-coordinate is negative, then the coordinates to be checked are above the court -> return false

        //Calculating the length of the vector
        vectorLength = (testVector[0]*testVector[0])+(testVector[1]*testVector[1]);
        vectorLength = (float) Math.sqrt((double) vectorLength);
        return !(vectorLength > courtHeight);   //If the length of the vector is greater then the courtHeight, then the coordinates to be checked are below the court -> return false
                                                                                                                                                                    // -> else return true
    }

    public float[][] getCourtCoordinates() {
        return courtCoordinates;
    }

    public int getCourtWidth() {
        return courtWidth;
    }

    public int getCourtHeight() {
        return courtHeight;
    }
}
