package com.geotag.mapsnap;

public class Camera {
    double latitude;
    double longitude;
    String model;
    String resolution;
    String fps;
    String range;
    String owner;
    String number;
    String constant;
    String nightVision;

    String id;
    public Camera(double latitude, double longitude, String model, String resolution,
                  String fps, String range, String owner, String number,
                  String constant, String nightVision) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.model = model;
        this.resolution = resolution;
        this.fps = fps;
        this.range = range;
        this.owner = owner;
        this.number = number;
        this.constant = constant;
        this.nightVision = nightVision;
    }

    // Default constructor (used by Firestore)
    public Camera() {}

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getFps() {
        return fps;
    }

    public void setFps(String fps) {
        this.fps = fps;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getConstant() {
        return constant;
    }

    public void setConstant(String constant) {
        this.constant = constant;
    }

    public String getNightVision() {
        return nightVision;
    }

    public void setNightVision(String nightVision) {
        this.nightVision = nightVision;
    }
}
