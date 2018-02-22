package kg.iceknight.grazygo.handler;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import kg.iceknight.grazygo.service.MockService;

public class MainButtonHandler implements View.OnClickListener {

    private Context context;

    public MainButtonHandler(Context context) {
        this.context = context;
    }

    @SuppressLint("ShowToast")
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(context, MockService.class);
        context.startService(intent);
    }
}
