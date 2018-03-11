package kg.iceknight.grazygo.background.service.helper;

import android.app.Service;
import android.os.Binder;

public class ServiceBinder extends Binder {

    private final Service service;

    public ServiceBinder(Service service) {
        this.service = service;
    }

    public Service getService() {
        return this.service;
    }
}
