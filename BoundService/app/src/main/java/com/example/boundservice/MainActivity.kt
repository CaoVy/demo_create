package com.example.boundservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private var boundService = BoundServices()
    private var mBound: Boolean = false

    private val boundContextConnect = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName?, iBinder: IBinder?) {
            val binder = iBinder as BoundServices.MyBinder
            boundService = binder.boundService()
            mBound = true
        }

        override fun onServiceDisconnected(componentName: ComponentName?) {
            mBound = false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(boundContextConnect)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imagePlay.setOnClickListener {
            playOrPause()
            startMusic()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser){
                    boundService.seekToPos(progress)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
    }

    private fun playOrPause() {
        val handler = Handler()
        handler.postDelayed({
            if (mBound){
//                seekBar.max = boundService.getDuration()
                if (boundService.isPlaying()){
                    boundService.onPlayMusic()
                    imagePlay.setImageResource(R.drawable.ic_play)
                    onSeekBar()
                }else{
                    boundService.startMusic()
                    imagePlay.setImageResource(R.drawable.ic_pause)
//                    try {
//                        seekBar.progress = boundService.getCursor()
//                    }catch (e:Exception){
//                        seekBar.progress = 0
//                    }
                }
            }
        }, 500)
    }


    private fun startMusic() {
        val music =
            Music("ZingMp3", "Wiz Khalifa && Charlie Puth", R.drawable.seeyouagain, R.raw.file_see)
        val intent = Intent(this, BoundServices::class.java)
        val bundle = Bundle()
        bundle.putSerializable("Music_Play", music)
        intent.putExtras(bundle)
        startService(intent)
        bindService(intent, boundContextConnect, Context.BIND_AUTO_CREATE)
    }

    private fun onSeekBar(){
        seekBar.max = boundService.getDuration()
        val handler = Handler()
        handler.postDelayed({
            try {
                seekBar.progress = boundService.getCursor()
                Thread.sleep(1000)
            }catch (e: Exception){
                seekBar.progress = 0
            }
        }, 500)

    }

}

