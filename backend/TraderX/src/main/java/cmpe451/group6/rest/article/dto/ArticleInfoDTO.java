package cmpe451.group6.rest.article.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ArticleInfoDTO {

    @ApiModelProperty(position = 0,required = true)
    private String header;

    @ApiModelProperty(position = 1)
    private String body;

    @ApiModelProperty(position = 2)
    private List<String> tags;

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
}

