package com.keelim.mygrade.ui.center

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import androidx.core.graphics.drawable.IconCompat
import com.keelim.mygrade.databinding.ActivityCenterBinding
import com.keelim.mygrade.notification.NotificationBuilder
import com.keelim.mygrade.work.MainWorker
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CenterActivity : AppCompatActivity() {
    @Inject
    lateinit var notificationBuilder: NotificationBuilder

    private val viewModel: CenterViewModel by viewModels()
    private val binding: ActivityCenterBinding by lazy {
        ActivityCenterBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        MainWorker.enqueueWork(this)
        sendNotification()
        handleIntent()
    }

    private fun sendNotification(){
        val replyLabel = "Enter your reply here"
        val remoteInput = RemoteInput.Builder(KEY_TEXT_REPLY)
            .setLabel(replyLabel)
            .build()

        val resultIntent = Intent(this, CenterActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(
            this,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val icon = IconCompat.createWithResource(this@CenterActivity,
            android.R.drawable.ic_dialog_info
        )

        val replyAction = NotificationCompat.Action.Builder(
            icon,
            "Reply", resultPendingIntent)
            .addRemoteInput(remoteInput)
            .build()

        notificationBuilder.showNotification(replyAction)
    }

    private fun handleIntent(){
        val remoteInput = RemoteInput.getResultsFromIntent(intent)
        remoteInput?.let{
            val inputString = it.getCharSequence(KEY_TEXT_REPLY).toString()
            viewModel.saveHistory(inputString)
        }
    }

    companion object{
        const val notificationId = 101
        const val KEY_TEXT_REPLY = "key_text_reply"
    }
}