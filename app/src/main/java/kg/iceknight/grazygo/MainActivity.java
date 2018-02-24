package kg.iceknight.grazygo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import kg.iceknight.grazygo.handler.MainButtonHandler;
import kg.iceknight.grazygo.handler.NotificationButtonHandler;
import kg.iceknight.grazygo.service.NotificationService;

public class MainActivity extends AppCompatActivity {

    private Button mainButton;
    private Button notificationButton;
    private NotificationManager mNotificationManager;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
    }

    private void initializeUI() {
        mainButton = findViewById(R.id.mainButton);
        mainButton.setOnClickListener(new MainButtonHandler(this));
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        notificationButton = findViewById(R.id.notificationButton);
//        notificationButton.setOnClickListener(new NotificationButtonHandler(this, new NotificationService(this)));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void show(View view) {
        try {

            Intent intent = new Intent(this, BroadcastReceiver.class);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.play)
                            .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                                    R.mipmap.play))
                            .setContentTitle("CrazyGo. Start")
                            .setContentText("Нажмите на для запуска")
                            .addAction(R.drawable.ic_play_arrow_white_24dp, "Play", null)
                            .addAction(R.drawable.ic_clear_white_24dp, "Exit", null)
                            .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                                    .setShowActionsInCompactView(0, 1))
                            .setVisibility(Notification.VISIBILITY_PUBLIC);
            Intent resultIntent = new Intent(this, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            mBuilder.setAutoCancel(true);
            mNotificationManager.notify(77, mBuilder.build());

        } catch (Exception e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
