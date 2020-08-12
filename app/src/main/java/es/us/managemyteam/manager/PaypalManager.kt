package es.us.managemyteam.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.paypal.android.sdk.payments.*
import es.us.managemyteam.contract.PaypalInterface
import org.json.JSONException
import java.math.BigDecimal

private const val REQUEST_CODE_PAYMENT = 1
private const val REQUEST_CODE_FUTURE_PAYMENT = 2
private const val REQUEST_CODE_PROFILE_SHARING = 3

private const val CONFIG_CLIENT_ID = "AS6bpF9z23r85dwuFs5CWhwzDNDHYgqGKTAtxWr8ht4BpXYhDY2pzqDyS8vHObxt7WHPjD1WyFgnpZZw"

private const val CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK
//private const val CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX
//private const val CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_PRODUCTION


class PaypalManager : PaypalInterface {

    private val TAG = javaClass.simpleName
    private val config = PayPalConfiguration()
        .environment(CONFIG_ENVIRONMENT)
        .clientId(CONFIG_CLIENT_ID)
        .merchantName("ManageMyTeam")
        .sandboxUserPassword(">=0u+@Nf")
        .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
        .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"))
    private var resultListener: PaypalInterface.PaypalResultListener? = null

    override fun initialize(context: Context) {
        context.startService(Intent(context, PayPalService::class.java).apply {
            putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
        })
    }

    override fun goToPaypal(
        fragment: Fragment,
        price: String,
        itemName: String,
        isoCode: String
    ) {
        fragment.let {
            it.startActivityForResult(Intent(fragment.context, PaymentActivity::class.java).apply {
                // send the same configuration for restart resiliency
                putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config)
                putExtra(PaymentActivity.EXTRA_PAYMENT, getItemToBuy(price, itemName, isoCode))
            }, REQUEST_CODE_PAYMENT)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                val confirm =
                    data?.getParcelableExtra<PaymentConfirmation>(PaymentActivity.EXTRA_RESULT_CONFIRMATION)
                if (confirm != null) {
                    try {
                        Log.i(TAG, confirm.toJSONObject().toString(4))
                        Log.i(TAG, confirm.payment.toJSONObject().toString(4))
                        /**
                         * TODO: send 'confirm' (and possibly confirm.getPayment() to your server for verification
                         * or consent completion.
                         * See https://developer.paypal.com/webapps/developer/docs/integration/mobile/verify-mobile-payment/
                         * for more details.

                         * For sample mobile backend interactions, see
                         * https://github.com/paypal/rest-api-sdk-python/tree/master/samples/mobile_backend
                         */

                        resultListener?.onPaymentOk()


                    } catch (e: JSONException) {
                        Log.e(TAG, "an extremely unlikely failure occurred: ", e)
                        resultListener?.onPaymentError()
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i(TAG, "The user canceled.")
                resultListener?.onPaymentCancelled()
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                    TAG,
                    "An invalid Payment or PayPalConfiguration was submitted. Please see the docs."
                )
                resultListener?.onPaymentError()
            }
        } else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                val auth =
                    data?.getParcelableExtra<PayPalAuthorization>(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION)
                if (auth != null) {
                    try {
                        Log.i("FuturePaymentExample", auth.toJSONObject().toString(4))

                        val authorization_code = auth.authorizationCode
                        Log.i("FuturePaymentExample", authorization_code)

                        resultListener?.onPaymentOk()

                    } catch (e: JSONException) {
                        Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e)
                        resultListener?.onPaymentError()
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("FuturePaymentExample", "The user canceled.")
                resultListener?.onPaymentCancelled()
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                    "FuturePaymentExample",
                    "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs."
                )
                resultListener?.onPaymentError()
            }
        } else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
            if (resultCode == Activity.RESULT_OK) {
                val auth = data?.getParcelableExtra<PayPalAuthorization>(
                    PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION
                )
                if (auth != null) {
                    try {
                        Log.i("ProfileSharingExample", auth.toJSONObject().toString(4))

                        val authorization_code = auth.authorizationCode
                        Log.i("ProfileSharingExample", authorization_code)

                        resultListener?.onPaymentOk()

                    } catch (e: JSONException) {
                        Log.e(
                            "ProfileSharingExample",
                            "an extremely unlikely failure occurred: ",
                            e
                        )
                        resultListener?.onPaymentError()
                    }

                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("ProfileSharingExample", "The user canceled.")
                resultListener?.onPaymentCancelled()
            } else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i(
                    "ProfileSharingExample",
                    "Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs."
                )
                resultListener?.onPaymentError()
            }
        }
    }

    override fun setResultListener(resultListener: PaypalInterface.PaypalResultListener) {
        this.resultListener = resultListener
    }

    private fun getItemToBuy(
        price: String,
        itemName: String,
        isoCode: String
    ): PayPalPayment {
        return PayPalPayment(
            BigDecimal(price), isoCode, itemName,
            PayPalPayment.PAYMENT_INTENT_SALE
        ).apply {
            addAppProvidedShippingAddress(this)
        }
    }

    /*
    * Add app-provided shipping address to payment
    */
    private fun addAppProvidedShippingAddress(paypalPayment: PayPalPayment) {
        val shippingAddress = ShippingAddress()
            .recipientName("Mom Parker")
            .line1("52 North Main St.")
            .city("Austin")
            .state("TX")
            .postalCode("78729")
            .countryCode("ES")
        paypalPayment.providedShippingAddress(shippingAddress)
    }

}