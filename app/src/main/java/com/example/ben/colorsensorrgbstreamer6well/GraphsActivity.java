package com.example.ben.colorsensorrgbstreamer6well;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.androidplot.ui.Size;
import com.androidplot.ui.SizeMode;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.example.ben.colorsensorrgbstreamer6well.R;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class GraphsActivity extends AppCompatActivity {

    private XYPlot plot;
    private int measurementNum;
    private Double maxYVal = 0d;
    private ArrayList<Number> alphaVals= new ArrayList<>();
    private ArrayList<Number> redVals = new ArrayList<>();
    private ArrayList<Number> greenVals = new ArrayList<>();
    private ArrayList<Number> blueVals = new ArrayList<>();

    private Button buttonSaveGraph;
    private EditText editTextDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//Force Landscape mode

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) { // only make a graph if information is passed
            for (int i = 0; i < bundle.size(); i++) {
                String index = Integer.toString(i);
                Log.i("Bundle Info", (String) bundle.get(index));
            }
            processBundle(getIntent().getExtras());
            findMaxYValue();
            makePlot(measurementNum+1, maxYVal);//measurementNum+1 is done so that all bars on the graph are visible
        }
        buttonSaveGraph = findViewById(R.id.save_image_button);
        buttonSaveGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveGraphAsImage();
            }
        });
    }

    void processBundle(Bundle graphBundle) {
        String dataString;
        String colorStr;
        Double colorVal;
        int colorIndex;
        for(int i=0; i<graphBundle.size(); i++) {
            dataString = graphBundle.getString(Integer.toString(i));

            //Find the index of the string where we define what the color measured
            if(dataString.contains("A")) {colorIndex = dataString.indexOf("A");}
            else if(dataString.contains("R")) {colorIndex = dataString.indexOf("R");}
            else if(dataString.contains("G")) {colorIndex = dataString.indexOf("G");}
            else if(dataString.contains("B")) {colorIndex = dataString.indexOf("B");}
            else {colorIndex = 0;}

            //Split the string into the different parts
            measurementNum = Integer.parseInt(dataString.substring(0, colorIndex));
            colorStr = dataString.substring(colorIndex,colorIndex+1);
            colorVal = Double.parseDouble(dataString.substring(colorIndex+1, dataString.length()));

            switch(colorStr) {
                case "A":
                    alphaVals.add(colorVal);
                    break;
                case "R":
                    redVals.add(colorVal);
                    break;
                case "G":
                    greenVals.add(colorVal);
                    break;
                case "B":
                    blueVals.add(colorVal);
                    break;
            }
            Log.i("Data", dataString);
            Log.i("Color Index", Integer.toString(colorIndex));
            Log.i("Color Val", Double.toString(colorVal));
        }
    }

    void findMaxYValue() {
        Double value;
//        for(int i=0; i<alphaVals.size(); i++) {
//            value = (int)alphaVals.get(i);
//            if(value > maxYVal) {
//                maxYVal = value;
//            }
//        }
        for(int i=0; i<redVals.size(); i++) {
            value = (Double)redVals.get(i);
            if(value > maxYVal) {
                maxYVal = value;
            }
        }
        for(int i=0; i<greenVals.size(); i++) {
            value = (Double)greenVals.get(i);
            if(value > maxYVal) {
                maxYVal = value;
            }
        }
        for(int i=0; i<blueVals.size(); i++) {
            value = (Double)blueVals.get(i);
            if(value > maxYVal) {
                maxYVal = value;
            }
        }
    }

    void makePlot(int upperDomainBoundary, Double upperRangeBoundary) {
        // initialize our XYPlot reference:
        plot = findViewById(R.id.plot);

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
//        XYSeries alphaSeries = new SimpleXYSeries(
//                alphaVals, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Alpha Values");
        XYSeries redSeries = new SimpleXYSeries(
                redVals, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Red Values");
        XYSeries greenSeries = new SimpleXYSeries(
                greenVals, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Green Values");
        XYSeries blueSeries = new SimpleXYSeries(
                blueVals, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Blue Values");

//        BarFormatter alphaFormatter = new BarFormatter(Color.BLACK, Color.WHITE);
//        plot.addSeries(alphaSeries, alphaFormatter);

        BarFormatter redFormatter = new BarFormatter(Color.RED, Color.WHITE);
        plot.addSeries(redSeries, redFormatter);

        BarFormatter greenFormatter = new BarFormatter(Color.GREEN, Color.WHITE);
        plot.addSeries(greenSeries, greenFormatter);

        BarFormatter blueFormatter = new BarFormatter(Color.BLUE, Color.WHITE);
        plot.addSeries(blueSeries, blueFormatter);

        BarRenderer barRenderer = plot.getRenderer(BarRenderer.class);
        barRenderer.setBarOrientation(BarRenderer.BarOrientation.SIDE_BY_SIDE);
        barRenderer.setBarGroupWidth(BarRenderer.BarGroupWidthMode.FIXED_WIDTH, PixelUtils.dpToPix(50));

        plot.setTitle("Detected Measurement Colors");

        //Domain Settings
        plot.setDomainBoundaries(-0.5, upperDomainBoundary, BoundaryMode.FIXED);
        plot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1);
        plot.setUserDomainOrigin(-1);

        //Range Settings
        plot.setRangeBoundaries(0, upperRangeBoundary, BoundaryMode.FIXED);
        plot.setRangeStep(StepMode.INCREMENT_BY_VAL, 100);

        //Set plot size
        plot.getGraph().setSize(new Size(
                PixelUtils.dpToPix(50), SizeMode.FILL,
                PixelUtils.dpToPix(50), SizeMode.FILL
        ));

        //Enable Pan and Zoom
        PanZoom.attach(plot);

        //Enable plot markup
        //plot.setMarkupEnabled(true);
    }

    void saveGraphAsImage() {
        editTextDialog = new EditText(this);
        editTextDialog.setTextColor(Color.BLACK);

        AlertDialog.Builder saveDialogBuilder = new AlertDialog.Builder(this);
        saveDialogBuilder.setMessage("Enter a name for your file: ");
        saveDialogBuilder.setTitle("Save As");
        saveDialogBuilder.setView(editTextDialog);

        saveDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String fullFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()
                        + "/" + editTextDialog.getText().toString() + ".jpg";
                plot.setDrawingCacheEnabled(true);
                int width = plot.getWidth();
                int height = plot.getHeight();
                plot.measure(width, height);
                Bitmap bmp = Bitmap.createBitmap(plot.getDrawingCache());
                plot.setDrawingCacheEnabled(false);
                try {
                    FileOutputStream fos = new FileOutputStream(fullFileName, true);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                }
                catch(FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
        saveDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  Closes the dialog automatically
            }
        });

        AlertDialog saveDialog = saveDialogBuilder.create();
        saveDialog.show();
    }
}
