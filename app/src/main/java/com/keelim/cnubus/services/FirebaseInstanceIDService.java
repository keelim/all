package com.keelim.cnubus.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.keelim.cnubus.R;
import com.keelim.cnubus.activities.MainActivity;

public class FirebaseInstanceIDService extends FirebaseMessagingService {


    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("Firebase", "FirebaseInstanceIDService : " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // 메시지 수신 시 실행되는 메소드
        if (remoteMessage != null && remoteMessage.getData().size() > 0) {
            sendNotification(remoteMessage);
        }
    }


    /**
     * 메시지가 수신되었을 때 실행되는 메소드
     **/
    private void sendNotification(RemoteMessage remoteMessage) {

        String message = remoteMessage.getData().get("data");
        // 수신되는 푸시 메시지

        int messageDivider = message.indexOf("|");
        // 구분자를 통해 어떤 종류의 알람인지를 구별합니다.

        String pushType = message.substring(messageDivider + 1); // 구분자 뒤에 나오는 메시지


        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.putExtra("pushType", pushType);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channel = "채널";
            String channel_nm = "채널 이름"; // 앱 설정에서 알림 이름으로 뜸.

            NotificationManager notichannel = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channelMessage = new NotificationChannel(channel, channel_nm,
                    android.app.NotificationManager.IMPORTANCE_DEFAULT);
            channelMessage.setDescription("채널에 대한 설명입니다.");
            channelMessage.enableLights(true);
            channelMessage.enableVibration(true);
            channelMessage.setShowBadge(false);
            channelMessage.setVibrationPattern(new long[]{100, 200, 100, 200});
            notichannel.createNotificationChannel(channelMessage);

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channel)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("알림이 도착을 했습니다. ")
                            .setContentText(message.substring(0, messageDivider))
                            .setChannelId(channel)
                            .setAutoCancel(true)
                            .setColor(Color.parseColor("#0ec874"))
                            .setContentIntent(pendingIntent)
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(9999, notificationBuilder.build());


        } else {
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, "")
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("푸시 타이틀")
                            .setContentText(message)
                            .setAutoCancel(true)
                            .setColor(Color.parseColor("#0ec874")) // 푸시 색상
                            .setContentIntent(pendingIntent)
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            notificationManager.notify(9999, notificationBuilder.build());

        }
    }
}


