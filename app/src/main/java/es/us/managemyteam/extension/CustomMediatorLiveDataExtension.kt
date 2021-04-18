package es.us.managemyteam.extension

import androidx.lifecycle.MutableLiveData
import es.us.managemyteam.util.CustomMediatorLiveData

fun CustomMediatorLiveData<*>.newData() = (liveData() as MutableLiveData).apply {
    value = null
}