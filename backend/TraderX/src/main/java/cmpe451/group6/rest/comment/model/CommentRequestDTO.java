package cmpe451.group6.rest.comment.model;

import io.swagger.annotations.ApiModelProperty;

public class CommentRequestDTO {

    @ApiModelProperty(position = 0)
    private String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
