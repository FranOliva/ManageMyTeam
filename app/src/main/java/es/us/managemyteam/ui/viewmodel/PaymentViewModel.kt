package es.us.managemyteam.ui.viewmodel

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.us.managemyteam.contract.PaypalInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PaymentViewModel(
    private val paypalInterface: PaypalInterface
) : ViewModel(), PaypalInterface.PaypalResultListener {

    init {
        paypalInterface.setResultListener(this)
    }

    fun goToPaypal(
        fragment: Fragment,
        price: String,
        itemName: String
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            paypalInterface.goToPaypal(fragment, price, itemName)
        }
    }

    fun catchResult(requestCode: Int, resultCode: Int, data: Intent?) {
        viewModelScope.launch(Dispatchers.Main) {
            paypalInterface.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun sendToServer() {
        viewModelScope.launch(Dispatchers.Main) {
            withContext(Dispatchers.IO) {
                // TODO send info to server
            }
        }
    }

    override fun onPaymentOk() {
        sendToServer()
    }

    override fun onPaymentError() {
    }

    override fun onPaymentCancelled() {
    }

}