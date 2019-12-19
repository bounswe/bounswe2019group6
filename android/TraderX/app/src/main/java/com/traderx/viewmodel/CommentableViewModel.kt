package com.traderx.viewmodel

import com.traderx.api.response.CommentResponse
import com.traderx.type.VoteType
import io.reactivex.Completable
import io.reactivex.Single

interface CommentableViewModel {
    fun createComment(code: String, comment: String): Single<CommentResponse>

    fun editComment(id: Int, message: String): Completable

    fun voteComment(id: Int, voteType: VoteType): Completable

    fun revokeComment(id: Int): Completable

    fun deleteAlert(id: Int): Completable

    fun deleteComment(id: Int): Completable
}