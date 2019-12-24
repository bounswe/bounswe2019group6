package com.traderx.ui.article

object AnnotationValidator {
    fun validateText(text: String): Boolean {
        return text.isNotBlank()
    }
}