package cmpe451.group6.rest.comment.model.article;

import cmpe451.group6.authorization.model.User;

import javax.persistence.*;

@Entity
public class ArticleCommentVote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_comment", referencedColumnName = "id")
    private ArticleComment articleComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user", referencedColumnName = "username")
    private User owner;

    @Column
    private boolean upvote;

    public ArticleCommentVote(ArticleComment articleComment, User owner, boolean upvote) {
        this.articleComment = articleComment;
        this.owner = owner;
        this.upvote = upvote;
    }

    public ArticleCommentVote() {
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArticleComment getArticleComment() {
        return articleComment;
    }

    public void setArticleComment(ArticleComment articleComment) {
        this.articleComment = articleComment;
    }

    public boolean getUpvote() {
        return upvote;
    }

    public void setUpvote(boolean upvote) {
        this.upvote = upvote;
    }
}