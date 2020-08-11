package com.keelim.nandadiagnosis.services;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.keelim.nandadiagnosis.model.interfaces.APIService;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class DownloadNotiService extends IntentService {
    public static final String PROGRESS_UPDATE = "progress_update";
    private NotificationCompat.Builder notificationBuilder;
    private NotificationManager notificationManager;

    public DownloadNotiService() {
        super("downloadService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("downlaod", "파일 다운로드", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.enableVibration(false);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationBuilder = new NotificationCompat.Builder(this, "download")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setContentTitle("다운로드")
                .setContentText("다운로드중")
                .setDefaults(0)
                .setAutoCancel(true);

        notificationManager.notify(0, notificationBuilder.build());

        Call<ResponseBody> request  = APIService.getInstance().apiService.downloadFile("");


    }
}
