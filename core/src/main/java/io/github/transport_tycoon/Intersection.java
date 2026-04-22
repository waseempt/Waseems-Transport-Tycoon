package io.github.transport_tycoon;

public class Intersection {
    private boolean hasTrafficLights = false;
    private int roadMask;

    // Array to hold the duration of each phase
    // 4-Way: [0] = Vertical, [1] = Horizontal
    // 3-Way: [0] = Left, [1] = Right, [2] = Bottom
    private float[] phaseDurations;

    private int currentPhase = 0;
    private float timer = 0f;
    private float clearanceBuffer = 1.5f;

    public Intersection(int roadMask) {
        this.roadMask = roadMask;

        // 15 is the 4-way mask. Anything else reaching here is a 3-way.
        if (this.roadMask == 15) {
            this.phaseDurations = new float[]{10f, 10f};
        } else {
            this.phaseDurations = new float[]{10f, 10f, 10f};
        }
    }

    public void installLights() {
        this.hasTrafficLights = true;
        this.currentPhase = 0;
        this.timer = phaseDurations[0];
    }

    public boolean hasLights() { return hasTrafficLights; }
    public int getRoadMask() { return roadMask; }
    public int getPhaseCount() { return phaseDurations.length; }

    public void setPhaseDuration(int phase, float duration) {
        if (phase >= 0 && phase < phaseDurations.length) {
            phaseDurations[phase] = duration;
        }
    }

    public float getPhaseDuration(int phase) {
        if (phase >= 0 && phase < phaseDurations.length) {
            return phaseDurations[phase];
        }
        return 10f;
    }

    // handles the automatic switching of the lights
    public void updateLights(float delta) {
        if (!hasTrafficLights) return;

        timer -= delta;
        if (timer <= 0) {
            currentPhase = (currentPhase + 1) % phaseDurations.length;
            timer = phaseDurations[currentPhase];
        }
    }

    // Returns a simple string for the Renderer to know what to draw
    public String getVisualState() {
        if (!hasTrafficLights || timer > phaseDurations[currentPhase] - clearanceBuffer) return "none";

        if (roadMask == 15) { // 4-Way states
            return currentPhase == 0 ? "v" : "h";
        } else { // 3-Way states
            if (currentPhase == 0) return "l";
            if (currentPhase == 1) return "r";
            else return "b";
        }
    }
}
