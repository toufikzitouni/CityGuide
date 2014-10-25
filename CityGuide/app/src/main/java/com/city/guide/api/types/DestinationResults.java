package com.city.guide.api.types;

/**
 * Created by toufikzitouni on 14-10-24.
 */
public class DestinationResults {
    private DestinationResults[] rows;
    private DestinationResults[] elements;
    private DestinationResults distance;
    private String text;

    public DestinationResults[] getRows() {
        return rows;
    }

    public void setRows(DestinationResults[] rows) {
        this.rows = rows;
    }

    public DestinationResults[] getElements() {
        return elements;
    }

    public void setElements(DestinationResults[] elements) {
        this.elements = elements;
    }

    public DestinationResults getDistance() {
        return distance;
    }

    public void setDistance(DestinationResults distance) {
        this.distance = distance;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
