package cmpe451.group6.rest.search.dto;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public class ArticleSearchDTO implements Serializable {

    @ApiModelProperty(position = 0, required = true)
    private int id;

    @ApiModelProperty(position = 1, required = true)
    private String user;

    @ApiModelProperty(position = 2, required = true)
    private String header;

    public ArticleSearchDTO() {
    }


    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
