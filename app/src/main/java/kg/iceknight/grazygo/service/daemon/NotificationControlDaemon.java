package kg.iceknight.grazygo.service.daemon;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import kg.iceknight.grazygo.service.ServiceCollection;

import static kg.iceknight.grazygo.common.Constants.LOG;

public class NotificationControlDaemon extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(LOG, "NotificationControlDaemon onCreate");
        super.onCreate();
    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG, "Notification Daemon onStartCommand. StartId = " + startId + " hashcode " + intent.hashCode());
        ServiceCollection.getMockHelperService().processRequest(ServiceCollection.getControlStatus());
        ServiceCollection.getVibrator().vibrate(50);
        ServiceCollection.getMainButtonHandler().set();
        stopSelf(startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(LOG, "NotificationControlDaemon onDestroy");
        super.onDestroy();
    }
}
