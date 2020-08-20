package es.us.managemyteam.contract

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.paypal.android.sdk.payments.ProofOfPayment

private const val ISO_CODE = "EUR"

interface PaypalInterface {

    fun initialize(context: Context)

    fun goToPaypal(
        fragment: Fragment,
        price: String,
        itemName: String,
        isoCode: String = ISO_CODE
    )

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    fun setResultListener(resultListener: PaypalResultListener)

    interface PaypalResultListener {

        fun onPaymentOk(concept: String, quantity: String, paymentProof: ProofOfPayment)

        fun onPaymentError()

        fun onPaymentCancelled()

    }

}