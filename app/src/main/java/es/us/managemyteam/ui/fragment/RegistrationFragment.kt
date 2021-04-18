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
import es.us.managemyteam.constant.RegistrationError
import es.us.managemyteam.data.model.RegistrationBo
import es.us.managemyteam.data.model.Role
import es.us.managemyteam.databinding.FragmentRegistrationBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.viewmodel.RegistrationViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>() {

    private val registrationViewModel: RegistrationViewModel by viewModel()
    private var currentRegistration: RegistrationBo = RegistrationBo()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCreateUserObserver()
        setupClickListeners()
        setupCurrentUserObserver()
        setupGetCurrentNewUserObserver()
    }

    private fun setupCreateUserObserver() {
        registrationViewModel.getCreateUserData()
            .observe(viewLifecycleOwner, object : ResourceObserver<Boolean>() {
                override fun onSuccess(response: Boolean?) {
                    showInformationDialog(getString(R.string.user_created_successfully))
                    popBack()
                }

                override fun onError(error: Error) {
                    super.onError(error)
                    processWithError(error)
                }

                override fun onLoading(loading: Boolean) {
                    super.onLoading(loading)
                    viewBinding.registrationBtnSendRequest.showLoading(loading)
                }
            })

    }

    private fun processWithError(error: Error) {
        val errorMessage = getString(error.errorMessageId)
        when(RegistrationError.values()[error.errorActionId]){
            RegistrationError.EMPTY_FIELDS -> {
                if (viewBinding.registrationEditTextName.text.isBlank()) {
                    viewBinding.registrationEditTextName.setError(errorMessage)
                }
                if (viewBinding.registrationEditTextSurname.text.isBlank()) {
                    viewBinding.registrationEditTextSurname.setError(errorMessage)
                }
                if (viewBinding.registrationEditTextEmail.text.isBlank()) {
                    viewBinding.registrationEditTextEmail.setError(errorMessage)
                }
                if (viewBinding.registrationEditTextPhonenumber.text.isBlank()) {
                    viewBinding.registrationEditTextPhonenumber.setError(errorMessage)
                }
                if (viewBinding.registrationEditTextPassword.text.isBlank()) {
                    viewBinding.registrationEditTextPassword.setError(errorMessage)
                }
                if (viewBinding.registrationEditTextConfirmPassword.text.isBlank()) {
                    viewBinding.registrationEditTextConfirmPassword.setError(errorMessage)
                }
            }
            RegistrationError.NOT_AN_EMAIL -> {
                viewBinding.registrationEditTextEmail.setError(errorMessage)
            }
            RegistrationError.NOT_A_PHONE -> {
                viewBinding.registrationEditTextPhonenumber.setError(errorMessage)
            }
            RegistrationError.PASSWORDS_NOT_FILL -> {
                viewBinding.registrationEditTextPassword.setError(errorMessage)
            }
        }
    }

    private fun clearErrors() {
        viewBinding.registrationEditTextName.setError(null)
        viewBinding.registrationEditTextSurname.setError(null)
        viewBinding.registrationEditTextEmail.setError(null)
        viewBinding.registrationEditTextPhonenumber.setError(null)
        viewBinding.registrationEditTextPassword.setError(null)
        viewBinding.registrationEditTextConfirmPassword.setError(null)
    }

    private fun setupClickListeners() {
        viewBinding.registrationImgBack.setOnClickListener {
            popBack()
        }
        viewBinding.registrationBtnSendRequest.setOnClickListener {
            clickOnAcceptRegister()
        }
        viewBinding.registrationCheckboxLink.setOnClickListener {
            setCurrentRegistration()
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

        clearErrors()
        getFocusedView().hideKeyboard()


        if (viewBinding.registrationCheckbox.isChecked) {
            registrationViewModel.createUser(
                email,
                password,
                confirmPassword,
                name,
                surname,
                phoneNumber,
                Role.PLAYER
            )
        } else {
            viewBinding.registrationCheckboxLink.startShakeAnimation()
//            showInformationDialog(getString(R.string.checkbox_error)) // TODO
        }
    }

    private fun setupCurrentUserObserver() {
        registrationViewModel.getCurrentRegistrationData()
            .observe(viewLifecycleOwner, object : ResourceObserver<RegistrationBo>() {
                override fun onSuccess(response: RegistrationBo?) {
                    currentRegistration = response ?: RegistrationBo()
                }

                override fun onError(error: Error) {
                    super.onError(error)
                    currentRegistration = RegistrationBo()
                }

            })
    }

    private fun setupGetCurrentNewUserObserver() {
        registrationViewModel.getCurrentRegistrationData()
            .observe(viewLifecycleOwner, object : ResourceObserver<RegistrationBo>() {
                override fun onSuccess(response: RegistrationBo?) {
                    response?.let {
                        currentRegistration = it
                        setupCurrentUser(it)
                    }
                }

                override fun onError(error: Error) {
                    super.onError(error)
                    showErrorDialog(
                        getString(error.errorMessageId),
                        getDefaultDialogErrorListener()
                    )
                }
            })
        registrationViewModel.getCurrentRegistration()
    }

    private fun setupCurrentUser(registration: RegistrationBo) {
        viewBinding.registrationEditTextName.setText(registration.user.name)
        viewBinding.registrationEditTextSurname.setText(registration.user.surname)
        viewBinding.registrationEditTextEmail.setText(registration.user.email)
        viewBinding.registrationEditTextPhonenumber.setText(registration.user.phoneNumber)
        viewBinding.registrationCheckbox.isChecked = registration.termsChecked
    }

    private fun setCurrentRegistration() {
        val name = viewBinding.registrationEditTextName.text.trim()
        val surname = viewBinding.registrationEditTextSurname.text.trim()
        val email = viewBinding.registrationEditTextEmail.text.trim()
        val phoneNumber = viewBinding.registrationEditTextPhonenumber.text.trim()
        val termsChecked = viewBinding.registrationCheckbox.isChecked

        currentRegistration.apply {
            this.user.name = name
            this.user.surname = surname
            this.user.email = email
            this.user.phoneNumber = phoneNumber
            this.termsChecked = termsChecked
        }

        registrationViewModel.setCurrentRegistration(currentRegistration)
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