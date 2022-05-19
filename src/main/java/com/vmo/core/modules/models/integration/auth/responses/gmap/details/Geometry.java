package com.vmo.core.modules.models.integration.auth.responses.gmap.details;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Null;

public class Geometry {
    /**
     * Possible no data
     */
    @Null
    private GeometryBounds bounds;
    private GeometryLocation location;
    @JsonProperty("location_type")
    private String locationType;
    private GeometryViewport viewport;

    public GeometryBounds getBounds() {
        return bounds;
    }

    @Null
    public void setBounds(GeometryBounds bounds) {
        this.bounds = bounds;
    }

    public GeometryLocation getLocation() {
        return location;
    }

    public void setLocation(GeometryLocation location) {
        this.location = location;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public GeometryViewport getViewport() {
        return viewport;
    }

    public void setViewport(GeometryViewport viewport) {
        this.viewport = viewport;
    }
}
