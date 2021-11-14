package com.pmapps.bustime2.BusStop;

public class BusStopItem {
    private String busStopTitle;
    private String busStopCode;
    private String busStopRoad;

    public BusStopItem(String busStopTitle, String busStopCode, String busStopRoad) {
        this.busStopTitle = busStopTitle;
        this.busStopCode = busStopCode;
        this.busStopRoad = busStopRoad;
    }

    public String getBusStopTitle() {
        return busStopTitle;
    }

    public String getBusStopCode() {
        return busStopCode;
    }

    public String getBusStopRoad() {
        return busStopRoad;
    }

}
