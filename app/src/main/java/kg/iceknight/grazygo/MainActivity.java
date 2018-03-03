package kg.iceknight.grazygo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import kg.iceknight.grazygo.background.service.MockingService;
import kg.iceknight.grazygo.background.service.helper.ServiceBinder;
import kg.iceknight.grazygo.handler.MainButtonHandler;
import kg.iceknight.grazygo.handler.NotificationButtonHandler;
import kg.iceknight.grazygo.service.NotificationService;

import static kg.iceknight.grazygo.common.Constants.EXIT_REQUEST_CODE;
import static kg.iceknight.grazygo.common.Constants.NOTIFICATION_ID;

public class MainActivity extends AppCompatActivity {

    private Button mainButton;
    private Button notificationButton;
    private NotificationService notificationService;
    private Intent intent;
    private MockingService mockingService;
    private ServiceConnection serviceConnection;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
    }

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
        intent = new Intent(this, MockingService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mockingService = (MockingService) ((ServiceBinder) iBinder).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) { }
        };

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

    public void startService(View view) {
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    public void resumeService(View view) {
        if (null != mockingService) {
            mockingService.resume();
        }
    }

    public void pauseService(View view) {
        if (null != mockingService) {
            mockingService.pause();
        }
    }

    public void resetService(View view) {
        if (null != mockingService) {
            mockingService.reset();
        }
    }
}
