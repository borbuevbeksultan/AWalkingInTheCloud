package kg.iceknight.grazygo.handler;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Objects;

import kg.iceknight.grazygo.MainActivity;
import kg.iceknight.grazygo.R;
import kg.iceknight.grazygo.common.Constants;
import kg.iceknight.grazygo.service.NotificationService;
import kg.iceknight.grazygo.service.ServiceCollection;

public class MainButtonHandler implements View.OnClickListener {

    private Button button;
    private Button setButton;
    private Button resetButton;
    private NotificationService notificationService;
    private MainActivity context;
    private String buttonStatus = "ready";

    public MainButtonHandler(Button button,
                             MainActivity context,
                             NotificationService notificationService,
                             Button setBtn,
                             Button resetBtn) {
        this.button = button;
        this.context = context;
        this.notificationService = notificationService;
        this.setButton = setBtn;
        this.resetButton = resetBtn;
    }

    @SuppressLint("NewApi")
    @Override
    public void onClick(View view) {
        if (buttonStatus.equals("ready")) {
            set();
            Log.d(Constants.LOG_TAG, "Main Button onClick distance = " + context.distance.doubleValue());
            notificationService.showNotification();
            ServiceCollection.getMockHelperService().set(context.distance.doubleValue(), context.variant, context.delay);
        }

    }

    @SuppressLint("NewApi")
    public void reset() {
        setButton.setText("Set");
        resetButton.setText("Reset");
        setButton.setBackground(context
                .getResources()
                .getDrawable(R.drawable.setbutton));
        resetButton.setBackground(context
                .getResources()
                .getDrawable(R.drawable.resetbutton));
        context.isWorking = false;
        buttonStatus = "ready";
        button.setBackground(context.getResources().getDrawable(R.drawable.mainbuttonready));
        button.setText("Ready");
    }

    @SuppressLint("NewApi")
    public void set() {
        context.isWorking = true;
        setButton.setBackground(context.getResources().getDrawable(R.drawable.mainbuttonclicked));
        resetButton.setBackground(context.getResources().getDrawable(R.drawable.mainbuttonclicked));
        setButton.setText("Not Available");
        resetButton.setText("Not Available");
        buttonStatus = "working";
        button.setBackground(context.getResources().getDrawable(R.drawable.mainbuttonclicked));
        button.setText("Working...");
    }

}
