package io.github.transport_tycoon;

public class TrafficLight {
    private LightState state;
    private float duration; // How long this light stays on
    private float timer;    // The active counter

    public TrafficLight(LightState initialState) {
        this.state = initialState;
        this.duration = 10f;

        this.timer = this.duration;

    }

    public LightState getState() {
        return state;
    }

    public void setState(LightState state) {
        this.state = state;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getTimer() {
        return timer;
    }

    public void setTimer(float timer) {
        this.timer = timer;
    }
}
