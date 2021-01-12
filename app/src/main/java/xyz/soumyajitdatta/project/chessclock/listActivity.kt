package xyz.soumyajitdatta.project.chessclock

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class listActivity : AppCompatActivity() {
    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        val friendsListView: ListView = findViewById(R.id.timeListView)
        val intent1 = getIntent()
        try
        {
            if((intent1.getStringExtra(Intent.EXTRA_TEXT))!=null)
            {
                Toast.makeText(this, (intent1.getStringExtra(Intent.EXTRA_TEXT)), Toast.LENGTH_SHORT).show()
            }
        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }
        val myTimes = ArrayList<String>()
        val myTimes1 = ArrayList<String>()
        val sqLiteDatabase = this.openOrCreateDatabase("TimesDB", MODE_PRIVATE, null)
        val c: Cursor = sqLiteDatabase.rawQuery("SELECT * FROM times", null)
        val nameIndex = c.getColumnIndex("name")
        val min1Index = c.getColumnIndex("minutesblack")
        val min2Index = c.getColumnIndex("minuteswhite")
        val incrementIndex = c.getColumnIndex("increment")
        c.moveToFirst()
        for (i in 0 until c.count) {
            if(c.getInt(min1Index)==c.getInt(min2Index))
            {
                myTimes.add(
                    c.getString(nameIndex) + "   " + c.getInt(min1Index)
                        .toString() + "+" + c.getInt(
                        incrementIndex
                    ).toString()
                )
            }
            else
            {
                myTimes.add(
                    c.getString(nameIndex) + "   (Black:" + c.getInt(min1Index)
                        .toString() + "+" + c.getInt(
                        incrementIndex
                    ).toString() + ",White:" + c.getInt(min2Index).toString() + "+" + c.getInt(
                        incrementIndex
                    ).toString() + ")"
                )
            }
            myTimes1.add(c.getString(nameIndex))
            c.moveToNext()
        }
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, myTimes)
        friendsListView.adapter = arrayAdapter
        friendsListView.onItemClickListener =
            OnItemClickListener { adapterView, view, i, l ->
                //Toast.makeText(applicationContext, "Hello " + myTimes[i], Toast.LENGTH_LONG).show()
                val name=myTimes1[i]
                val c1:Cursor=sqLiteDatabase.rawQuery(
                    "SELECT * FROM times WHERE name='$name'",
                    null
                )
                var min1=0
                var min2=0
                var increment=0
                c1.moveToFirst()
                if(c1.count==1)
                {
                    min1=c1.getInt(c1.getColumnIndex("minutesblack"))
                    min2=c1.getInt(c1.getColumnIndex("minuteswhite"))
                    increment=c1.getInt(c1.getColumnIndex("increment"))
                }
                else
                {
                    Toast.makeText(this, "NOT FOUND", Toast.LENGTH_SHORT).show()
                }
                val mins1instr=min1.toString()
                val mins2instr=min2.toString()
                val incrementinstr=increment.toString()
                val intent = Intent(applicationContext, clockActivity::class.java)
                intent.putExtra(Intent.EXTRA_TEXT, mins1instr)
                intent.putExtra(Intent.EXTRA_CC, mins2instr)
                intent.putExtra(Intent.EXTRA_USER, incrementinstr)
                startActivity(intent)
            }
        friendsListView.setOnItemLongClickListener(OnItemLongClickListener { adapterView, view, i, l ->
            val name=myTimes1[i]
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Are you sure?")
                .setMessage("Do you want to delete this time?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialogInterface, i ->
                    sqLiteDatabase.execSQL("DELETE FROM times WHERE name='$name'");
                    val intent=Intent(applicationContext, listActivity::class.java)
                    intent.putExtra(Intent.EXTRA_TEXT,name+" Timer was deleted")
                    startActivity(intent)
                })
                .setNegativeButton("No", null)
                .show()
            true
        })
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent=Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            return true
        }
        return false
    }
    fun addtime(view: View)
    {
        val intent=Intent(applicationContext, addActivity::class.java)
        startActivity(intent)
    }
}