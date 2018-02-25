package kg.iceknight.grazygo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;

import kg.iceknight.grazygo.handler.MainButtonHandler;
import kg.iceknight.grazygo.handler.NotificationButtonHandler;
import kg.iceknight.grazygo.service.NotificationService;

import static kg.iceknight.grazygo.common.Constants.EXIT_REQUEST_CODE;
import static kg.iceknight.grazygo.common.Constants.NOTIFICATION_ID;
import static kg.iceknight.grazygo.common.Constants.PAUSE_REQUEST_CODE;
import static kg.iceknight.grazygo.common.Constants.PLAY_REQUEST_CODE;

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
        notificationButton.setOnClickListener(new NotificationButtonHandler(this, notificationService));
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EXIT_REQUEST_CODE) {
            closeApplication();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
        notificationService.config(requestCode).showNotification();
    }

    public void closeApplication() {
        notificationService.cancelNotification(NOTIFICATION_ID);
        System.exit(0);
    }
}
