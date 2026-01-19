package cyclenest.osrm;

/**
 * Model class for OSRM route results.
 * Includes helper methods for human-readable units.
 */
public class DistanceResult {
    private double distance; // Raw meters from OSRM
    private double duration; // Raw seconds from OSRM

    public DistanceResult() {}

    public DistanceResult(double distance, double duration) {
        this.distance = distance;
        this.duration = duration;
    }

    // Standard raw data getters for the API
    public double getDistanceMeters() { return distance; }
    public double getDurationSeconds() { return duration; }

    // Human-readable helpers
    public String getDisplayDistance() {
        return String.format("%.2f km", distance / 1000.0);
    }

    public String getDisplayDuration() {
        int mins = (int) Math.round(duration / 60.0);
        if (mins >= 60) {
            return (mins / 60) + "h " + (mins % 60) + "m";
        }
        return mins + " mins";
    }

    // Setters required for Jackson/OSRM Mapping
    public double getDistance() { return distance; }
    public void setDistance(double distance) { this.distance = distance; }
    public double getDuration() { return duration; }
    public void setDuration(double duration) { this.duration = duration; }
}