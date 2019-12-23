package com.traderx.api

import com.traderx.AppConfig

object ApiEndpoint {
    const val API_URI: String = AppConfig.API_HOST
    const val ANNOTATION_URI: String = "${AppConfig.ANNOTATION_HOST}/annotation"
    const val COMMENT_URI: String = "$API_URI/comment"
    const val FOLLOW_URI: String = "$API_URI/follow"
    const val FOLLOW_USER: String = "$FOLLOW_URI/follow"
    const val UNFOLLOW_USER: String = "$FOLLOW_URI/unfollow"
    const val FOLLOW_REMOVE: String = "$FOLLOW_URI/follower/remove"
    const val FOLLOWERS_LIST: String = "$FOLLOW_URI/followers/list"
    const val FOLLOWINGS_LIST: String = "$FOLLOW_URI/follows/list"
    const val FOLLOW_ACCEPT: String = "$FOLLOW_URI/request/accept"
    const val FOLLOW_DECLINE: String = "$FOLLOW_URI/request/decline"
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
    const val BECOME_TRADER: String = "$USER_URI/set_profile/{role}"
    const val ARTICLE_URI: String = "$API_URI/article"
    const val ARTICLE: String = "$ARTICLE_URI/byId/{id}"
    const val ARTICLES: String = "$ARTICLE_URI/all"
    const val ARTICLE_CREATE: String = "$ARTICLE_URI/write"
    const val ARTICLE_CREATE_IMAGE: String = "$API_URI/image/upload"
    const val ARTICLE_USER_ARTICLES: String = "$ARTICLE_URI/byUsername/{username}"
    const val ARTICLE_EDIT: String = "$ARTICLE_URI/edit"
    const val ARTICLE_DELETE: String = "$ARTICLE_URI/delete"
    const val ANNOTATION_ARTICLE: String = "$ANNOTATION_URI/{id}"
    const val ANNOTATION_ARTICLE_CREATE: String = "$ANNOTATION_URI/create"
    const val ANNOTATION_ARTICLE_UPDATE: String = "$ANNOTATION_URI/update"
    const val ANNOTATION_ARTICLE_DELETE: String = "$ANNOTATION_URI/delete"
    const val ANNOTATION_ARTICLE_ALL: String = "$ANNOTATION_URI/all/{id}"
    const val COMMENT_ARTICLE: String = "$COMMENT_URI/article/{id}"
    const val COMMENT_ARTICLE_INSERT: String = "$COMMENT_URI/article/post/{id}"
    const val COMMENT_ARTICLE_EDIT: String = "$COMMENT_URI/article/edit/{id}"
    const val COMMENT_ARTICLE_VOTE: String = "$COMMENT_URI/article/vote/{id}/{type}"
    const val COMMENT_ARTICLE_REVOKE: String = "$COMMENT_URI/article/revoke/{id}"
    const val COMMENT_ARTICLE_DELETE: String = "$COMMENT_URI/article/delete/{id}"
    const val EQUIPMENT: String = "$EQUIPMENT_URI/{name}"
    const val EQUIPMENT_CURRENCY_LIST: String = "$EQUIPMENT_URI/currency/list"
    const val EQUIPMENT_CRYPTO_CURRENCY_LIST: String = "$EQUIPMENT_URI/crypto-currency/list"
    const val EQUIPMENT_STOCK_LIST: String = "$EQUIPMENT_URI/stock/list"
    const val COMMENT_EQUIPMENT: String = "$COMMENT_URI/equipment/{code}"
    const val COMMENT_EQUIPMENT_POST: String = "$COMMENT_URI/equipment/post/{code}"
    const val COMMENT_EDIT: String = "$COMMENT_URI/equipment/edit/{id}"
    const val COMMENT_VOTE: String = "$COMMENT_URI/equipment/vote/{id}/{vote}"
    const val COMMENT_REVOKE: String = "$COMMENT_URI/equipment/revoke/{id}"
    const val COMMENT_DELETE: String = "$COMMENT_URI/equipment/delete/{id}"
    const val TRANSACTION_URI: String = "$API_URI/transaction"
    const val TRANSACTION_BUY: String = "$TRANSACTION_URI/buy"
    const val TRANSACTIONS: String = "$TRANSACTION_URI/user/{username}"
    const val ASSET_URI: String = "$API_URI/asset"
    const val ASSET_AMOUNT: String = "$ASSET_URI/code/{code}"
    const val ALERT_URI: String = "$API_URI/alert"
    const val ALERT_ALL: String = "$ALERT_URI/get"
    const val ALERT_DELETE: String = "$ALERT_URI/remove"
    const val ALERT_CREATE: String = "$ALERT_URI/set"
    const val USER_FORGOT_PASSWORD: String = "$API_URI/password/forgot"
    const val GET_PORTFOLIO: String = "$API_URI/portfolio/get"
    const val GET_ALL_PORTFOLIO: String = "$API_URI/portfolio/getAll"
    const val ADD_PORTFOLIO: String = "$API_URI/portfolio/create"
    const val ADD_TO_PORTFOLIO: String = "$API_URI/portfolio/add"
    const val ADD_MANY_TO_PORTFOLIO: String = "$API_URI/portfolio/equipments"
    const val DELETE_PORTFOLIO: String = "$API_URI/portfolio/delete"
    const val DELETE_FROM_PORTFOLIO: String = "$API_URI/portfolio/discard"
    const val EVENTS: String = "$API_URI/events"
    const val PREDICTION_URI: String = "$API_URI/prediction"
    const val PREDICTION_CREATE: String = "$PREDICTION_URI/create"
    const val PREDICTION_USER_ALL: String = "$PREDICTION_URI/list/{username}"
    const val SEARCH_URI: String = "$API_URI/search"
    const val SEARCH_USERS: String = "$SEARCH_URI/user/byName"
    const val SEARCH_ARTICLES: String = "$SEARCH_URI/article/byHeader"
    const val SEARCH_EQUIPMENTS: String = "$SEARCH_URI/equipment/byName"
}