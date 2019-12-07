package cmpe451.group6.rest.equipment.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class HistoricalValue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Date timestamp;

    @Column(nullable = false)
    private double low;

    @Column(nullable = false)
    private double open;

    @Column(nullable = false)
    private double high;

    @Column(nullable = false)
    private double close;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_equipment", referencedColumnName = "code")
    private Equipment equipment;

    public HistoricalValue() {
    }

    public HistoricalValue(Date timestamp, double low, double open, double high, double close, Equipment equipment) {
        this.timestamp = timestamp;
        this.low = low;
        this.open = open;
        this.high = high;
        this.close = close;
        this.equipment = equipment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }
}
