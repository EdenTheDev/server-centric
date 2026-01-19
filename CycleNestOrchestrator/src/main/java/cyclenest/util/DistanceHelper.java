package cyclenest.util;

/**
 * DistanceHelper - Static utilities for geographic calculations.
 * I moved this logic here to keep the Resource classes clean and to 
 * allow for easy reuse across the application.
 */
public class DistanceHelper {

    /**
     * calculateHaversine - Calculates the straight-line distance between two points.
     * This local calculation is vital for Part C performance; it allows us to 
     * filter thousands of items locally before ever calling the OSRM API.
     */
    public static double calculateHaversine(double lat1, double lon1, double lat2, double lon2) {
        // Radius of the earth in kilometres
        final int R = 6371; 
        
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        // Returns the final distance in kilometres
        return R * c; 
    }
}