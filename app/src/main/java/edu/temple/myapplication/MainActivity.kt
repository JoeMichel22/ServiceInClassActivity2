package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    lateinit var tvTimer: TextView
    lateinit var timerBinder: TimerService.TimerBinder
    private var connected= false
    private val serviceConnection= object: ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            timerBinder= p1 as TimerService.TimerBinder
            timerBinder.setHandler(timerHandler)
            connected= true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            connected= false
        }
    }

    val timerHandler= Handler(Looper.getMainLooper()){
        tvTimer.text= it.what.toString()
        true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )
        tvTimer= findViewById(R.id.tvTimer)

        findViewById<Button>(R.id.startButton).setOnClickListener {
            if (connected) timerBinder.start(50)
        }

        findViewById<Button>(R.id.pauseButton).setOnClickListener {
            if(connected) timerBinder.pause()
        }
        
        findViewById<Button>(R.id.stopButton).setOnClickListener {
            if (connected) timerBinder.stop()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mnuPlay -> if (connected) timerBinder.start(50)
            R.id.mnuPause -> if(connected) timerBinder.pause()
            R.id.mnuStop -> if (connected) timerBinder.stop()
        }

        return super.onOptionsItemSelected(item)
    }
}