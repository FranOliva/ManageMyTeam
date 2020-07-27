package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.databinding.FragmentLoginBinding
import es.us.managemyteam.extension.hide
import es.us.managemyteam.extension.showErrorDialog
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.viewmodel.LoginViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : BaseFragment<FragmentLoginBinding>() {

    private val loginViewModel: LoginViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupLoginObserver()
        setupEnterClickListener()
    }

    private fun setupViews() {
        viewBinding.loginLabelGoToRegistration.setOnClickListener {
            findNavController().navigate(R.id.action_login_to_registration)
        }
    }

    private fun setupLoginObserver() {
        loginViewModel.getLoginData()
            .observe(viewLifecycleOwner, object : ResourceObserver<UserBo>() {
                override fun onSuccess(response: UserBo?) {
                    response?.let {
                        findNavController().navigate(R.id.action_login_to_events)
                    }
                }

                override fun onError(error: Error) {
                    super.onError(error)
                    showErrorDialog("Ha ocurrido un error en el inicio de sesión")
                }

                override fun onLoading(loading: Boolean) {
                    super.onLoading(loading)
                    viewBinding.loginBtnEnter.showLoading(loading)
                }
            })
    }

    private fun setupEnterClickListener() {
        viewBinding.loginBtnEnter.setOnClickListener {
            val email = viewBinding.loginEditTextEmail.text.trim()
            val password = viewBinding.loginEditTextPassword.text.trim()

            loginViewModel.login(email, password)
        }
    }


    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.hide()
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.hide()
    }
}