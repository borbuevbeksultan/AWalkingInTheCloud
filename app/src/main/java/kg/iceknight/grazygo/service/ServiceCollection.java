package kg.iceknight.grazygo.service;

public class ServiceCollection {

    private static NotificationService notificationService;

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
