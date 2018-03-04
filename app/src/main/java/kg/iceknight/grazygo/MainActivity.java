package kg.iceknight.grazygo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import kg.iceknight.grazygo.handler.MainButtonHandler;
import kg.iceknight.grazygo.handler.NotificationButtonHandler;
import kg.iceknight.grazygo.service.NotificationService;
import kg.iceknight.grazygo.service.ServiceCollection;

import static kg.iceknight.grazygo.common.Constants.EXIT_REQUEST_CODE;
import static kg.iceknight.grazygo.common.Constants.LOG;
import static kg.iceknight.grazygo.common.Constants.NOTIFICATION_ID;

public class MainActivity extends AppCompatActivity {

    private Button mainButton;
    private Button notificationButton;
    private NotificationService notificationService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
    }

    private void initializeUI() {
        mainButton = findViewById(R.id.mainButton);
        mainButton.setOnClickListener(new MainButtonHandler(this));
        notificationButton = findViewById(R.id.notificationButton);
        this.notificationService = new NotificationService(this);
        ServiceCollection.setNotificationService(notificationService);
        notificationButton.setOnClickListener(new NotificationButtonHandler(notificationService));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(LOG, "onActivityResult");
        if (requestCode == EXIT_REQUEST_CODE) {
            closeApplication();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        notificationService.config(requestCode).showNotification();
    }

    public void closeApplication() {
        notificationService.cancelNotification(NOTIFICATION_ID);
        finish();
    }
}
