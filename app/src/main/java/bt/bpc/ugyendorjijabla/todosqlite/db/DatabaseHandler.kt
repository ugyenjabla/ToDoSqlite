package bt.bpc.ugyendorjijabla.todosqlite.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import bt.bpc.ugyendorjijabla.todosqlite.modules.Tasks

class DatabaseHandler(var context: Context) : SQLiteOpenHelper(context, DatabaseHandler.DB_NAME, null, DatabaseHandler.DB_Version ) {
    override fun onCreate(db: SQLiteDatabase?) {
    val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $NAME TEXT, $DESC TEXT, $COMPLETED TEXT)"
        db!!.execSQL(CREATE_TABLE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    val DROP_TABLE = "Drop Table if exist $TABLE_NAME"
        db!!.execSQL(DROP_TABLE)
        onCreate(db)
    }
    //function to insert database
    fun addTask(tasks: Tasks): Boolean{
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put(NAME, tasks.name)
        cv.put(DESC, tasks.desc)
        cv.put(COMPLETED, tasks.completed)

        var status = db.insert(TABLE_NAME, null, cv)
        db.close()
        return (Integer.parseInt("$status")!= -1)
    }

    //function to get data from the database
    fun getTask(id: Int):Tasks {
        val task = Tasks()
        val db = this.writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $ID = $id"
        val cursor = db.rawQuery(selectQuery, null)
        cursor.moveToFirst()
        task.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        task.name = cursor.getString(cursor.getColumnIndex(NAME))
        task.desc = cursor.getString(cursor.getColumnIndex(DESC))
        task.completed = cursor.getString(cursor.getColumnIndex(COMPLETED))
        cursor.close()
        return task

    }
    fun task(): List<Tasks> {
        val taskList = ArrayList<Tasks>()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val tasks = Tasks()
                    tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    tasks.name = cursor.getString(cursor.getColumnIndex(NAME))
                    tasks.desc = cursor.getString(cursor.getColumnIndex(DESC))
                    tasks.completed = cursor.getString(cursor.getColumnIndex(COMPLETED))
                    taskList.add(tasks)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return taskList
    }

    fun updateTask(tasks: Tasks): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, tasks.name)
        values.put(DESC, tasks.desc)
        values.put(COMPLETED, tasks.completed)
        val _success = db.update(TABLE_NAME, values, ID + "=?", arrayOf(tasks.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteTask(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, ID + "=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteAllTasks(): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, null, null).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    companion object {
        val DB_Version = 1
        val DB_NAME = "MyTasks"
        val TABLE_NAME = "Tasks"
        val ID = "id"
        val NAME = "name"
        val DESC = "desc"
        val COMPLETED = "completed"
    }


}