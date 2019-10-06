package cmpe451.group6.rest.template.dto;


import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 *  This class defines the format of your database object's (TemplateItem)
 *  response format. For instance, in TemplateItem class, we have boolField.
 *  Since we do not include it here, this field won't be sent to the client
 *  in API response.
 *
 *  [DO NOT CHANGE OR IMPLEMENT THOSE CLASSES / INTERFACES. FOR TEMPLATING PURPOSE ONLY.]
 */

public class TemplateItemResponseDTO {

    // @ApiModel property is to create JSON formatted Swagger doc automatically.
    // Add all getters and setters.
    // This class is going to be used to:
    //      1. Store data coming from client directly.
    //      2. Send data from database to client directly.
    // In short, JSON representation of a class will be created according to the fields defined here.

    @ApiModelProperty(position = 0)
    private int itemId;
    @ApiModelProperty(position = 1)
    private long longField;

    //@ApiModelProperty(position = 2)
    //private boolean boolField;

    @ApiModelProperty(position = 2)
    private String stringField;
    @ApiModelProperty(position = 3)
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