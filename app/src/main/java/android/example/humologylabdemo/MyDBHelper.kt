package android.example.humologylabdemo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDBHelper(context: Context) : SQLiteOpenHelper(context,"MYDB",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("CREATE TABLE NAMETABLE(_id integer primary key autoincrement, FULLNAME TEXT, TIME DATETIME DEFAULT CURRENT_TIMESTAMP)")
//        db?.execSQL("INSERT INTO NAMETABLE(FULLNAME) VALUES ('John Doe')")
//        db?.execSQL("INSERT INTO NAMETABLE(FULLNAME) VALUES ('Jane Doe')")
//        db?.execSQL("INSERT INTO NAMETABLE(FULLNAME) VALUES ('Jill Doe')")
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}