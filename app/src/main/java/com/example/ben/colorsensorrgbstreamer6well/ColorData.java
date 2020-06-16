package com.example.ben.colorsensorrgbstreamer6well;

import java.util.ArrayList;

public class ColorData {
    public ArrayList<Double> alphaVals = new ArrayList<>();
    public ArrayList<Double> redVals = new ArrayList<>();
    public ArrayList<Double> greenVals = new ArrayList<>();
    public ArrayList<Double> blueVals = new ArrayList<>();
    public ArrayList<Integer> sensorVals = new ArrayList<>();
    public Double largestNonAlphaValue = 0.0d;
    public int numberOfMeasurements = 0;

    public ColorData() {
        // Do nothing on instantiation
    }

    public void processData(String colorStr, Double colorVal) {
        switch(colorStr){
            case "A":
                alphaVals.add(colorVal);
                break;
            case "R":
                redVals.add(colorVal);
                if(colorVal > largestNonAlphaValue)
                    largestNonAlphaValue = colorVal;
                break;
            case "G":
                greenVals.add(colorVal);
                if(colorVal > largestNonAlphaValue)
                    largestNonAlphaValue = colorVal;
                break;
            case "B":
                blueVals.add(colorVal);
                if(colorVal > largestNonAlphaValue)
                    largestNonAlphaValue = colorVal;
                break;
            default:
                //Do nothing
                break;
        }
        numberOfMeasurements = alphaVals.size();
    }

    public void addSensorBeingUsed(int sensorNum) {
        sensorVals.add(sensorNum);
    }
}
