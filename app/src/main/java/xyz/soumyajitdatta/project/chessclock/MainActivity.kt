package xyz.soumyajitdatta.project.chessclock

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("Recycle")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sqLiteDatabase = this.openOrCreateDatabase("TimesDB", MODE_PRIVATE, null)
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS times (name TEXT,minutesblack INT(10),minuteswhite INT(10),increment INT(5),id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT )")
        val c: Cursor = sqLiteDatabase.rawQuery("SELECT * FROM times", null)
        if (c.count==0)
        {
            sqLiteDatabase.execSQL("INSERT INTO times (name, minutesblack,minuteswhite,increment) VALUES ('Bullet',1,1,0)");
            sqLiteDatabase.execSQL("INSERT INTO times (name, minutesblack,minuteswhite,increment) VALUES ('Blitz1',3,3,0)");
            sqLiteDatabase.execSQL("INSERT INTO times (name, minutesblack,minuteswhite,increment) VALUES ('Blitz2',5,5,0)");
            sqLiteDatabase.execSQL("INSERT INTO times (name, minutesblack,minuteswhite,increment) VALUES ('Rapid1',10,10,0)");
            sqLiteDatabase.execSQL("INSERT INTO times (name, minutesblack,minuteswhite,increment) VALUES ('Rapid2',30,30,0)");
            sqLiteDatabase.execSQL("INSERT INTO times (name, minutesblack,minuteswhite,increment) VALUES ('Classical',60,60,0)");
            sqLiteDatabase.execSQL("INSERT INTO times (name, minutesblack,minuteswhite,increment) VALUES ('Armageddon',4,5,0)");
        }
        val unicode = 0x1F496
        val emoji=getEmojiByUnicode(unicode)
        val textview=findViewById<TextView>(R.id.textView2)
        textview.text= "Coded with $emoji by Soumyajit Datta"
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(applicationContext, listActivity::class.java)
            startActivity(intent)
        }, 3000)
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            System.exit(0)
            return true
        }
        return false
    }
    fun getEmojiByUnicode(unicode: Int): String? {
        return String(Character.toChars(unicode))
    }
}
