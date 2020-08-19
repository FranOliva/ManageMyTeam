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
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.databinding.FragmentRegistrationBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.viewmodel.RegistrationViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>() {

    private val registrationViewModel: RegistrationViewModel by viewModel()
    private var currentNewUser: UserBo = UserBo()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCreateUserObserver()
        setupClickListeners()
        setupCurrentUserObserver()
        setupGetCurrentNewUserObserver()
        toTerms()
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

        setCurrentUser()
    }

    private fun toTerms() {

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


        if (viewBinding.registrationCheckbox.isChecked){
            registrationViewModel.createUser(
                email,
                password,
                confirmPassword,
                name,
                surname,
                phoneNumber,
                Role.PLAYER
            )
        }else{
            showInformationDialog(getString(R.string.checkbox_error))
        }
    }

    private fun setupCurrentUserObserver() {
        registrationViewModel.getCurrentNewUserData()
            .observe(viewLifecycleOwner, object : ResourceObserver<UserBo>() {
                override fun onSuccess(response: UserBo?) {
                    currentNewUser = response ?: UserBo()
                }

                override fun onError(error: Error) {
                    super.onError(error)
                    currentNewUser = UserBo()
                }

            })
    }

    private fun setupGetCurrentNewUserObserver() {
        registrationViewModel.getCurrentNewUserData()
            .observe(viewLifecycleOwner, object : ResourceObserver<UserBo>() {
                override fun onSuccess(response: UserBo?) {
                    response?.let {
                        currentNewUser = it
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
        registrationViewModel.getCurrentNewUser()
    }

    private fun setupCurrentUser(user: UserBo) {
        viewBinding.registrationEditTextName.setText(user.name)
        viewBinding.registrationEditTextSurname.setText(user.surname)
        viewBinding.registrationEditTextEmail.setText(user.email)
        viewBinding.registrationEditTextPhonenumber.setText(user.phoneNumber)
    }

    private fun setCurrentUser() {
        val name = viewBinding.registrationEditTextName.text.trim()
        val surname = viewBinding.registrationEditTextSurname.text.trim()
        val email = viewBinding.registrationEditTextEmail.text.trim()
        val phoneNumber = viewBinding.registrationEditTextPhonenumber.text.trim()

        currentNewUser.apply {
            this.name = name
            this.surname = surname
            this.email = email
            this.phoneNumber = phoneNumber
        }

        registrationViewModel.setCurrentNewUser(currentNewUser)
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