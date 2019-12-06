package com.traderx.viewmodel

import com.traderx.api.RequestService
import com.traderx.db.Article
import com.traderx.db.ArticleDao
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class TraderEquipmentViewModel(
    private val dataSource: ArticleDao,
    private val networkSource: RequestService
) : BaseViewModel() {

    fun getArticle(articleId: Int): Single<Article> {
        return dataSource.getArticle(articleId).flatMap { networkSource.getArticle(articleId) }
    }

    fun fetchArticles(): Single<List<Article>> {
        return networkSource.getArticles()
    }

    fun getArticles(): Flowable<List<Article>> {
        return dataSource.getArticles().flatMap { fetchArticles().toFlowable() }
    }

    fun insertArticle(article: Article): Completable {
        return networkSource.insertArticle(article).concatWith { dataSource.insertArticle(article)}
    }

    fun deleteArticle(articleId: Int): Completable {
        return networkSource.deleteArticle(articleId).concatWith { dataSource.deleteArticle(articleId)}
    }
}