package com.traderx.api

import com.traderx.AppConfig

object ApiEndpoint {
    const val API_URI: String = AppConfig.API_HOST
    const val FOLLOW_URI: String = "$API_URI/follow"
    const val FOLLOW_USER: String = "$FOLLOW_URI/follow"
    const val UNFOLLOW_USER: String = "$FOLLOW_URI/unfollow"
    const val FOLLOWERS_LIST: String = "$FOLLOW_URI/followers/list"
    const val FOLLOWINGS_LIST: String = "$FOLLOW_URI/follows/list"
    const val PENDING_FOLLOW_REQUESTS: String = "$FOLLOW_URI/request/list"
    const val USER_URI: String = "$API_URI/users"
    const val EQUIPMENT_URI: String = "$API_URI/equipment"
    const val USERS_ALL: String = "$USER_URI/getAll"
    const val USER_SIGNIN: String = "$API_URI/login"
    const val USER_SIGNUP: String = "$API_URI/signup"
    const val USER_SIGNOUT: String = "$API_URI/signout"
    const val USER_INFO: String = "$USER_URI/me"
    const val USER_PROFILE: String = "$USER_URI/profile/{username}"
    const val UPDATE_USER: String = "$USER_URI/set_profile/{status}"
    const val ARTICLE: String = "$API_URI/article/{articleId}"
    const val ARTICLES: String = "$API_URI/test"
    const val INSERT_ARTICLE: String = "$API_URI/article"
    const val EQUIPMENT: String = "$EQUIPMENT_URI/{name}"
    const val EQUIPMENT_CURRENCY_LIST: String = "$EQUIPMENT_URI/currency/list"
    const val EQUIPMENT_CRYPTO_CURRENCY_LIST: String = "$EQUIPMENT_URI/crypto-currency/list"
    const val EQUIPMENT_STOCK_LIST: String = "$EQUIPMENT_URI/stock/list"
    const val COMMENT_URI: String = "$API_URI/comment"
    const val COMMENT_EQUIPMENT: String = "$COMMENT_URI/equipment/{code}"
}