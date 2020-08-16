package es.us.managemyteam.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import es.us.managemyteam.data.model.PaymentBo
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.RepositoryUtil
import es.us.managemyteam.repository.util.Resource

interface PaymentRepository {

    suspend fun getMyPayments(): LiveData<Resource<List<PaymentBo>>>

    suspend fun createPayment(payment: PaymentBo): LiveData<Resource<Boolean>>
}

class PaymentRepositoryImpl : PaymentRepository {

    private val auth = FirebaseAuth.getInstance()
    private val paymentsRef = RepositoryUtil.getDatabaseTable("payments")
    private val paymentData = MutableLiveData<Resource<Boolean>>()
    private val myPaymentsData = MutableLiveData<Resource<List<PaymentBo>>>()

    override suspend fun getMyPayments(): LiveData<Resource<List<PaymentBo>>> {
        myPaymentsData.postValue(null)
        paymentsRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                myPaymentsData.value = Resource.error(Error(serverErrorMessage = error.message))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                myPaymentsData.value =
                    Resource.success(snapshot.children.mapNotNull { it.getValue(PaymentBo::class.java) }
                        .filter { it.userUuid == auth.currentUser?.uid })
            }

        })

        return myPaymentsData
    }

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