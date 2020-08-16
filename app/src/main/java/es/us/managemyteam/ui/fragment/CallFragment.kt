package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.contract.AcceptListener
import es.us.managemyteam.databinding.FragmentMyCallsBinding
import es.us.managemyteam.extension.setNavIcon
import es.us.managemyteam.extension.setToolbarTitle
import es.us.managemyteam.extension.show
import es.us.managemyteam.ui.adapter.MyCallAdapter

class CallFragment : BaseFragment<FragmentMyCallsBinding>(), AcceptListener {

    private val callAdapter = MyCallAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
    }

    private fun setupList() {
        viewBinding.myCallsListCalls.adapter = callAdapter
    }

    //region BaseFragment

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyCallsBinding {
        return FragmentMyCallsBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.apply {
            setToolbarTitle(getString(R.string.calls))
            setNavIcon(null)
            show()
        }
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.show()
    }

    //endregion

    //region AcceptListener

    override fun onAccepted(uuid: String) {
        // viewmodel call onaccepted
    }

    override fun onRefused(uuid: String) {
        // viewmodel call onrefused
    }

    //endregion

}