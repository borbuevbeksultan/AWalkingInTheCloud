package kg.iceknight.grazygo;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import kg.iceknight.grazygo.handler.MainButtonHandler;
import kg.iceknight.grazygo.service.MockService;

public class MainActivity extends AppCompatActivity {

    private Button mainButton;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUI();
    }

    private void initializeUI() {
        mainButton = findViewById(R.id.mainButton);
        mainButton.setOnClickListener(new MainButtonHandler(this));
    }

    public void onClick(View view) {

    }

}
