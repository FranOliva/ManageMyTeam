package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.data.model.PaymentBo
import es.us.managemyteam.databinding.FragmentPaymentBinding
import es.us.managemyteam.extension.setNavIcon
import es.us.managemyteam.extension.setToolbarTitle
import es.us.managemyteam.extension.show
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.viewmodel.MyPaymentsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MyPaymentsFragment : BaseFragment<FragmentPaymentBinding>() {

    private val paymentsViewModel: MyPaymentsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupClickListeners()
    }

    private fun setupObserver() {
        paymentsViewModel.getMyPaymentsData()
            .observe(viewLifecycleOwner, object : ResourceObserver<List<PaymentBo>>() {
                override fun onSuccess(response: List<PaymentBo>?) {
                    TODO("Not yet implemented")
                }

            })
    }

    private fun setupClickListeners() {
        viewBinding.paymentBtnPay.setOnClickListener {
            findNavController().navigate(R.id.action_my_payments_to_create_payment)
        }
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPaymentBinding {
        return FragmentPaymentBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.apply {
            setToolbarTitle(getString(R.string.club))
            setNavIcon(null)
            show()
        }
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.show()
    }

}