package xyz.soumyajitdatta.project.chessclock

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class clockActivity : AppCompatActivity() {
    var timeinmin1:Int=0
    var timeinmin2:Int=0
    var increment:Int=0
    var timer1:Long=0
    var timer2:Long=0
    var active2=0
    var active=0
    var pauseplay=1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clock)
        val mediaPlayer1: MediaPlayer? = MediaPlayer.create(
            this@clockActivity,
            R.raw.bangs16
        )
        val mediaPlayer: MediaPlayer? = MediaPlayer.create(this, R.raw.knocks1)
        val intent1 = getIntent()
        timeinmin1 = (intent1.getStringExtra(Intent.EXTRA_TEXT))?.toInt() ?: 0
        timeinmin2 = (intent1.getStringExtra(Intent.EXTRA_CC))?.toInt() ?: 0
        increment= (intent1.getStringExtra(Intent.EXTRA_USER))?.toInt() ?: 0
        timer1=(timeinmin1*60*1000).toLong()
        timer2=(timeinmin2*60*1000).toLong()
        val clock1:TextView=findViewById(R.id.clock1)
        val clock2:TextView=findViewById(R.id.clock2)
        clock1.text = settext(timer1)
        clock2.text = settext(timer2)
        clock1.setOnClickListener {
            if(active==0||active==2)
            {
                mediaPlayer?.start()
                setActive(1)
                val timernow=timer2
                var countDownTimer=object : CountDownTimer(timernow, 100) {
                    override fun onTick(millisecondsUntilDone: Long)
                        {
                            clock2.text = settext(millisecondsUntilDone)
                            timer2 = millisecondsUntilDone+(increment*1000).toLong()
                            if (active==0||active==3)
                            {
                                clock2.text = settext(timer2)
                                cancel()
                            }
                        }
                        override fun onFinish()
                        {
                            mediaPlayer1?.start()
                            setActive(3)
                        }
                }.start()
            }
        }
        clock2.setOnClickListener {
            if(active==1||active==2)
            {
                mediaPlayer?.start()
                val timernow=timer1
                setActive(0)
                var countDownTimer=object: CountDownTimer(timernow, 100) {
                    override fun onTick(millisecondsUntilDone: Long)
                    {
                        clock1.text = settext(millisecondsUntilDone)
                        timer1 = millisecondsUntilDone+(increment*1000).toLong()
                        if (active==1||active==3)
                        {
                            clock1.text = settext(timer1)
                            cancel()
                        }
                    }
                    override fun onFinish()
                    {
                        mediaPlayer1?.start()
                        setActive(3)
                    }
                }.start()
            }
        }
    }
    fun onclickpauseplay(view: View?) {
        if (pauseplay==1)
        {
            val button: Button = findViewById(R.id.pauseplay)
            button.setBackgroundResource(android.R.drawable.ic_media_play)
            active2=active
            setActive(3)
            pauseplay=0
        }
        else
        {
            val button: Button = findViewById(R.id.pauseplay)
            button.setBackgroundResource(android.R.drawable.ic_media_pause)
            setActive(active2)
            if (active2==1)
            {
                findViewById<TextView>(R.id.clock1).performClick()
            }
            else if(active2==0)
            {
                findViewById<TextView>(R.id.clock2).performClick()
            }
            pauseplay=1
        }
    }
    fun  settext(ms: Long):String
    {
        var seconds:Int=(ms/1000).toInt()
        val milli=((ms-(seconds*1000).toLong())/100).toInt()
        val minutes:Int=(seconds/60)
        seconds -= (minutes * 60)
        var secondsinstring:String=""
        var minsinstring:String=""
        var text=""
        if ((seconds/10)<=0)
        {
            secondsinstring="0$seconds"
        }
        else
        {
            secondsinstring="$seconds"
        }
        if ((minutes/10)<=0)
        {
            minsinstring="0$minutes"
        }
        else
        {
            minsinstring="$minutes"
        }
        if(minutes==0&&seconds<20)
        {
            text= "$minsinstring:$secondsinstring"+"."+(milli).toString()
        }
        else
        {
            text= "$minsinstring:$secondsinstring"
        }
        return text
    }
    fun setActive(number: Int?)
    {
        if (number != null) {
            active=number
        }
    }
    fun reset(view: View?)
    {
        val intent=Intent(applicationContext, clockActivity::class.java)
        intent.putExtra(Intent.EXTRA_TEXT,timeinmin1.toString())
        intent.putExtra(Intent.EXTRA_CC,timeinmin2.toString())
        intent.putExtra(Intent.EXTRA_USER, increment.toString())
        startActivity(intent)
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            val intent=Intent(applicationContext, listActivity::class.java)
            startActivity(intent)
            return true
        }
        return false
    }
    fun onclicklist(view: View?)
    {
        val intent=Intent(applicationContext, listActivity::class.java)
        startActivity(intent)
    }
}
