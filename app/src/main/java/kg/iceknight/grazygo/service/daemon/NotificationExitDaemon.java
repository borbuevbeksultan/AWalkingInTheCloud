package kg.iceknight.grazygo.service.daemon;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import kg.iceknight.grazygo.service.ServiceCollection;

import static kg.iceknight.grazygo.common.Constants.LOG;

public class NotificationExitDaemon extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(LOG, "NotificationExitDaemon onCreate");
        super.onCreate();
    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG, "Notification Daemon onStartCommand. StartId = " + startId + " hashcode " + intent.hashCode());
        ServiceCollection.getNotificationService().config(ServiceCollection.getControlStatus()).showNotification();
        ServiceCollection.getMainActivity().finish();
        stopSelf(startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        Log.d(LOG, "NotificationControlDaemon onDestroy");
        super.onDestroy();
    }
}
