package com.traderx.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ArticleDao {
    @Query("SELECT * FROM article WHERE id = :articleId")
    fun getArticle(articleId: Int): Single<Article>

    @Query("SELECT * FROM article LIMIT 10")
    fun getArticles(): Flowable<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArticle(article: Article): Completable

    @Query("DELETE FROM article where id = :articleId")
    fun deleteArticle(articleId: Int): Completable
}