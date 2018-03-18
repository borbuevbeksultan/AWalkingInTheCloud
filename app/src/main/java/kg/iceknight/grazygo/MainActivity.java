package kg.iceknight.grazygo;

import android.Manifest.permission;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.Toast;

import kg.iceknight.grazygo.background.service.MockingService;
import kg.iceknight.grazygo.background.service.helper.ServiceBinder;
import kg.iceknight.grazygo.handler.ExitButtonHandler;
import kg.iceknight.grazygo.handler.MainButtonHandler;
import kg.iceknight.grazygo.service.MockHelperService;
import kg.iceknight.grazygo.service.MockService;
import kg.iceknight.grazygo.service.NotificationService;
import kg.iceknight.grazygo.service.ServiceCollection;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.location.LocationManager.NETWORK_PROVIDER;
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                    initializeUI();
                } else {
                    Toast.makeText(MainActivity.this, "Разрешите приложению доступ к GPS", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "MainActivity onCreate()");
        setContentView(R.layout.main_activity);
        if (!((LocationManager) getSystemService(LOCATION_SERVICE)).isProviderEnabled(NETWORK_PROVIDER)) {
            Toast.makeText(MainActivity.this, "Включите GPS и имитацию GPS", Toast.LENGTH_LONG).show();
            finishAffinity();
            return;
        }
        if (!MockHelperService.isMockSettingsON(this)) {
            Toast.makeText(MainActivity.this, "Включите имитацию местоположения", Toast.LENGTH_LONG).show();
            finishAffinity();
            return;
        }
        int permStatusFineLoc = ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION);
        if (permStatusFineLoc != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            initializeUI();
        }

    }

    @Override
    protected void onDestroy() {
        if (mockingService != null) {
            unbindService(serviceConnection);
        }
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
