package kg.iceknight.grazygo.service;

import android.location.Location;
import android.os.Vibrator;

import kg.iceknight.grazygo.MainActivity;
import kg.iceknight.grazygo.background.service.MockingService;
import kg.iceknight.grazygo.handler.MainButtonHandler;

public class ServiceCollection {

    private static NotificationService notificationService;
    private static MainActivity mainActivity;
    private static Location choosedLocation;
    private static Vibrator vibrator;

    public static Vibrator getVibrator() {
        return vibrator;
    }

    public static void setVibrator(Vibrator vibrator) {
        ServiceCollection.vibrator = vibrator;
    }

    public static MainButtonHandler getMainButtonHandler() {
        return mainButtonHandler;
    }

    public static void setMainButtonHandler(MainButtonHandler mainButtonHandler) {
        ServiceCollection.mainButtonHandler = mainButtonHandler;
    }

    private static MainButtonHandler mainButtonHandler;

    public static MockingService getMockingService() {
        return mockingService;
    }

    public static void setMockingService(MockingService mockingService) {
        ServiceCollection.mockingService = mockingService;
    }

    private static MockingService mockingService;

    public static Location getChoosedLocation() {
        return choosedLocation;
    }

    public static void setChoosedLocation(Location choosedLocation) {
        ServiceCollection.choosedLocation = choosedLocation;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    public static void setMainActivity(MainActivity mainActivity) {
        ServiceCollection.mainActivity = mainActivity;
    }

    public static MockHelperService getMockHelperService() {
        return mockHelperService;
    }

    public static void setMockHelperService(MockHelperService mockHelperService) {
        ServiceCollection.mockHelperService = mockHelperService;
    }

    private static MockHelperService mockHelperService;

    public static int getControlStatus() {
        return controlStatus;
    }

    public static void setControlStatus(int controlStatus) {
        ServiceCollection.controlStatus = controlStatus;
    }

    private static int controlStatus;

    public static NotificationService getNotificationService() {
        return notificationService;
    }

    public static void setNotificationService(NotificationService notificationService) {
        ServiceCollection.notificationService = notificationService;
    }
}
