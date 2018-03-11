package kg.iceknight.grazygo.background.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import kg.iceknight.grazygo.background.service.helper.MockTimerJumpTask;
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
    Integer distance;
    AtomicInteger remainSteps;
    Integer variant;
    Integer delay;

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
        Log.d(LOG_TAG, "MockingService onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mockService.disableMockLocation();
        if (null != timer) {
            timer.cancel();
        }
        mockService = null;
        timer = null;
        Log.d(LOG_TAG, "MockingService: onDestroy");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(LOG_TAG, "MockingService: onUnbind");
        return true;
    }

    public void resumeOrStart() {
        Log.d(LOG_TAG, "MockingService: resumeOrStart. Variant = " + variant);
        switch (variant) {
            case 1: {
                TimerTask timerTask = new MockTimerJumpTask(mockService, delay, distance);
                Log.d(LOG_TAG, "Mocking Service delay = " + delay + " distance = " + distance * 50);
                Executors.newSingleThreadExecutor().execute(timerTask);
                break;
            }
            case 2: {
                timer = new Timer();
                TimerTask timerTask = new MockTimerTask(mockService, timer, remainSteps);
                Log.d(LOG_TAG, "Mocking Service remainSteps = " + remainSteps.toString());
                timer.schedule(timerTask, DEFAULT_DELAY_FOR_TIMER_TASK, DEFAULT_PERIOD_FOR_TIMER_TASK);
                break;
            }

        }

    }

    public void pause() {
        Log.d(LOG_TAG, "MockingService: pause");
        if (timer != null) {
            timer.cancel();
        }
    }

    public void reset() {
        mockService.disableMockLocation();
        pause();
        Log.d(LOG_TAG, "MockingService: resetParam");
        mockService = new MockService(this);
        remainSteps.set(distance);
    }

    public void setParam(Double distanceInKm, int variant, Integer delay) {
        Log.d(LOG_TAG, "MockingService: setParam: param = " + distanceInKm.toString());
        int steps = (int) (distanceInKm * 500) / JUMP_INTERVAL_METERS;
        distance = steps;
        remainSteps = new AtomicInteger(steps);
        this.delay = delay;
        this.variant = variant;
    }

}
