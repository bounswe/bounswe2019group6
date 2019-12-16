package cmpe451.group6.rest.event.model;

import javax.persistence.*;

@Entity
public class EventsJson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, updatable = false, length = 10000)
    private String json;

    public EventsJson() {
    }

    public EventsJson(String json) {
        this.json = json;
    }

    public Integer getId() {
        return id;
    }

    public String getJson() {
        return json;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
