package kg.iceknight.grazygo.background.service.helper;

import android.annotation.SuppressLint;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import kg.iceknight.grazygo.common.Constants;
import kg.iceknight.grazygo.service.MockService;
import kg.iceknight.grazygo.service.ServiceCollection;

public class MockTimerTask extends TimerTask {

    private MockService mockService;
    private Timer executor;
    private AtomicInteger iterations;

    public MockTimerTask(MockService mockService, Timer executor, AtomicInteger iterations) {
        this.mockService = mockService;
        this.executor = executor;
        this.iterations = iterations;
    }

    @SuppressLint("NewApi")
    @Override
    public void run() {

        if (iterations.get() <= 0) {
            ServiceCollection.getNotificationService().config(Constants.PAUSE_REQUEST_CODE).showNotification();
            ServiceCollection.getMainActivity().runOnUiThread(() -> ServiceCollection.getMockHelperService().reset());
            ServiceCollection.getVibrator().vibrate(50);
            executor.cancel();
            return;
        }
        Log.d(Constants.LOG_TAG, "TimerTask remain = " + iterations.toString());
        mockService.walk();
        iterations.decrementAndGet();

    }

}
