package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.databinding.FragmentSelectPlayersBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.adapter.UnableUsersAdapter
import es.us.managemyteam.ui.viewmodel.UnableUsersViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class UnableUsersFragment : BaseFragment<FragmentSelectPlayersBinding>() {

    private var unableUsersAdapter: UnableUsersAdapter? = null
    private val unableUsersViewModel: UnableUsersViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupUsersObserver()
        setupClickListener()
    }

    private fun setupClickListener() {
        viewBinding.selectPlayersBtnSave.setOnClickListener {

        }
    }

    private fun setupUsersObserver() {
        unableUsersViewModel.getUsersData()
            .observe(viewLifecycleOwner, object : ResourceObserver<List<UserBo>>() {
                override fun onSuccess(response: List<UserBo>?) {

                }

            })
        unableUsersViewModel.getUsers()
    }

    private fun setupList() {
        viewBinding.selectPlayersListPlayers.apply {
            if (unableUsersAdapter == null) {
                unableUsersAdapter = UnableUsersAdapter()
            }
            unableUsersAdapter?.let {
                adapter = it
            }
        }
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSelectPlayersBinding {
        return FragmentSelectPlayersBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.apply {
            setToolbarTitle(getString(R.string.create_event_select_call))
            setNavIcon(ContextCompat.getDrawable(context, R.drawable.ic_back))
            setNavAction { popBack() }
            show()
        }
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.hide()
    }
}