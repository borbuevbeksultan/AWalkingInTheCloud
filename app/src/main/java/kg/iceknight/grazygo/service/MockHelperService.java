package kg.iceknight.grazygo.service;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import kg.iceknight.grazygo.BuildConfig;
import kg.iceknight.grazygo.R;
import kg.iceknight.grazygo.background.service.MockingService;

import static kg.iceknight.grazygo.common.Constants.LOG_TAG;
import static kg.iceknight.grazygo.common.Constants.PAUSE_REQUEST_CODE;
import static kg.iceknight.grazygo.common.Constants.PLAY_REQUEST_CODE;

public class MockHelperService {

    private MockingService mockingService;

    public MockHelperService(MockingService mockingService) {
        this.mockingService = mockingService;
    }

    @SuppressLint("NewApi")
    public void processRequest(int requestCode) {
        ServiceCollection.getNotificationService().config(requestCode).showNotification();
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

    @SuppressLint("NewApi")
    public void reset() {
        ServiceCollection.getMainButtonHandler().reset();
        mockingService.reset();
    }

    public void set(Double distance, int variant, Integer delay) {
        Log.d(LOG_TAG, "MockingHelperService Start() distance = " + distance);
        mockingService.setParam(distance, variant, delay);
    }

    public static boolean isMockSettingsON(Context context) {
        boolean isMockLocation;
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                AppOpsManager opsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
                isMockLocation = (opsManager.checkOp(AppOpsManager.OPSTR_MOCK_LOCATION, android.os.Process.myUid(), BuildConfig.APPLICATION_ID)== AppOpsManager.MODE_ALLOWED);
            } else {
                isMockLocation = !Settings.Secure.getString(context.getContentResolver(), "mock_location").equals("0");
            }
        } catch (Exception e) {
            return false;
        }
        return isMockLocation;
    }
}
