package com.example.mine.mychatapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



class sqlDB extends SQLiteOpenHelper {

    private static String databaseName="Mydatabase";

    private  String secCreation="create table Section"+
            "(ID text,"+
            "name text,"+
            "DepCode text,"+
            "SecCode text,"+
            "manager text,"+
            "closed text);";

    private String empCreation="create table Employee" +
            "(ID text,"+
            "name text,"+
            "Bdate text,"+
            "sCode text,"+
            "posCode text,"+
            "Hdate text,"+
            "sex text,"+
            "Bdest text,"+
            "FOREIGN KEY(sCode) REFERENCES Section (SecCode)";
    private SQLiteDatabase db;
    public sqlDB(Context context)
    {
        super(context,databaseName,null,2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(empCreation);
        sqLiteDatabase.execSQL(secCreation);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        db.execSQL("DROP TABLE IF EXISTS Employee");
        db.execSQL("DROP TABLE IF EXISTS Section");
    }


    public void addSection(Section sec)
    {

        ContentValues row=new ContentValues();
        row.put("ID",sec.ID);
        row.put("name",sec.name);
        row.put("DepCode",sec.departmentCode);
        row.put("SecCode",sec.sectionCode);
        row.put("manager",sec.manager);
        row.put("closed",sec.closed);
        db=getWritableDatabase();
        db.insert("Section",null,row);
        db.close();

    }
    public Cursor fetchAllSections()
    {
        db=getReadableDatabase();
        String[] row={"ID","name","DepCode","SecCode","manager","closed"};
        Cursor cursor=db.query("Section",row,null,null,null,null,null);
        if (cursor!=null)
        {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }
    public Cursor fetchSectionBySecCode(String SecCode)
    {
        db=getReadableDatabase();
        String[] arg={SecCode};
        Cursor cursor=db.rawQuery("select * from Section where SecCode like ?",arg);
        if (cursor!=null)
        {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }




    public void addEmployee(Employee emp)
    {

        ContentValues row=new ContentValues();
        row.put("ID",emp.nationalID);
        row.put("name",emp.name);
        row.put("Bdate",emp.birthDate);
        row.put("sCode",emp.sectionCode);
        row.put("posCode",emp.positionCode);
        row.put("Hdate",emp.hiringDate);
        row.put("sex",emp.sex);
        row.put("Bdest",emp.birthDestination);
        row.put("Bdest",emp.birthDestination);


        db=getWritableDatabase();
        db.insert("Employee",null,row);
        db.close();

    }
    public Cursor fetchEmployeesBySecCode(String SecCode)
    {
        db=getReadableDatabase();
        String[] arg={SecCode};
        Cursor cursor=db.rawQuery("select * from Employee where sCode like ?",arg);
        if (cursor!=null)
        {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }
}
