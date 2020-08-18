package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.us.managemyteam.data.model.PaymentBo
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.GetMyPaymentsUc
import es.us.managemyteam.util.CustomMediatorLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyPaymentsViewModel(
    private val getMyPaymentsUc: GetMyPaymentsUc
) : ViewModel() {

    private val myPaymentsData = CustomMediatorLiveData<Resource<List<PaymentBo>>>()

    fun getMyPaymentsData() = myPaymentsData.liveData()

    fun getMyPayments() {
        viewModelScope.launch(Dispatchers.Main) {
            myPaymentsData.setData(Resource.loading(data = null))
            withContext(Dispatchers.IO) {
                myPaymentsData.changeSource(
                    Dispatchers.Main,
                    getMyPaymentsUc()
                )
            }
        }
    }

}