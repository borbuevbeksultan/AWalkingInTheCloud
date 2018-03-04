package kg.iceknight.grazygo.service;

import android.util.Log;

import kg.iceknight.grazygo.background.service.MockingService;

import static kg.iceknight.grazygo.common.Constants.LOG_TAG;
import static kg.iceknight.grazygo.common.Constants.PAUSE_REQUEST_CODE;
import static kg.iceknight.grazygo.common.Constants.PLAY_REQUEST_CODE;

public class MockHelperService {

    private MockingService mockingService;

    public MockHelperService(MockingService mockingService) {
        this.mockingService = mockingService;
    }

    public void processRequest(int requestCode) {
        Log.d(LOG_TAG, "processRequest " + requestCode);
        switch (requestCode) {
            case PLAY_REQUEST_CODE : {
                mockingService.resumeOrStart();
                break;
            }

            case PAUSE_REQUEST_CODE : {
                mockingService.pause();
                break;
            }

        }
    }

    public void reset() {
        mockingService.reset();
    }
}
