package es.us.managemyteam.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import es.us.managemyteam.data.model.PaymentBo
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.RepositoryUtil
import es.us.managemyteam.repository.util.Resource

interface PaymentRepository {

    suspend fun createPayment(payment: PaymentBo): LiveData<Resource<Boolean>>
}

class PaymentRepositoryImpl : PaymentRepository {

    private val auth = FirebaseAuth.getInstance()
    private val paymentsRef = RepositoryUtil.getDatabaseTable("payments")
    private val paymentData = MutableLiveData<Resource<Boolean>>()

    override suspend fun createPayment(payment: PaymentBo): MutableLiveData<Resource<Boolean>> {
        val newPayment = payment.apply {
            uuid = auth.currentUser?.uid
        }
        paymentData.postValue(null)
        paymentsRef.push().setValue(newPayment).addOnCompleteListener {
            if (it.isSuccessful) {
                paymentData.value = Resource.success(true)
            } else {
                paymentData.value =
                    Resource.error(Error(serverErrorMessage = it.exception?.message))
            }
        }

        return paymentData
    }

}