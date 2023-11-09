package edu.temple.myapplication

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.widget.Button
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    private lateinit var timerBinder: TimerService.TimerBinder
    private var connected= false
    private val serviceConnection= object: ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            timerBinder= p1 as TimerService.TimerBinder
            connected= true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            connected= false
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindService(
            Intent(this, TimerService::class.java),
            serviceConnection,
            BIND_AUTO_CREATE
        )



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
}