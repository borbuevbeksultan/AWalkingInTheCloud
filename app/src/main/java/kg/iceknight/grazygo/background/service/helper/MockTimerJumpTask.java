package kg.iceknight.grazygo.background.service.helper;

import android.annotation.SuppressLint;
import android.icu.util.TimeUnit;
import android.util.EventLogTags;
import android.util.Log;
import android.util.TimingLogger;

import java.sql.Time;
import java.util.TimerTask;

import kg.iceknight.grazygo.common.Constants;
import kg.iceknight.grazygo.service.MockService;
import kg.iceknight.grazygo.service.ServiceCollection;


public class MockTimerJumpTask extends TimerTask {

    private MockService mockService;
    private Integer delay;
    private Integer distance;

    public MockTimerJumpTask(MockService mockService, Integer delay, Integer distance) {
        this.mockService = mockService;
        this.delay = delay;
        this.distance = distance;
    }

    @SuppressLint("NewApi")
    @Override
    public void run() {
        long startTime = System.currentTimeMillis();
        mockService.jump(distance * 50);
        Log.d(Constants.LOG_TAG, "Task jump distance = " + distance * 50);
        try {
            Thread.sleep(delay * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        Log.d(Constants.LOG_TAG, String.valueOf(endTime - startTime));
        ServiceCollection.getMainActivity().runOnUiThread(() -> ServiceCollection.getMockHelperService().reset());
        ServiceCollection.getNotificationService().config(Constants.PAUSE_REQUEST_CODE).showNotification();
        ServiceCollection.getVibrator().vibrate(50);
    }
}
