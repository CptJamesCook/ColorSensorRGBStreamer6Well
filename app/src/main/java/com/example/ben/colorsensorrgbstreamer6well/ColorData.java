package com.example.ben.colorsensorrgbstreamer6well;

import android.content.Intent;

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

    public Intent createGraphIntent(Intent graphIntent) {
        Integer numberOfValues = alphaVals.size() + redVals.size() + greenVals.size() + blueVals.size();
        Integer intentIndex = 0;
        Integer measurement = 0;
        while(intentIndex < numberOfValues) {
            graphIntent.putExtra(Integer.toString(intentIndex), Integer.toString(measurement) + "A" + Double.toString(alphaVals.get(measurement)));
            intentIndex++;
            graphIntent.putExtra(Integer.toString(intentIndex), Integer.toString(measurement) + "R" + Double.toString(redVals.get(measurement)));
            intentIndex++;
            graphIntent.putExtra(Integer.toString(intentIndex), Integer.toString(measurement) + "G" + Double.toString(greenVals.get(measurement)));
            intentIndex++;
            graphIntent.putExtra(Integer.toString(intentIndex), Integer.toString(measurement) + "B" + Double.toString(blueVals.get(measurement)));
            intentIndex++;
            measurement++;
        }
        return graphIntent;
    }

    public void addSensorBeingUsed(int sensorNum) {
        sensorVals.add(sensorNum);
    }
}
