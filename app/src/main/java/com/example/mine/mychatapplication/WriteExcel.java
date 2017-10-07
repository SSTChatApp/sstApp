package com.example.mine.mychatapplication;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import jxl.write.WriteException;




class WriteExcel extends AsyncTask<Void, Void, Void> {

    private Context context;
    private Resources r;
    private File inputFile;
    private AlertDialog waitDialog;
    private AlertDialog failDialog;
    private AlertDialog successDialog;


    public void getContext(Context context){

        this.context=context;

        r=context.getResources();


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(R.string.setupWait_ms).setTitle(R.string.setupWait);

        waitDialog= builder.create();

        builder.setMessage(R.string.setupFinished_ms).setTitle(R.string.finished);

        successDialog= builder.create();

        builder.setMessage(R.string.setupFailed_ms).setTitle(R.string.setupFailed);

        failDialog= builder.create();
    }
    @Override
    protected void onPostExecute(Void avoid) {
        super.onPostExecute(avoid);

        final Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);

        final Uri contentUri = Uri.fromFile(inputFile);

        scanIntent.setData(contentUri);

        context.sendBroadcast(scanIntent);


        if(waitDialog.isShowing()){

            waitDialog.dismiss();

        }
        successDialog.show();
    }

    @Override
    protected void onCancelled(Void avoid) {
        super.onCancelled(avoid);
        if(waitDialog.isShowing()){

            waitDialog.dismiss();

        }

        failDialog.show();

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        waitDialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {



        if (!inputFile.exists()) {

            inputFile.mkdirs();

            String employeesFileArr[]=r.getStringArray(R.array.EmployeesTable);

            String divisionsFileArr[]=r.getStringArray(R.array.DivisionsTable);

            String departmentsFileArr[]=r.getStringArray(R.array.DepartmentsTable);

            String sectionsFileArr[]=r.getStringArray(R.array.SectionsTable);

            String positionsFileArr[]=r.getStringArray(R.array.PositionsTable);

            String jobsLevelsFileArr[]=r.getStringArray(R.array.JobLevelTable);



            try {

                write(r.getString(R.string.EmployeesFile),  new ArrayList<>(Arrays.asList(employeesFileArr)));

                write(r.getString(R.string.SectionsFile),  new ArrayList<>(Arrays.asList(sectionsFileArr)));

                write(r.getString(R.string.DivisionsFile),  new ArrayList<>(Arrays.asList(divisionsFileArr)));

                write(r.getString(R.string.DepartmentsFile),  new ArrayList<>(Arrays.asList(departmentsFileArr)));

                write(r.getString(R.string.PositionsFile),  new ArrayList<>(Arrays.asList(positionsFileArr)));

                write(r.getString(R.string.JobsLevelsFile),  new ArrayList<>(Arrays.asList(jobsLevelsFileArr)));




            }
            catch (IOException | WriteException e ){

                e.getStackTrace();

                Log.d("TAG", "onCreate12: "+e.getMessage());
            }
        }
        return null;
    }



    public void setOutputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    private void write(String sheetname, ArrayList<String> colNames) throws IOException, WriteException {
        File file = new File(inputFile.getAbsolutePath(), sheetname + ".xlsx");
        int colNumber=colNames.size();
        //Creating WorkBook
        XSSFWorkbook workbook = new XSSFWorkbook();
        //Creating Sheet
        XSSFSheet firstSheet = workbook.createSheet("Sheet");
        XSSFRow row = firstSheet.createRow(0);
        XSSFCell cell;

        //Formating Style
        XSSFCellStyle style = workbook.createCellStyle();
        style.setBorderTop((short) 6); // double lines border
        style.setBorderBottom((short) 1); // single line border

        //style.setFillForegroundColor(HSSFColor.GREY_50_PERCENT.index);

        //Formating Font
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 15);
        font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
        style.setFont(font);

        //Setting Columns Titles
        for (int i = 0; i < colNumber; i++) {
            cell = row.createCell(i);
            cell.setCellStyle(style);
            cell.setCellValue(new XSSFRichTextString(colNames.get(i)));
        }
        //Writing to Memory
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            workbook.write(fos);
            workbook.close();
        }
        catch (IOException e) {
            Log.d("TAG", "write: " + e.getMessage());
        }
        finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                }
                catch (IOException e) {
                    Log.d("TAG", "write: " + e.getMessage());
                }
            }
        }
    }



}