package com.example.ben.colorsensorrgbstreamer6well;

import android.graphics.Color;

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

public class PlotManager {

    PlotManager() {}

    public void makePlot(Double upperDomainBoundary, Double upperRangeBoundary, XYPlot plot, ColorData colorData) {
        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
//        XYSeries alphaSeries = new SimpleXYSeries(
//                alphaVals, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Alpha Values");
        XYSeries redSeries = new SimpleXYSeries(
                colorData.redVals, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Red Values");
        XYSeries greenSeries = new SimpleXYSeries(
                colorData.greenVals, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Green Values");
        XYSeries blueSeries = new SimpleXYSeries(
                colorData.blueVals, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Blue Values");

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
}
