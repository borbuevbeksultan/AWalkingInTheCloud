package kg.iceknight.grazygo.handler;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import kg.iceknight.grazygo.service.MockService;

public class MainButtonHandler implements View.OnClickListener {

    private Context context;

    public MainButtonHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onClick(View view) {
        //run mock
    }
}
