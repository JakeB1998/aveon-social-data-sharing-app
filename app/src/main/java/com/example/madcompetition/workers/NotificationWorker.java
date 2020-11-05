package com.example.madcompetition.workers;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.madcompetition.activties.ActivityMessagingInterface;
import com.example.madcompetition.R;

public class NotificationWorker extends Worker {

    private final int  NOTIF_ID = 3661;
    public static final String TITLE_KEY = "Title";
    public static final String CONTENT_TEXT_KEY = "ContentText";
    private Context context;
    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {

        Data data = null;
        if (getInputData() != null) {
            data = getInputData();
            String CHANNEL_ID = "12342";

            final String TITLE = data.getString(NotificationWorker.TITLE_KEY);
            final String CONTENT_TEXT = data.getString(NotificationWorker.CONTENT_TEXT_KEY);
            final String BIG_TEXT = "Message Here";

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "Notification";
                String description = "To notify users";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
                channel.setDescription(description);

                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);

                Intent intent = new Intent(context, ActivityMessagingInterface.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

                // set data to notification

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(TITLE)
                        .setContentText(CONTENT_TEXT) // "Much longer text that cannot fit one line..."
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(BIG_TEXT))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                 .setContentIntent(pendingIntent)
                       .setAutoCancel(true);

                Notification n = builder.build();

                notificationManager.notify(this.NOTIF_ID, n);
                return Result.success();
            }


        }

         return Result.failure();



    }
}
