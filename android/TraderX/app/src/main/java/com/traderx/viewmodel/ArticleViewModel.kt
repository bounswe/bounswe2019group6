package com.traderx.viewmodel

import com.traderx.api.RequestService
import com.traderx.api.request.AnnotationRequest
import com.traderx.api.request.ArticleRequest
import com.traderx.api.request.CommentRequest
import com.traderx.api.response.AnnotationResponse
import com.traderx.api.response.CommentResponse
import com.traderx.db.Article
import com.traderx.db.ArticleDao
import com.traderx.type.VoteType
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class ArticleViewModel(
    private val dataSource: ArticleDao,
    private val networkSource: RequestService
) : BaseViewModel(), CommentableViewModel {

    fun getArticle(articleId: Int): Single<Article> {
        return networkSource.getArticle(articleId)
    }

    fun fetchArticles(): Single<ArrayList<Article>> {
        return networkSource.getArticles()
    }

    fun getArticles(): Flowable<ArrayList<Article>> {
        return fetchArticles().toFlowable()
    }

    fun getUserArticles(username: String): Flowable<ArrayList<Article>> {
        return networkSource.getUserArticles(username).toFlowable()
    }

    fun createArticle(articleRequest: ArticleRequest): Completable {
        return networkSource.createArticle(articleRequest)
    }

    fun deleteArticle(articleId: Int): Completable {
        return networkSource.deleteArticle(articleId)
    }

    override fun getComments(id: Any): Single<ArrayList<CommentResponse>> {
        return networkSource.getArticleComments(id as Int)
    }

    override fun createComment(id: Any, comment: String): Single<CommentResponse> {
        return networkSource.createCommentArticle(id as Int, CommentRequest(comment))
    }

    override fun editComment(id: Int, message: String): Completable {
        return networkSource.editCommentArticle(id, CommentRequest(message))
    }

    override fun revokeComment(id: Int): Completable {
        return networkSource.revokeCommentArticle(id)
    }

    override fun voteComment(id: Int, voteType: VoteType): Completable {
        return networkSource.voteCommentArticle(id, voteType.request)
    }

    override fun deleteComment(id: Int): Completable {
        return networkSource.deleteCommentArticle(id)
    }

    fun editArticle(id: Int, articleRequest: ArticleRequest): Completable {
        return networkSource.editArticle(id, articleRequest)
    }

    fun getAnnotations(id: Int): Single<ArrayList<AnnotationResponse>> {
        return networkSource.getArticleAnnotations(id)
    }

    fun createAnnotation(annotation: AnnotationRequest): Completable {
        return networkSource.createArticleAnnotation(annotation)
    }

    fun deleteAnnotation(id: Int): Completable {
        return networkSource.deleteArticleAnnotation(id)
    }
}