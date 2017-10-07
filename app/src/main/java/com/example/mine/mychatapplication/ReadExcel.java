package com.example.mine.mychatapplication;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;





class ReadExcel extends AsyncTask<String, Void, String> {

    private Context context;
    private AlertDialog waitDialog;
    private AlertDialog failDialog;
    private AlertDialog successDialog;
    private DatabaseReference mDatabaseReference;

    public void exe(Context context,AlertDialog waitDialog,AlertDialog failDialog,AlertDialog successDialog,String...strings){

        this.context=context;

        this.waitDialog=waitDialog;

        this.failDialog=failDialog;

        this.successDialog=successDialog;

        mDatabaseReference= FirebaseDatabase.getInstance().getReference();

        this.execute(strings);

    }

    @Override
    protected void onPostExecute(String re) {

        super.onPostExecute(re);

        if(waitDialog.isShowing()){

            waitDialog.dismiss();

        }
        successDialog.show();
        //employeesUploadProgress.setVisibility(View.INVISIBLE);

    }

    @Override
    protected String doInBackground(String... strings) {

        File mFile = new File(strings[0]);
        try {
            // Log.d("ahmed", mFile.getAbsolutePath());

            OPCPackage opcPackage = OPCPackage.open(mFile.getAbsolutePath());

            XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);



            XSSFSheet sheet = workbook.getSheetAt(0);

            FormulaEvaluator mFormulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();


            Employee emp = new Employee();

            Section sec1 = new Section();

            Position pos = new Position();

            JobLevel level = new JobLevel();

            Department dep = new Department();

            Division div = new Division();

            Iterator<Row> iterator = sheet.iterator();

            Row row;

            iterator.next();

            while (iterator.hasNext()) {

                row = iterator.next();

                try {

                    switch (strings[1]) {
                        case "1":

                            uploaddiv(row, div, mFormulaEvaluator);

                            break;
                        case "2":

                            uploaddep(row, dep, mFormulaEvaluator);

                            break;
                        case "3":

                            uploadsec(row, sec1, mFormulaEvaluator);

                            break;
                        case "4":

                            uploadlevel(row, level, mFormulaEvaluator);

                            break;
                        case "5":

                            uploadpos(row, pos, mFormulaEvaluator);

                            break;
                        case "6":
                            uploadEmp(row, emp, mFormulaEvaluator);

                            break;

                    }


                } catch (NullPointerException e) {

                    Log.e("TAG", "getCellAsString: NullPointerException: " + e.getMessage());
                }


            }

        } catch (InvalidFormatException | IOException e) {

            Log.d("TAG", "read_data: " + e.getMessage());
        }

