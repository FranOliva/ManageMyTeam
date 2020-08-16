package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.us.managemyteam.data.model.PaypalConfigBo
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.CreatePaypalConfigUc
import es.us.managemyteam.usecase.GetPaypalConfigUc
import es.us.managemyteam.util.CustomMediatorLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AdminPaypalViewModel(
    private val getPaypalConfigUc: GetPaypalConfigUc,
    private val createPaypalConfigUc: CreatePaypalConfigUc
) : ViewModel() {

    private val paypalConfig = CustomMediatorLiveData<Resource<PaypalConfigBo>>()
    private val createPaypalConfigData = CustomMediatorLiveData<Resource<Boolean>>()

    init {
        getPaypalConfig()
    }

    fun getPaypalConfigData() = paypalConfig.liveData()

    fun getPaypalConfig() {
        viewModelScope.launch(Dispatchers.Main) {
            paypalConfig.setData(Resource.loading(data = null))
            withContext(Dispatchers.IO) {
                paypalConfig.changeSource(
                    Dispatchers.Main,
                    getPaypalConfigUc()
                )
            }
        }
    }

    fun getCreatePaypalConfigData() = createPaypalConfigData.liveData()

    fun createPaypalConfig(paypalConfigBo: PaypalConfigBo) {
        viewModelScope.launch(Dispatchers.Main) {
            createPaypalConfigData.setData(Resource.loading(data = null))
            withContext(Dispatchers.IO) {
                createPaypalConfigData.changeSource(
                    Dispatchers.Main,
                    createPaypalConfigUc(paypalConfigBo)
                )
            }
        }
    }
}