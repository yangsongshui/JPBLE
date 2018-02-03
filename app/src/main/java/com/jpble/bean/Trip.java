package com.jpble.bean;

import java.util.List;

/**
 * 作者：omni20170501
 */

public class Trip {
    String date;
    List<TripInfo> mList;

    public Trip() {
    }

    public Trip(String date, List<TripInfo> mList) {
        this.date = date;
        this.mList = mList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<TripInfo> getmList() {
        return mList;
    }

    public void setmList(List<TripInfo> mList) {
        this.mList = mList;
    }

    public static class TripInfo {
        String time;
        String km;
        String minute;
        String orbit;
        public TripInfo() {
        }

        public TripInfo(String time, String km, String minute,String orbit) {
            this.time = time;
            this.km = km;
            this.minute = minute;
            this.orbit = orbit;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getKm() {
            return km;
        }

        public void setKm(String km) {
            this.km = km;
        }

        public String getMinute() {
            return minute;
        }

        public void setMinute(String minute) {
            this.minute = minute;
        }

        public String getOrbit() {
            return orbit;
        }

        public void setOrbit(String orbit) {
            this.orbit = orbit;
        }

        @Override
        public String toString() {
            return "TripInfo{" +
                    "time='" + time + '\'' +
                    ", km='" + km + '\'' +
                    ", minute='" + minute + '\'' +
                    ", orbit='" + orbit + '\'' +
                    '}';
        }
    }
}
