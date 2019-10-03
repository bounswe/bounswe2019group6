package cmpe451.group6.rest.template.model;


import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 *  Here, you create an object which is
 *  going to be stored in database directly.
 *
 *  [DO NOT CHANGE OR IMPLEMENT THOSE CLASSES / INTERFACES. FOR TEMPLATING PURPOSE ONLY.]
 */
@Entity(name = "OptionalTableName")
public class TemplateItem {

    // Make all fields private and add getters and setters
    // Do not implement and internal methods. (i.e. public void incrementLongField()... )
    // Use Service component instead.

    // Each item has to have a ID
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int itemId;

    @NotNull
    private long longField;

    @Column(name = "optionalColumnName")
    private boolean boolField;

    @Size(min = 1, max = 4)
    private String stringField;

    private List<String> listField;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public long getLongField() {
        return longField;
    }

    public void setLongField(long longField) {
        this.longField = longField;
    }

    public boolean isBoolField() {
        return boolField;
    }

    public void setBoolField(boolean boolField) {
        this.boolField = boolField;
    }

    public String getStringField() {
        return stringField;
    }

    public void setStringField(String stringField) {
        this.stringField = stringField;
    }

    public List<String> getListField() {
        return listField;
    }

    public void setListField(List<String> listField) {
        this.listField = listField;
    }
}
