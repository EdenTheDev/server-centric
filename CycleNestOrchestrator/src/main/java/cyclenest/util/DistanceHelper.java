package cyclenest.util;

/**
 * Helper class for distance calculations.
 */
public class DistanceHelper {

    /**
     * Calculates the "as-the-crow-flies" distance between two points using the Haversine formula.
     * This local math avoids hitting external API rate limits for large datasets.
     */
    public static double calculateHaversine(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in km
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c; // Returns distance in kilometers
    }
}