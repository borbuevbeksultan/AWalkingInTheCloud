package kg.iceknight.grazygo.background.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import kg.iceknight.grazygo.background.service.helper.MockTimerTask;
import kg.iceknight.grazygo.background.service.helper.ServiceBinder;
import kg.iceknight.grazygo.service.MockService;

import static kg.iceknight.grazygo.common.Constants.DEFAULT_DELAY_FOR_TIMER_TASK;
import static kg.iceknight.grazygo.common.Constants.DEFAULT_PERIOD_FOR_TIMER_TASK;
import static kg.iceknight.grazygo.common.Constants.JUMP_INTERVAL_METERS;
import static kg.iceknight.grazygo.common.Constants.LOG_TAG;

public class MockingService extends Service {

    private MockService mockService;
    private Timer timer;
    AtomicInteger remainSteps;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new ServiceBinder(this);
    }

    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "MockingService: onCreate");
        super.onCreate();
        mockService = new MockService(this);
        remainSteps = new AtomicInteger(0);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mockService.disableMockLocation();
        timer.cancel();
        mockService = null;
        timer = null;
        Log.d(LOG_TAG, "MockingService: onDestroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(LOG_TAG, "MockingService: onUnbind");
        return super.onUnbind(intent);
    }

    public void resumeOrStart() {
        Log.d(LOG_TAG, "MockingService: resumeOrStart");
        timer = new Timer();
        TimerTask timerTask = new MockTimerTask(mockService, timer, remainSteps);
        timer.schedule(timerTask, DEFAULT_DELAY_FOR_TIMER_TASK, DEFAULT_PERIOD_FOR_TIMER_TASK);
    }

    public void pause() {
        Log.d(LOG_TAG, "MockingService: pause");
        timer.cancel();
    }

    public void reset() {
        mockService = new MockService(this);
    }

    public void setParam(Double distanceInKm) {
        Log.d(LOG_TAG, "MockingService: setParam: param = " + distanceInKm.toString());
        int meters = (int) (distanceInKm * 1000);
        remainSteps = new AtomicInteger(meters / JUMP_INTERVAL_METERS);
    }

}
