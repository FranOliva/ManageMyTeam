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
import es.us.managemyteam.contract.AcceptPlayerListener
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.databinding.FragmentAcceptsPlayersBinding
import es.us.managemyteam.extension.setNavIcon
import es.us.managemyteam.extension.setToolbarTitle
import es.us.managemyteam.extension.show
import es.us.managemyteam.extension.showErrorDialog
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.adapter.AcceptPlayersAdapter
import es.us.managemyteam.ui.viewmodel.AcceptPlayersViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class AcceptPlayersFragment : BaseFragment<FragmentAcceptsPlayersBinding>(), AcceptPlayerListener {

    private var acceptPlayersAdapter: AcceptPlayersAdapter? = null
    private val acceptsPlayersViewModel: AcceptPlayersViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupPlayersObserver()
        setupAcceptPlayerObserver()
    }

    private fun setupAcceptPlayerObserver() {
        acceptsPlayersViewModel.getAcceptPlayerData()
            .observe(viewLifecycleOwner, object : ResourceObserver<Boolean>() {
                override fun onSuccess(response: Boolean?) {
                    response?.let {
                        if (it) {
                            acceptsPlayersViewModel.getPlayers()
                        }
                    }
                }

            })
    }

    private fun setupPlayersObserver() {
        acceptsPlayersViewModel.getPlayersData()
            .observe(viewLifecycleOwner, object : ResourceObserver<List<UserBo>>() {
                override fun onSuccess(response: List<UserBo>?) {
                    response?.let {
                        acceptPlayersAdapter?.apply {
                            setData(it)
                            notifyDataSetChanged()
                        }
                    }
                }

                override fun onLoading(loading: Boolean) {
                    super.onLoading(loading)
                    // TODO
                }

                override fun onError(error: Error) {
                    super.onError(error)
                    showErrorDialog(getString(error.errorMessageId))
                }
            })
        acceptsPlayersViewModel.getPlayers()
    }

    private fun setupList() {
        viewBinding.acceptsPlayerListUsers.apply {
            if (acceptPlayersAdapter == null) {
                acceptPlayersAdapter = AcceptPlayersAdapter(this@AcceptPlayersFragment)
            }
            acceptPlayersAdapter?.let {
                adapter = it
            }
        }
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentAcceptsPlayersBinding {
        return FragmentAcceptsPlayersBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.apply {
            setToolbarTitle(getString(R.string.administration_accept_players))
            setNavIcon(ContextCompat.getDrawable(context, R.drawable.ic_back))
            show()
        }
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.show()
    }

    override fun onPlayerAccepted(user: UserBo) {
        acceptsPlayersViewModel.acceptPlayer(user)
    }

    override fun onPlayerRefused(user: UserBo) {
        Toast.makeText(context, "Rechazado", Toast.LENGTH_LONG).show()
    }
}