package com.example.ben.colorsensorrgbstreamer6well;

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
import java.util.ArrayList;

public class FormattedWorkbook {
    private HSSFWorkbook _workbook;
    private static String[] columns = {"Measurement #", "Sensor #", "Alpha", "Red", "Green", "Blue", "Adjusted Red", "Adjusted Green", "Adjusted Blue"};
    private HSSFSheet _sheet;

    private int measurementColumn = 0;
    private int sensorColumn = 1;
    private int alphaColumn = 2;
    private int redColumn = 3;
    private int greenColumn = 4;
    private int blueColumn = 5;
    private int adjustedRedColumn = 6;
    private int adjustedGreenColumn = 7;
    private int adjustedBlueColumn = 8;

    FormattedWorkbook() {
        _workbook = new HSSFWorkbook();
        _sheet = _workbook.createSheet("Colorimeter Measurements");

        //set default column width to make up for the fact we can't resize columns automagically
        //https://stackoverflow.com/questions/37069820/android-poi-crash-when-using-autosizecolumn
        int fontSize = 14;
        _sheet.setDefaultColumnWidth((int)(0.11 * fontSize * columns[6].length())); //longest column should be Adjusted Green name

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
        ArrayList<Double> alphaVals = colorData.alphaVals;
        ArrayList<Double> redVals = colorData.redVals;
        ArrayList<Double> greenVals = colorData.greenVals;
        ArrayList<Double> blueVals = colorData.blueVals;

        ArrayList<Integer> sensorVals = colorData.sensorVals;

        for(int measurement=0; measurement<totalMeasurements; measurement++) {
            row = _sheet.createRow(measurement+1);
            row.createCell(measurementColumn).setCellValue(measurement+1);
            row.createCell(sensorColumn).setCellValue(sensorVals.get(measurement));
            row.createCell(alphaColumn).setCellValue(alphaVals.get(measurement));
            row.createCell(redColumn).setCellValue(redVals.get(measurement));
            row.createCell(greenColumn).setCellValue(greenVals.get(measurement));
            row.createCell(blueColumn).setCellValue(blueVals.get(measurement));
            row.createCell(adjustedRedColumn).setCellValue(redVals.get(measurement)/alphaVals.get(measurement) *100);
            row.createCell(adjustedGreenColumn).setCellValue(greenVals.get(measurement)/alphaVals.get(measurement) *100);
            row.createCell(adjustedBlueColumn).setCellValue(blueVals.get(measurement)/alphaVals.get(measurement) *100);
        }

        this.save(saveFileName);
    }

    private void save(String saveFileName) throws IOException {
        String filePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath()
                + saveFileName;
        FileOutputStream fileOut = new FileOutputStream(filePath);
        _workbook.write(fileOut);
        fileOut.close();
    }
}
