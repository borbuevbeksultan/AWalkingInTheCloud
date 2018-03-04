package kg.iceknight.grazygo.service;

import kg.iceknight.grazygo.MainActivity;

public class ServiceCollection {

    private static NotificationService notificationService;
    private static MainActivity mainActivity;

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
