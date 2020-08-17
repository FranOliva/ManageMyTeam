package es.us.managemyteam.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.data.model.Role
import es.us.managemyteam.databinding.FragmentRegistrationBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.viewmodel.RegistrationViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>() {

    private val registrationViewModel: RegistrationViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCreateUserObserver()
        setupClickListeners()
    }

    private fun setupCreateUserObserver() {
        registrationViewModel.getCreateUserData()
            .observe(viewLifecycleOwner, object : ResourceObserver<Boolean>() {
                override fun onSuccess(response: Boolean?) {
                    showInformationDialog(getString(R.string.user_created_successfully))
                }

                override fun onError(error: Error) {
                    super.onError(error)
                    showErrorDialog(
                        getString(error.errorMessageId),
                        DialogInterface.OnClickListener { dialog, _ -> dialog.dismiss() }
                    )
                }

                override fun onLoading(loading: Boolean) {
                    super.onLoading(loading)
                    viewBinding.registrationBtnSendRequest.showLoading(loading)
                }
            })

    }

    private fun setupClickListeners() {
        viewBinding.registrationImgBack.setOnClickListener {
            popBack()
        }
        viewBinding.registrationBtnSendRequest.setOnClickListener {
            clickOnAcceptRegister()
        }
        viewBinding.registrationCheckboxLink.setOnClickListener {
            findNavController().navigate(R.id.action_registration_to_terms_and_conditions)
        }
    }

    private fun clickOnAcceptRegister() {
        val email = viewBinding.registrationEditTextEmail.text.trim()
        val password = viewBinding.registrationEditTextPassword.text.trim()
        val confirmPassword = viewBinding.registrationEditTextConfirmPassword.text.trim()
        val name = viewBinding.registrationEditTextName.text.trim()
        val surname = viewBinding.registrationEditTextSurname.text.trim()
        val phoneNumber = viewBinding.registrationEditTextPhonenumber.text.trim()

        getFocusedView().hideKeyboard()

        registrationViewModel.createUser(
            email,
            password,
            confirmPassword,
            name,
            surname,
            phoneNumber,
            Role.PLAYER
        )
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegistrationBinding {
        return FragmentRegistrationBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.hide()
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.hide()
    }
}