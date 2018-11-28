package com.example.ben.colorsensorrgbstreamer6well;

import android.graphics.Color;
import android.os.Environment;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;

import java.io.FileOutputStream;
import java.io.IOException;

public class FormattedWorkbook {
    private HSSFWorkbook _workbook;
    private static String[] columns = {"Measurement #", "Sensor #", "Alpha", "Red", "Green", "Blue", "Percent Red", "Percent Green", "Percent Blue", "Hex Color"};
    private HSSFSheet _sheet;

    private int measurementColumn = 0;
    private int sensorColumn = 1;
    private int alphaColumn = 2;
    private int redColumn = 3;
    private int greenColumn = 4;
    private int blueColumn = 5;
    private int percentRedColumn = 6;
    private int percentGreenColumn = 7;
    private int percentBlueColumn = 8;
    private int hexColorColumn = 9;

    FormattedWorkbook() {
        _workbook = new HSSFWorkbook();
        _sheet = _workbook.createSheet("Colorimeter Measurements");

        //set default column width to make up for the fact we can't resize columns automagically
        //https://stackoverflow.com/questions/37069820/android-poi-crash-when-using-autosizecolumn
        int fontSize = 14;
        _sheet.setDefaultColumnWidth((int)(0.11 * fontSize * columns[6].length())); //longest column should be Percent Green name

        // Create a Font for styling header cells
        Font headerFont = _workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) fontSize);
        headerFont.setColor(IndexedColors.RED.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = _workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = _sheet.createRow(0);

        // Create cells
        for(int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
            cell.setCellStyle(headerCellStyle);
        }
    }

    public void output(String saveFileName, ColorData colorData) throws IOException {
        //make the excel sheet
        int totalMeasurements = colorData.alphaVals.size();//number of measurements is equal to the number of values in a parameters array
        Row row;

        Double alpha;
        Double red;
        Double green;
        Double blue;

        Double percentRed;
        Double percentGreen;
        Double percentBlue;

        Integer sensorNum;

        for(int measurement=0; measurement<totalMeasurements; measurement++) {
            row = _sheet.createRow(measurement+1);
            alpha = colorData.alphaVals.get(measurement);
            red = colorData.redVals.get(measurement);
            green = colorData.greenVals.get(measurement);
            blue = colorData.blueVals.get(measurement);

            percentRed = calculateColorPercent(red, alpha, red, green, blue);
            percentGreen = calculateColorPercent(green, alpha, red, green, blue);
            percentBlue = calculateColorPercent(blue, alpha, red, green, blue);

            sensorNum = colorData.sensorVals.get(measurement);

            //Generate cells and populate them.
            row.createCell(measurementColumn).setCellValue(measurement+1);
            row.createCell(sensorColumn).setCellValue(sensorNum);
            row.createCell(alphaColumn).setCellValue(alpha);
            row.createCell(redColumn).setCellValue(red);
            row.createCell(greenColumn).setCellValue(green);
            row.createCell(blueColumn).setCellValue(blue);
            row.createCell(percentRedColumn).setCellValue(percentRed);
            row.createCell(percentGreenColumn).setCellValue(percentGreen);
            row.createCell(percentBlueColumn).setCellValue(percentBlue);
            row.createCell(hexColorColumn).setCellValue(colorHexString(percentRed, percentGreen, percentBlue));
        }

        this.save(saveFileName);
    }

    private Double calculateColorPercent(Double numerator, Double alpha, Double red, Double green, Double blue) {
        Double adjNumerator = numerator/alpha;
        Double adjRed = red/alpha;
        Double adjGreen = green/alpha;
        Double adjBlue = blue/alpha;
        return adjNumerator/(adjRed+adjGreen+adjBlue) * 100.0;
    }

    private String colorHexString(Double percentRed, Double percentGreen, Double percentBlue) {
        int redInt = (int)Math.round(2.55*percentRed); // 255/100 maps a percentage to a value between 0-255
        int greenInt = (int)Math.round(2.55*percentGreen);
        int blueInt =  (int)Math.round(2.55*percentBlue);

        //Convert numbers into a color int, and format into a hex string
        return String.format("#%06X", (0xFFFFFF & Color.rgb(redInt, greenInt, blueInt)));
    }

    private void save(String saveFileName) throws IOException {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()
                + saveFileName;
        FileOutputStream fileOut = new FileOutputStream(filePath);
        _workbook.write(fileOut);
        fileOut.close();
    }
}
