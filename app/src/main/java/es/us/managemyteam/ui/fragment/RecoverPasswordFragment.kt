package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.databinding.FragmentRecoverPasswordBinding
import es.us.managemyteam.extension.hide
import es.us.managemyteam.extension.popBack
import es.us.managemyteam.extension.showInformationDialog
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.viewmodel.RecoverPasswordViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class RecoverPasswordFragment : BaseFragment<FragmentRecoverPasswordBinding>() {

    private val recoverPasswordViewModel: RecoverPasswordViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        setupObserver()
    }

    private fun setupObserver() {
        recoverPasswordViewModel.getRecoverPasswordData()
            .observe(viewLifecycleOwner, object : ResourceObserver<Boolean>() {
                override fun onSuccess(response: Boolean?) {
                    showInformationDialog(getString(R.string.user_recover_password_successful))
                    popBack()
                }

                override fun onError(error: Error) {
                    super.onError(error)
                    viewBinding.recoverPasswordEditTextEmail.setError(
                        error.serverErrorMessage ?: getString(error.errorMessageId)
                    )
                }

                override fun onLoading(loading: Boolean) {
                    super.onLoading(loading)
                    viewBinding.recoverPasswordBtnSendRequest.showLoading(loading)
                }
            })
    }

    private fun setupClickListeners() {
        viewBinding.recoverPasswordImgBack.setOnClickListener {
            popBack()
        }

        viewBinding.recoverPasswordBtnSendRequest.setOnClickListener {
            clickOnSendRequest()
        }
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRecoverPasswordBinding {
        return FragmentRecoverPasswordBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.hide()
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.hide()
    }

    private fun clickOnSendRequest() {
        viewBinding.recoverPasswordEditTextEmail.setError(null)
        recoverPasswordViewModel.getRecoverPassword(viewBinding.recoverPasswordEditTextEmail.text.trim())
    }

}