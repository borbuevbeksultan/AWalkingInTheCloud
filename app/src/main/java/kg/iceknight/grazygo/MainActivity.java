package kg.iceknight.grazygo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import kg.iceknight.grazygo.background.service.MockingService;
import kg.iceknight.grazygo.background.service.helper.ServiceBinder;
import kg.iceknight.grazygo.service.MockHelperService;
import kg.iceknight.grazygo.service.NotificationService;
import kg.iceknight.grazygo.service.ServiceCollection;

import static kg.iceknight.grazygo.common.Constants.LOG_TAG;

public class MainActivity extends AppCompatActivity {

    private NotificationService notificationService;
    private MockingService mockingService;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(serviceConnection);
        Log.d(LOG_TAG, "MainActivity onDestroy");
    }

    private void initializeUI() {
        ServiceCollection.setMainActivity(this);
        this.notificationService = new NotificationService(this);
        ServiceCollection.setNotificationService(notificationService);
        serviceConnection = new DefaultServiceConnection();
        Intent intent = new Intent(this, MockingService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private class DefaultServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mockingService = (MockingService) ((ServiceBinder) iBinder).getService();
            ServiceCollection.setMockHelperService(new MockHelperService(mockingService));
            Log.d(LOG_TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(LOG_TAG, "onServiceDisconnected");
        }
    }

}
