package kg.iceknight.grazygo.handler;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;

import kg.iceknight.grazygo.service.ServiceCollection;

import static kg.iceknight.grazygo.common.Constants.LOG_TAG;
import static kg.iceknight.grazygo.common.Constants.NOTIFICATION_ID;

public class ExitButtonHandler implements View.OnClickListener{

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        ServiceCollection.getNotificationService().cancelNotification(NOTIFICATION_ID);
        if (ServiceCollection.getMainActivity() == null) {
            Log.d(LOG_TAG, "activity is NULL");
        } else {
            ServiceCollection.getMainActivity().finishAffinity();
        }
    }
}
