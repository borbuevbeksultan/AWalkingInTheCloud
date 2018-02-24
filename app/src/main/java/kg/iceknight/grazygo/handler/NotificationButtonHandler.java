package kg.iceknight.grazygo.handler;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;

import kg.iceknight.grazygo.service.NotificationService;

public class NotificationButtonHandler implements View.OnClickListener {

    private Context context;
    private NotificationService notificationService;

    public NotificationButtonHandler(Context context, NotificationService notificationService) {
        this.context = context;
        this.notificationService = notificationService;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        notificationService.showNotification();
    }
}
