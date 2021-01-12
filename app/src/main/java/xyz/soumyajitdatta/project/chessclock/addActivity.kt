package xyz.soumyajitdatta.project.chessclock

import android.content.Intent
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import android.widget.Toast

class addActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent= Intent(applicationContext, listActivity::class.java)
            startActivity(intent)
            return true
        }
        return false
    }
    fun onclickadd(view: View)
    {
        val name=findViewById<TextView>(R.id.name)
        val minblack=findViewById<TextView>(R.id.minblack)
        val minwhite=findViewById<TextView>(R.id.minwhite)
        val increment=findViewById<TextView>(R.id.increment)
        if((((minblack.text).toString()).toInt()>60)||(((minwhite.text).toString()).toInt()>60))
        {
            Toast.makeText(this@addActivity, "Please give the time under 60 minutes", Toast.LENGTH_SHORT).show()
        }
        else
        {
            if ((((increment.text).toString()).toInt()>60))
            {
                Toast.makeText(this@addActivity, "Please give the time under 60 seconds", Toast.LENGTH_SHORT).show()
            }
            else
            {
                val nameinstr=(name.text).toString()
                val minblackinint=((minblack.text).toString()).toInt()
                val minwhiteinint=((minwhite.text).toString()).toInt()
                val incrementinint=((increment.text).toString()).toInt()
                val sqLiteDatabase = this.openOrCreateDatabase("TimesDB", MODE_PRIVATE, null)
                val c: Cursor = sqLiteDatabase.rawQuery("SELECT * FROM times WHERE name='$nameinstr'", null)
                if (c.count==0)
                {
                    sqLiteDatabase.execSQL("INSERT INTO times (name, minutesblack,minuteswhite,increment) VALUES ('$nameinstr','$minblackinint','$minwhiteinint','$incrementinint')");
                    val intent= Intent(applicationContext, listActivity::class.java)
                    intent.putExtra(Intent.EXTRA_TEXT,nameinstr+" Timer was added")
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(this@addActivity, "Please Choose a different name", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}