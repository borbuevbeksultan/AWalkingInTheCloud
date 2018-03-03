package kg.iceknight.grazygo.background.service.helper;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

import kg.iceknight.grazygo.service.MockService;

public class MockTimerTask extends TimerTask {

    private MockService mockService;
    private Timer executor;
    private AtomicInteger iterations;

    public MockTimerTask(MockService mockService, Timer executor, AtomicInteger iterations) {
        this.mockService = mockService;
        this.executor = executor;
        this.iterations = iterations;
    }

    @Override
    public void run() {

        if (iterations.get() <= 0) {
            executor.cancel();
        }

        mockService.walk();
        iterations.decrementAndGet();

    }

}
