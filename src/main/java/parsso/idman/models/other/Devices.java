package parsso.idman.models.other;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class Devices {
    public Devices() {
    }

    long _id;
    String username;
    String name;
    Time time;

    public Time getTime() {
        return Time.longToPersianTime(_id);
    }

    @Getter
    @Setter
    public static class DeviceList {
        List<Devices> devices;
        int pages;
        long size;

        public DeviceList() {
        }

        public DeviceList(List<Devices> devicesList, long size, int pages) {
            this.devices = devicesList;
            this.pages = pages;
            this.size = size;
        }
    }

}
