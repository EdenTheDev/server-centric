package cyclenest.util; // Make sure this matches the folder it is in!

import org.glassfish.jersey.server.ResourceConfig;
import javax.ws.rs.ApplicationPath;

@ApplicationPath("api") // It's better to use "api" so your URLs are /api/items
public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        // This tells Jersey to look inside 'cyclenest' for all your resources
        packages("cyclenest");
    }
}