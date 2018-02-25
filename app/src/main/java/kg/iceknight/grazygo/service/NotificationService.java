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

import kg.iceknight.grazygo.MainActivity;
import kg.iceknight.grazygo.R;

import static kg.iceknight.grazygo.common.Constants.EXIT_REQUEST_CODE;
import static kg.iceknight.grazygo.common.Constants.NOTIFICATION_ID;
import static kg.iceknight.grazygo.common.Constants.PAUSE_REQUEST_CODE;
import static kg.iceknight.grazygo.common.Constants.PLAY_REQUEST_CODE;

public class NotificationService {

    private final NotificationManager mNotificationManager;
    private final MainActivity context;
    private int controlIcon;
    private String infoText;
    private int requestCode;

    public NotificationService(MainActivity context) {
        this.context = context;
        this.controlIcon = R.drawable.ic_play_arrow_white_24dp;
        this.infoText = "Play";
        this.requestCode = PLAY_REQUEST_CODE;
        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification() {
        try {

            PendingIntent pendingIntentControl = context.createPendingResult(requestCode, new Intent(), 0);
            PendingIntent pendingIntentExit = context.createPendingResult(EXIT_REQUEST_CODE, new Intent(), PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.play)
                            .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                    R.mipmap.play))
                            .setContentTitle("CrazyGo. Start")
                            .setContentText("Press buttons")
                            .addAction(controlIcon, infoText, pendingIntentControl)
                            .addAction(R.drawable.ic_clear_white_24dp, "Exit", pendingIntentExit)
                            .setStyle(new android.support.v4.media.app.NotificationCompat.DecoratedMediaCustomViewStyle().setShowCancelButton(true))
                            .setVisibility(Notification.VISIBILITY_PUBLIC);

            Intent resultIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context,
                            0,
                            resultIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setAutoCancel(true);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public NotificationService config(int requestCode) {

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
