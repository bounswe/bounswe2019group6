package cmpe451.group6.rest.article.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.Date;
import java.util.List;

public class ArticleDTO {

    @ApiModelProperty(position = 0, required = true)
    private int id;
    @ApiModelProperty(position = 1, required = true)
    private String username;
    @ApiModelProperty(position = 2, required = true)
    private String header;
    @ApiModelProperty(position = 3, required = true)
    private String body;
    @ApiModelProperty(position = 4, required = true)
    private List<String> tags;
    @ApiModelProperty(position = 4, required = true)
    private String createdAt;
    @ApiModelProperty(position = 5, required = true)
    private String imageUrl;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
