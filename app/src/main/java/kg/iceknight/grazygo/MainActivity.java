package kg.iceknight.grazygo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kg.iceknight.grazygo.background.service.MockingService;
import kg.iceknight.grazygo.background.service.helper.ServiceBinder;
import kg.iceknight.grazygo.handler.MainButtonHandler;
import kg.iceknight.grazygo.handler.NotificationButtonHandler;
import kg.iceknight.grazygo.service.MockHelperService;
import kg.iceknight.grazygo.service.NotificationService;
import kg.iceknight.grazygo.service.ServiceCollection;

import static kg.iceknight.grazygo.common.Constants.EXIT_REQUEST_CODE;
import static kg.iceknight.grazygo.common.Constants.LOG_TAG;
import static kg.iceknight.grazygo.common.Constants.NOTIFICATION_ID;

public class MainActivity extends AppCompatActivity {

    private NotificationService notificationService;
    private MockingService mockingService;
    private ServiceConnection serviceConnection;
    private MockHelperService mockHelperService;
    private boolean isServiceBound;
    private EditText editText;
    private double distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isServiceBound = false;
        unbindService(serviceConnection);
        Log.d(LOG_TAG, "onDestroy");
    }

    private void initializeUI() {
        Button mainButton = findViewById(R.id.mainButton);
        mainButton.setOnClickListener(new MainButtonHandler(this));

        Button notificationButton = findViewById(R.id.notificationButton);
        this.notificationService = new NotificationService(this);
        ServiceCollection.setNotificationService(notificationService);
        notificationButton.setOnClickListener(new NotificationButtonHandler(notificationService));
        editText = findViewById(R.id.distance);

        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                mockingService = (MockingService) ((ServiceBinder) iBinder).getService();
                isServiceBound = true;
                Log.d(LOG_TAG, "onServiceConnected");
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                isServiceBound = false;
                Log.d(LOG_TAG, "onServiceDisconnected");
            }

        };

        Intent intent = new Intent(this, MockingService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(LOG_TAG, "Main activity: onActivityResult. RequestData = " + requestCode  + "; resultCode = " + resultCode);

        if (requestCode == EXIT_REQUEST_CODE) {
            notificationService.cancelNotification(NOTIFICATION_ID);
            finish();
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
        notificationService.config(requestCode).showNotification();
        if (isServiceBound) { mockHelperService.processRequest(requestCode); }
    }

    public void start(View view) {
        Log.d(LOG_TAG, "start");
        if ((this.mockHelperService == null) && (mockingService != null)) {
            this.mockHelperService = new MockHelperService(mockingService);
            Log.d(LOG_TAG, "mockHelperService initialized");
        }

        if (isServiceBound) {
            Log.d(LOG_TAG, "Param setting");
            distance = Double.parseDouble(editText.getText().toString());
            mockingService.setParam(distance);
        }

    }

}
