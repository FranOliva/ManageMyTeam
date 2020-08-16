package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.databinding.FragmentEditPasswordBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.viewmodel.UpdatePasswordViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class EditPasswordFragment : BaseFragment<FragmentEditPasswordBinding>() {

    private val editPasswordViewModel: UpdatePasswordViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        setupClickListener()
    }

    private fun setupClickListener() {
        viewBinding.editPasswordBtnSendRequest.setOnClickListener {
            clickOnUpdate()
        }
    }

    private fun clickOnUpdate() {
        val currentPassword = viewBinding.editPasswordEditTextConfirmOldPassword.text
        val newPassword = viewBinding.editPasswordEditTextNewPassword.text
        val confirmPassword = viewBinding.editPasswordEditTextConfirmNewPassword.text

        editPasswordViewModel.getUpdatePassword(currentPassword, newPassword, confirmPassword)
    }

    private fun setupObserver() {
        editPasswordViewModel.getUpdatePasswordData()
            .observe(viewLifecycleOwner, object : ResourceObserver<Boolean>() {
                override fun onSuccess(response: Boolean?) {
                    response?.let {
                        Toast.makeText(
                            context,
                            getString(R.string.update_user_success),
                            Toast.LENGTH_LONG
                        ).show()
                        popBack()
                    }
                }

                override fun onError(error: Error) {
                    super.onError(error)
                    showErrorDialog(getString(error.errorMessageId))
                }

                override fun onLoading(loading: Boolean) {
                    super.onLoading(loading)
                    viewBinding.editPasswordBtnSendRequest.showLoading(loading)
                }
            })
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditPasswordBinding {
        return FragmentEditPasswordBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.apply {
            setToolbarTitle(getString(R.string.edit_user_password))
            setNavIcon(ContextCompat.getDrawable(context, R.drawable.ic_back))
            setNavAction { popBack() }
            show()
        }
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.hide()
    }
}