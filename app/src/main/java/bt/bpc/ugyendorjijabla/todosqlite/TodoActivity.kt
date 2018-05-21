package bt.bpc.ugyendorjijabla.todosqlite

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import bt.bpc.ugyendorjijabla.todosqlite.adapter.TaskRecyclerAdapter
import bt.bpc.ugyendorjijabla.todosqlite.db.DatabaseHandler
import bt.bpc.ugyendorjijabla.todosqlite.modules.Tasks

import kotlinx.android.synthetic.main.activity_todo.*

class TodoActivity : AppCompatActivity() {

    var fab : FloatingActionButton? = null
    var listtask : RecyclerView? = null
    var linearlayout : LinearLayoutManager? = null

    var taskRecyclerAdapter : TaskRecyclerAdapter? = null
    var dbHandler: DatabaseHandler? =null
    var listdatatask: List<Tasks> = ArrayList<Tasks>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)
        setSupportActionBar(toolbar)
// function for initialization view
initview()
        //function for operations
        initoperations()


        }
    private fun initview(){

        fab = findViewById<FloatingActionButton>(R.id.fab)
        listtask = findViewById<RecyclerView>(R.id.listtask)
        linearlayout = LinearLayoutManager(applicationContext)
        (listtask as RecyclerView).layoutManager = linearlayout

        taskRecyclerAdapter = TaskRecyclerAdapter(tasksList = listdatatask, context = applicationContext)



    }

    private fun initoperations() {
fab?.setOnClickListener {
    val i = Intent(applicationContext, AddOrEditActivity::class.java)
    i.putExtra("mode", "A")
startActivity(i)
} }
        override fun onResume() {
            super.onResume()
            initdb()


        }

    private fun initdb() {
        dbHandler = DatabaseHandler(this)
        listdatatask = (dbHandler as DatabaseHandler).task()
        taskRecyclerAdapter = TaskRecyclerAdapter(tasksList = listdatatask, context = applicationContext)
        (listtask as RecyclerView).adapter = taskRecyclerAdapter
    }

    //this method show toolbar like settings,etc
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)

    }
//this method to click our view on the toolbar
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    val id = item?.itemId
    if (id == R.id.mn_delete) {
        val dialog = AlertDialog.Builder(this).setTitle("Info").setMessage("Click 'YES' Delete All Tasks")
                .setPositiveButton("YES", { dialog, i ->
                    dbHandler!!.deleteAllTasks()
                    initdb()
                    dialog.dismiss()
                })
                .setNegativeButton("NO", { dialog, i ->
                    dialog.dismiss()
                })
        dialog.show()
        return true
    }

    return super.onOptionsItemSelected(item)
    }


}

