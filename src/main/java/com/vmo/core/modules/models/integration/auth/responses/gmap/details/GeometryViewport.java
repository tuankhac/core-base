package com.vmo.core.modules.models.integration.auth.responses.gmap.details;

public class GeometryViewport {
    private GeometryLocation northeast;
    private GeometryLocation southwest;

    public GeometryLocation getNortheast() {
        return northeast;
    }

    public void setNortheast(GeometryLocation northeast) {
        this.northeast = northeast;
    }

    public GeometryLocation getSouthwest() {
        return southwest;
    }

    public void setSouthwest(GeometryLocation southwest) {
        this.southwest = southwest;
    }
}
