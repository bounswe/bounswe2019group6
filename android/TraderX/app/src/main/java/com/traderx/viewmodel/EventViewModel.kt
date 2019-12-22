package com.traderx.viewmodel

import com.traderx.api.RequestService
import com.traderx.api.response.EventResponse
import io.reactivex.Single

class EventViewModel(
    private val networkSource: RequestService
): BaseViewModel() {
    fun getEvents(): Single<List<EventResponse>> {
        return networkSource.getEvents()
    }
}