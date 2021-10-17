package com.example.boundservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Binder

import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Log
import androidx.core.app.NotificationCompat

class BoundServices : Service() {
    private val myBinder = MyBinder()

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer.create(this, R.raw.file_see)
        Log.d("AAA", "onCreate")
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.d("AAA", "onBind")
        return myBinder
    }

    inner class MyBinder() : Binder() {
        fun boundService(): BoundServices {
            return this@BoundServices
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            val bundle = it.extras
            if (bundle != null) {
                val music = bundle.get("Music_Play") as Music
                createNotification(music)
            }
        }
        return START_NOT_STICKY
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("AAA", "onUnbind")
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("AAA", "onDestroy")
        if (mediaPlayer == null) {
            mediaPlayer!!.release()
        }
    }

     fun isPlaying(): Boolean{
        return mediaPlayer!!.isPlaying
    }

     fun startMusic() {
        if (mediaPlayer != null){
            mediaPlayer?.start()
        }
    }

    fun onPlayMusic() {
        if (mediaPlayer != null){
            mediaPlayer?.pause()
        }
    }

    fun getDuration(): Int {
        return mediaPlayer!!.duration
    }

    fun getCursor(): Int {
        return mediaPlayer!!.currentPosition
    }

    fun seekToPos(pos: Int){
        mediaPlayer!!.seekTo(pos)
    }

    private fun createNotification(music: Music) {
        val mediaSession = MediaSessionCompat(this, "Music")
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, 0)
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.seeyouagain)
        val notificationChannel =
            NotificationChannel(MUSIC_ID, "PlayMusic", NotificationManager.IMPORTANCE_DEFAULT)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(notificationChannel)
        val builder = NotificationCompat.Builder(this, MUSIC_ID)
            .setSmallIcon(R.drawable.ic_music)
            .setSubText("Wiz Khalifa && Charlie Puth")
            .setLargeIcon(bitmap)
            .setContentTitle(music.title)
            .setContentText(music.single)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_back, "Back", null)
            .addAction(R.drawable.ic_pause, "Pause", null)
            .addAction(R.drawable.ic_next, "Next", null)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2)
                    .setMediaSession(mediaSession.sessionToken)
            )
        startForeground(2, builder.build())
    }

    companion object {
        private const val MUSIC_ID = "DCV"
        private var mediaPlayer: MediaPlayer? = null
    }
}