package cyclenest.osrm;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OsrmResponse {
    public double[][] distances;
    public double[][] durations;

    public OsrmResponse() {} // Required for Jackson
}