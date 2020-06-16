package com.example.ben.colorsensorrgbstreamer6well;

import android.graphics.Color;
import android.view.SurfaceView;
import android.widget.TextView;

import java.util.HashMap;

public class Sensor {

    private String name;

    private TextView redTextView;
    private TextView greenTextView;
    private TextView blueTextView;

    private SurfaceView surface;

    private Double alphaVal = 0.0;
    private Double redVal = 0.0;
    private Double greenVal = 0.0;
    private Double blueVal = 0.0;

    private HashMap<String, Double> normalizationValues = new HashMap<>();

    public Sensor(){
        normalizationValues.put("A", Double.POSITIVE_INFINITY);
        normalizationValues.put("R", Double.POSITIVE_INFINITY);
        normalizationValues.put("G", Double.POSITIVE_INFINITY);
        normalizationValues.put("B", Double.POSITIVE_INFINITY);
    }

    public void setName(String name){
        this.name = name;
    }

    private void setColorVal(String colorStr, Double colorVal){
        switch(colorStr){
            case "A":
                this.alphaVal = colorVal;
                break;
            case "R":
                this.redVal = colorVal;
                break;
            case "G":
                this.greenVal = colorVal;
                break;
            case "B":
                this.blueVal = colorVal;
                break;
            default:
                break;
        }
    }

    public void setRedTextView(TextView redTextView) {
        this.redTextView = redTextView;
    }

    public void setGreenTextView(TextView greenTextView) {
        this.greenTextView = greenTextView;
    }

    public void setBlueTextView(TextView blueTextView) {
        this.blueTextView = blueTextView;
    }

    public void computeColorAndSetTextViews(String colorStr, Double colorNum){
        //modify appropriate color variable to either be the maximum value, or the passed in value

        if(colorNum > normalizationValues.get(colorStr)){
            setColorVal(colorStr, normalizationValues.get(colorStr));
        }
        else{
            setColorVal(colorStr, colorNum);
        }

        int color = calculateARGB();

        String alphaStr = "" + Color.alpha(color);
        String redStr = "" + Color.red(color);
        String greenStr = "" + Color.green(color);
        String blueStr = "" + Color.blue(color);

        setTextViews();
    }

    public void setTextViews(){
        String redStr = "R: " + redVal;
        String greenStr = "G: " + greenVal;
        String blueStr = "B: " + blueVal;
        redTextView.setText(redStr);
        greenTextView.setText(greenStr);
        blueTextView.setText(blueStr);
    }

    public int calculateARGB(){
        int alpha = (int) (alphaVal / normalizationValues.get("A") * 255);
        int red = (int) (redVal / normalizationValues.get("R") * 255);
        int green = (int) (greenVal / normalizationValues.get("G") * 255);
        int blue = (int) (blueVal / normalizationValues.get("B") * 255);
        return Color.argb(alpha, red, green, blue);
    }

}
