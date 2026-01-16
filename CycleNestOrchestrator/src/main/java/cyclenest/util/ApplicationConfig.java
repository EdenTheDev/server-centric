package cyclenest.util;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {

        // Scan for all @Path resources and providers under these packages
        packages(
            "cyclenest.resource",
            "cyclenest.osrm"
        );

        // JSON support (Object <-> JSON)
        register(JacksonFeature.class);
    }
}