package kg.iceknight.grazygo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import kg.iceknight.grazygo.MainActivity;
import kg.iceknight.grazygo.R;

public class NotificationService {

    private final NotificationManager mNotificationManager;
    private final Context context;

    public NotificationService(Context context) {
        this.context = context;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification() {
        try {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle("CrazyGo. Start")
                            .setContentText("Нажмите на для запуска")
                            .addAction(R.drawable.ic_launcher_background, "suka", null)
                            .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                                    .setShowActionsInCompactView(1))
                            .setVisibility(Notification.VISIBILITY_PUBLIC);
            Intent resultIntent = new Intent(context, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setAutoCancel(true);
            mNotificationManager.notify(77, mBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
