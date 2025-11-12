package com.cyclenest.osrm;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class ApplicationConfig extends Application {
    // Empty on purpose — activates JAX-RS under /api/*
}
