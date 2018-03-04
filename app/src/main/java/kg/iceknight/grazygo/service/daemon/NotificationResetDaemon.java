package kg.iceknight.grazygo.service.daemon;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import kg.iceknight.grazygo.common.Constants;
import kg.iceknight.grazygo.service.ServiceCollection;

public class NotificationResetDaemon extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("NewApi")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ServiceCollection.getNotificationService().config(Constants.PAUSE_REQUEST_CODE).showNotification();
        ServiceCollection.getMockHelperService().reset();
        stopSelf(startId);
        return super.onStartCommand(intent, flags, startId);
    }
}
