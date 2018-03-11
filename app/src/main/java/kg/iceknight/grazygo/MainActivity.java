package kg.iceknight.grazygo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.concurrent.Executors;

import kg.iceknight.grazygo.background.service.MockingService;
import kg.iceknight.grazygo.background.service.helper.ServiceBinder;
import kg.iceknight.grazygo.handler.ExitButtonHandler;
import kg.iceknight.grazygo.handler.MainButtonHandler;
import kg.iceknight.grazygo.service.MockHelperService;
import kg.iceknight.grazygo.service.MockService;
import kg.iceknight.grazygo.service.NotificationService;
import kg.iceknight.grazygo.service.ServiceCollection;

import static kg.iceknight.grazygo.common.Constants.LOG_TAG;

public class MainActivity extends AppCompatActivity {

    private Button mainButton;
    private Button exitButton;
    public Button setBtn;
    public Button resetBtn;
    public boolean isWorking;
    private EditText editText;
    private NotificationService notificationService;
    private MockingService mockingService;
    private ServiceConnection serviceConnection;
    public Integer distance = 1;
    public Integer delay = 1;
    public Integer variant = 1;


    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "MainActivity onCreate()");
        setContentView(R.layout.main_activity);
        initializeUI();
        if (ActivityCompat
                .checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            ActivityCompat.requestPermissions(this ,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},2);
        }
        if (Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION).equals("0")) {
            Toast.makeText(MainActivity.this, "Включите фиктивное местоположение", Toast.LENGTH_LONG).show();
            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
        }

    }

    @Override
    protected void onDestroy() {
        unbindService(serviceConnection);
        Log.d(LOG_TAG, "MainActivity onDestroy");
        super.onDestroy();
    }

    private void initializeUI() {
        setBtn = findViewById(R.id.setBtn);
        resetBtn = findViewById(R.id.resetBtn);
        mainButton = findViewById(R.id.mainButton);
        exitButton = findViewById(R.id.exit);
        editText = findViewById(R.id.editTextCoodr);
        ServiceCollection.setMainActivity(this);
        RadioButton variant1;
        RadioButton variant2;
        variant1 = findViewById(R.id.variant1);
        variant2 = findViewById(R.id.variant2);
        NumberPicker numberPicker1;
        NumberPicker numberPicker2;
        numberPicker1 = findViewById(R.id.numberPicker1);
        numberPicker2 = findViewById(R.id.numberPicker2);
        variant1.setOnClickListener(view -> {
            variant2.setChecked(false);
            variant = 1;
            numberPicker2.setEnabled(true);
        });

        variant2.setOnClickListener(view -> {
            variant1.setChecked(false);
            variant = 2;
            numberPicker2.setEnabled(false);
        });
        variant1.setChecked(true);
        numberPicker1.setMinValue(1);
        numberPicker1.setMaxValue(10);
        numberPicker1.setWrapSelectorWheel(true);
        numberPicker1.setOnValueChangedListener((numberPicker, i, i1) -> distance = numberPicker.getValue());
        numberPicker2.setMinValue(1);
        numberPicker2.setMaxValue(10);
        numberPicker2.setWrapSelectorWheel(true);
        numberPicker2.setOnValueChangedListener((numberPicker, i, i1) -> delay = numberPicker.getValue());
        ServiceCollection.setVibrator((Vibrator) getSystemService(VIBRATOR_SERVICE));
        this.notificationService = new NotificationService(this);
        MainButtonHandler mainButtonHandler = new MainButtonHandler(mainButton, this, notificationService, setBtn, resetBtn);
        mainButton.setOnClickListener(mainButtonHandler);
        ServiceCollection.setMainButtonHandler(mainButtonHandler);
        ServiceCollection.setNotificationService(notificationService);
        serviceConnection = new DefaultServiceConnection();
        exitButton.setOnClickListener(new ExitButtonHandler());
        Intent intent = new Intent(this, MockingService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private class DefaultServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mockingService = (MockingService) ((ServiceBinder) iBinder).getService();
            ServiceCollection.setMockingService(mockingService);
            ServiceCollection.setMockHelperService(new MockHelperService(mockingService));
            Log.d(LOG_TAG, "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(LOG_TAG, "onServiceDisconnected");
        }
    }

    @SuppressLint("NewApi")
    public void setButtonOnClick(View view) {
        try {
            if (!isWorking) {
                Location location = new Location("");
                String[] coordString = editText.getText().toString().split(" ");
                for (int i = 0; i < coordString.length; i++) {
                    coordString[i] = coordString[i].trim().replace(",", ".");
                }
                Double latitude = Double.parseDouble(coordString[0]);
                Double longitute = Double.parseDouble(coordString[1]);
                location.setLatitude(latitude);
                location.setLongitude(longitute);
                ServiceCollection.setChoosedLocation(location);
                new MockService(ServiceCollection.getMockingService()).setLocation(location);
                Log.d(LOG_TAG, location.toString());
                Toast.makeText(MainActivity.this, "Установлено: " + latitude + " " + longitute, Toast.LENGTH_LONG).show();
            }
        } catch (SecurityException e) {
            Toast.makeText(MainActivity.this, "Включите имитацию местоположения", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Введите корректные данные", Toast.LENGTH_LONG).show();
        }
    }

    public void resetButtonOnClick(View view) {
        if ((!isWorking) && (ServiceCollection.getChoosedLocation() != null)) {
            Toast.makeText(MainActivity.this, "Настройки сброшены", Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, "MainActivity resetButtonClick");
            ServiceCollection.setChoosedLocation(null);
            new MockService(ServiceCollection.getMockingService()).disableMockLocation();
        }
    }

}
