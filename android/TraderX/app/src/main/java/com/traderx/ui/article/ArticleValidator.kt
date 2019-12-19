package com.traderx.ui.article

object ArticleValidator {
    fun validateHeader(header: String): Boolean {
        return header.isNotBlank()
    }

    fun validateTags(tags: String): Boolean {
        return true
    }

    fun validateBody(body: String): Boolean {
        return body.isNotBlank()
    }
}