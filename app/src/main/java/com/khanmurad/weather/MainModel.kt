package com.khanmurad.weather

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.khanmurad.weather.WeatherAPI.Repository
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.actor

class MainModel: ViewModel() {
    private val cache = HashMap<String, List<Chart>>(10)
    private val mutableCity = MutableLiveData<String>().apply { value = "" }
    private val mutableCharts = MutableLiveData<List<Chart>>().apply { value = emptyList() }
    private val mutableLoading = MutableLiveData<Boolean>().apply { value = false }
    private val mutableMessage = MutableLiveData<String>()

    val city: LiveData<String> = mutableCity
    val charts: LiveData<List<Chart>> = mutableCharts
    val loading: LiveData<Boolean> = mutableLoading
    val message: LiveData<String> = mutableMessage

    private val actor = actor<Action>(UI, Channel.CONFLATED) {
        for (action in this) when (action) {
            is SelectCity -> {
                mutableCity.value = action.city
                mutableLoading.value = true
                try {
                    mutableCharts.value = cache.getFreshCharts(action.city) ?: getNewCharts(action.city)
                } catch (e: Exception) {
                    mutableMessage.value = e.toString()
                }
                mutableLoading.value = false
            }
        }
    }
    init{ action(SelectCity("London")) }
    fun action(action: Action) = actor.offer(action)

    override fun onCleared() = actor.cancel().unit

    private suspend fun getNewCharts(city: String)
        = Repository.getCityCharts(city).also { cache[city] = it }
}