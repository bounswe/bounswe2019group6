package cmpe451.group6.rest.equipment;

import java.io.Serializable;
import java.util.Date;

class EquipmentValue implements Serializable {

    private Date timestamp;

    private double value;

    public EquipmentValue() {
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
