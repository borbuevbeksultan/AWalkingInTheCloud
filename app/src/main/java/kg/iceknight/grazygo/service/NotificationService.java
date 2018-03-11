 package kg.iceknight.grazygo.service;

 import android.app.Notification;
 import android.app.NotificationManager;
 import android.app.PendingIntent;
 import android.content.Context;
 import android.content.Intent;
 import android.graphics.BitmapFactory;
 import android.os.Build;
 import android.support.annotation.RequiresApi;
 import android.support.v4.app.NotificationCompat;
 import android.util.Log;

 import java.io.Serializable;

 import kg.iceknight.grazygo.MainActivity;
 import kg.iceknight.grazygo.R;
 import kg.iceknight.grazygo.service.daemon.NotificationControlDaemon;
 import kg.iceknight.grazygo.service.daemon.NotificationExitDaemon;
 import kg.iceknight.grazygo.service.daemon.NotificationResetDaemon;

 import static kg.iceknight.grazygo.common.Constants.LOG_TAG;
 import static kg.iceknight.grazygo.common.Constants.NOTIFICATION_ID;
 import static kg.iceknight.grazygo.common.Constants.PAUSE_REQUEST_CODE;
 import static kg.iceknight.grazygo.common.Constants.PLAY_REQUEST_CODE;

public class NotificationService implements Serializable {

    private final NotificationManager mNotificationManager;
    private final MainActivity context;
    private int controlIcon;
    private String infoText;
    private int requestCode;
    private int resetIcon;

    public NotificationService(MainActivity context) {
        this.context = context;
        this.controlIcon = R.drawable.ic_play_arrow_white_24dp;
        this.resetIcon = R.drawable.ic_power_settings_new_white_24dp;
        this.infoText = "Play";
        this.requestCode = PLAY_REQUEST_CODE;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification() {
        try {

            Log.d(LOG_TAG, "MockingService: showNotification: requestCode = " + this.requestCode);

            Intent runDaemonIntent = new Intent(context, NotificationControlDaemon.class);
            Intent resetDaemonIntent = new Intent(context, NotificationResetDaemon.class);
            Intent exitDaemonIntent = new Intent(context, NotificationExitDaemon.class);

            PendingIntent pendingIntentControl = PendingIntent.getService(context, 10, runDaemonIntent, 0);
            PendingIntent pendingIntentReset = PendingIntent.getService(context, 10, resetDaemonIntent, 0);
            PendingIntent pendingIntentExit = PendingIntent.getService(context, 10, exitDaemonIntent, 0);


            ServiceCollection.setControlStatus(requestCode);
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.play)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                    R.mipmap.play))
                            .setContentTitle("CrazyGo. Start")
                            .setContentText("Press buttons")
                            .addAction(controlIcon, infoText, pendingIntentControl)
                            .addAction(resetIcon, "Reset", pendingIntentReset)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                                    .setShowCancelButton(true)
                                    .setCancelButtonIntent(pendingIntentExit)
                                    .setShowActionsInCompactView(0, 1))
                            .setVisibility(Notification.VISIBILITY_PUBLIC);

            Intent resultIntent = new Intent();
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public NotificationService config(int requestCode) {
        Log.d(LOG_TAG, "MockingService: config: requestCode = " + requestCode);
        if (requestCode == PLAY_REQUEST_CODE) {

            this.requestCode = PAUSE_REQUEST_CODE;
            this.controlIcon = R.drawable.ic_pause_white_24dp;
            this.infoText = "Pause";
        }

        if (requestCode == PAUSE_REQUEST_CODE) {
            this.requestCode = PLAY_REQUEST_CODE;
            this.controlIcon = R.drawable.ic_play_arrow_white_24dp;
            this.infoText = "Play";
        }

        return this;

    }

    public void cancelNotification(int id) {
        mNotificationManager.cancel(id);
    }

}
