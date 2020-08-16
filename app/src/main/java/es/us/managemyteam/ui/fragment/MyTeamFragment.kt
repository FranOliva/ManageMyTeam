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
import es.us.managemyteam.databinding.FragmentMyTeamBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.adapter.MyTeamAdapter
import es.us.managemyteam.ui.viewmodel.MyTeamViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MyTeamFragment : BaseFragment<FragmentMyTeamBinding>() {

    private val myTeamAdapter = MyTeamAdapter()
    private val myTeamViewModel: MyTeamViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupUserObserver()
        setupPlayersObserver()
    }

    private fun setupList() {
        viewBinding.myTeamListPlayers.adapter = myTeamAdapter
    }

    private fun setupUserObserver() {
        myTeamViewModel.getUserData().observe(viewLifecycleOwner, object :
            ResourceObserver<UserBo>() {
            override fun onSuccess(response: UserBo?) {
                response?.let {
                    // TODO call get all users
                }
            }
        })
        myTeamViewModel.getUser()
    }

    private fun setupPlayersObserver() {
        myTeamViewModel.getPlayersData().observe(viewLifecycleOwner, object :
            ResourceObserver<List<UserBo>>() {
            override fun onSuccess(response: List<UserBo>?) {
                response?.let {
                    myTeamAdapter.setData(it)
                    myTeamAdapter.notifyDataSetChanged()
                }
            }
        })
    }

    //region BaseFragment

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyTeamBinding {
        return FragmentMyTeamBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.apply {
            setToolbarTitle(getString(R.string.my_team_title))
            setNavIcon(ContextCompat.getDrawable(context, R.drawable.ic_back))
            setNavAction {
                popBack()
            }
            show()
        }
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.show()
    }

    //endregion

}