        return "Done";
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        waitDialog.show();
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);

        if(waitDialog.isShowing()){

            waitDialog.dismiss();

        }

        failDialog.show();
    }

    private void uploadEmp(Row row, Employee emp, FormulaEvaluator mFormulaEvaluator) {
        //Name
        emp.name = (row.getCell(0)!=null?mFormulaEvaluator.evaluate(row.getCell(0)).getStringValue():"");

        //Sex
        emp.sex = (row.getCell(1)!=null?Boolean.toString(mFormulaEvaluator.evaluate(row.getCell(1)).getBooleanValue()):"");

        //BirthDate
        emp.birthDate = (row.getCell(2)!=null? row.getCell(2).getDateCellValue().toString():"");

        //BirthDestination
        emp.birthDestination = (row.getCell(3)!=null?mFormulaEvaluator.evaluate(row.getCell(3)).getStringValue():"");

        //IdNumber
        emp.nationalID = (row.getCell(4)!=null?mFormulaEvaluator.evaluate(row.getCell(4)).formatAsString():"");

        emp.nationalID = emp.nationalID.replaceAll("[^\\w\\s]", "");

        //SectionCode
        emp.departmentCode = (row.getCell(5)!=null?mFormulaEvaluator.evaluate(row.getCell(5)).formatAsString():"");

        emp.departmentCode = emp.departmentCode.replaceAll("[^\\w\\s]", "");

        //SectionCode
        emp.sectionCode = (row.getCell(6)!=null?mFormulaEvaluator.evaluate(row.getCell(6)).formatAsString():"");

        emp.sectionCode = emp.sectionCode.replaceAll("[^\\w\\s]", "");

        //PositionCode
        emp.positionCode = (row.getCell(7)!=null?mFormulaEvaluator.evaluate(row.getCell(7)).formatAsString():"");

        emp.positionCode = emp.positionCode.replaceAll("[^\\w\\s]", "");

        //HiringDate
        emp.hiringDate = (row.getCell(8)!=null?row.getCell(8).getDateCellValue().toString():"");

        //adding user with national ID as key if exist
        if (!(emp.nationalID.isEmpty())) {

            mDatabaseReference.child(context.getResources().getString(R.string.UsersProfile)).child(emp.nationalID).setValue(emp);

            //Adding User  to Section with User Code  as entire key if Section code exist
            if (!(emp.sectionCode.isEmpty())) {

                mDatabaseReference.child(context.getResources().getString(R.string.SectionsData)).child(emp.sectionCode).setValue(emp.nationalID);

            }
            //Adding User  to Position with User Code  as entire key if Position code exist
            if (!(emp.positionCode.isEmpty())) {

                mDatabaseReference.child(context.getResources().getString(R.string.PositionsData)).child(emp.positionCode).setValue(emp.nationalID);

            }
            publishProgress();
        }

    }

    private void uploadsec(Row row, Section sec1, FormulaEvaluator mFormulaEvaluator) {

       // Log.d("TAG", "uploadsec: "+(Looper.myLooper() == Looper.getMainLooper()));
        //Section Code
        sec1.sectionCode = (row.getCell(0)!=null?mFormulaEvaluator.evaluate(row.getCell(0)).formatAsString():"");

        sec1.sectionCode = sec1.sectionCode.replaceAll("[^\\w\\s]", "");

        //Name
        sec1.name = (row.getCell(1)!=null?mFormulaEvaluator.evaluate(row.getCell(1)).getStringValue():"");

        //DepartmentCode
        sec1.departmentCode = (row.getCell(2)!=null?mFormulaEvaluator.evaluate(row.getCell(2)).formatAsString():"");

        sec1.departmentCode = sec1.departmentCode.replaceAll("[^\\w\\s]", "");

        //Manager Code
        sec1.manager = (row.getCell(3)!=null?mFormulaEvaluator.evaluate(row.getCell(3)).formatAsString():"");

        //Closed or Not
        sec1.closed = (row.getCell(4)!=null?Boolean.toString(mFormulaEvaluator.evaluate(row.getCell(4)).getBooleanValue()):"");


        //adding section with section code as key if exist
        if (!(sec1.sectionCode.isEmpty())) {


            mDatabaseReference.child(context.getResources().getString(R.string.Sections)).child(sec1.sectionCode).setValue(sec1);

            //Adding Section  to Department with Section Code  as entire key if Department code exist
            if (!(sec1.departmentCode.isEmpty())) {

                mDatabaseReference.child(context.getResources().getString(R.string.DepartmentsData)).child(sec1.departmentCode).setValue(sec1.sectionCode);

            }

            publishProgress();
        }

    }

    private void uploaddep(Row row, Department dep, FormulaEvaluator mFormulaEvaluator) {
        //Department Code

        dep.departmentCode = (row.getCell(0)!=null?mFormulaEvaluator.evaluate(row.getCell(0)).formatAsString():"");

        dep.departmentCode = dep.departmentCode.replaceAll("[^\\w\\s]", "");

        //Name

        dep.name = (row.getCell(1)!=null?mFormulaEvaluator.evaluate(row.getCell(1)).getStringValue():"");

        // Division Code

        dep.divisionCode = (row.getCell(2)!=null?mFormulaEvaluator.evaluate(row.getCell(2)).formatAsString():"");

        dep.divisionCode = dep.divisionCode.replaceAll("[^\\w\\s]", "");

        // Department Closed or Not

        dep.closed = (row.getCell(3)!=null?Boolean.toString(mFormulaEvaluator.evaluate(row.getCell(3)).getBooleanValue()):"");

        //adding department with department code as key if exist

        if (!(dep.divisionCode.isEmpty())) {

            mDatabaseReference.child(context.getResources().getString(R.string.Departments)).child(dep.departmentCode).setValue(dep);

            //Adding Department  to Division with Department Code  as entire key if Division code exist
            if (!(dep.divisionCode.isEmpty())) {

                mDatabaseReference.child(context.getResources().getString(R.string.DivisionsData)).child(dep.divisionCode).setValue(dep.departmentCode);

            }

            publishProgress();
        }

    }

    private void uploaddiv(Row row, Division div, FormulaEvaluator mFormulaEvaluator) {

        //Division Code
        div.divisionCode = (row.getCell(0)!=null?mFormulaEvaluator.evaluate(row.getCell(0)).formatAsString():"");

        div.divisionCode = div.divisionCode.replaceAll("[^\\w\\s]", "");

        //Name
        div.name = (row.getCell(1)!=null?mFormulaEvaluator.evaluate(row.getCell(1)).getStringValue():"");

        //Closed or Not
        div.closed = (row.getCell(2)!=null?Boolean.toString(mFormulaEvaluator.evaluate(row.getCell(2)).getBooleanValue()):"");

        //adding division with division code as key if exist
        if (!(div.divisionCode.isEmpty())) {

            mDatabaseReference.child(context.getResources().getString(R.string.Divisions)).child(div.divisionCode).setValue(div);

            publishProgress();
        }

    }

    private void uploadpos(Row row, Position pos, FormulaEvaluator mFormulaEvaluator) {

        //Position Code
        pos.positionCode = (row.getCell(0)!=null?mFormulaEvaluator.evaluate(row.getCell(0)).formatAsString():"");

        pos.positionCode = pos.positionCode.replaceAll("[^\\w\\s]", "");

        //Name
        pos.name = (row.getCell(1)!=null?mFormulaEvaluator.evaluate(row.getCell(1)).getStringValue():"");

        //Jop Levels Code
        pos.levelCode = (row.getCell(2)!=null?mFormulaEvaluator.evaluate(row.getCell(2)).formatAsString():"");

        pos.levelCode = pos.levelCode.replaceAll("[^\\w\\s]", "");

        //Position Closed or Not
        pos.closed = (row.getCell(3)!=null?Boolean.toString(mFormulaEvaluator.evaluate(row.getCell(3)).getBooleanValue()):"");

        //Adding position with position code as key if exist
        if (!(pos.positionCode.isEmpty())) {

            mDatabaseReference.child(context.getResources().getString(R.string.Positions)).child(pos.positionCode).setValue(pos);

            //Adding Position with Position Code to Jop Level as entire key if Level code exist
            if (!(pos.levelCode.isEmpty())) {

                mDatabaseReference.child(context.getResources().getString(R.string.JobLevelsData)).child(pos.levelCode).setValue(pos.positionCode);
            }
            publishProgress();
        }

    }

    private void uploadlevel(Row row, JobLevel level, FormulaEvaluator mFormulaEvaluator) {
        mFormulaEvaluator.evaluate(row.getCell(1)).getStringValue();

        //Jop Level Code
        level.levelCode = (row.getCell(0)!=null?mFormulaEvaluator.evaluate(row.getCell(0)).formatAsString():"");

        level.levelCode = level.levelCode.replaceAll("[^\\w\\s]", "");

        level.name = (row.getCell(1)!=null?mFormulaEvaluator.evaluate(row.getCell(1)).getStringValue():"");


        //Adding Jop Level with Level code as key if exist
        if (!(level.levelCode.isEmpty())) {
            mDatabaseReference.child(context.getResources().getString(R.string.JobLevels)).child(level.levelCode).setValue(level);
            publishProgress();
        }

    }
}