package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.databinding.FragmentChatBinding
import es.us.managemyteam.extension.setNavIcon
import es.us.managemyteam.extension.setToolbarTitle
import es.us.managemyteam.extension.show
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.adapter.MessageAdapter
import es.us.managemyteam.ui.viewmodel.EventsViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ChatFragment : BaseFragment<FragmentChatBinding>() {

    private var messageAdapter: MessageAdapter = MessageAdapter()
    private val eventsViewModel: EventsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUserObserver()

    }

    private fun setupUserObserver() {
        eventsViewModel.getUserData()
            .observe(viewLifecycleOwner, object : ResourceObserver<UserBo>() {
                override fun onSuccess(response: UserBo?) {
                    response?.let {
                        setupList(it.uuid ?: "")
                    }
                }
            })
        eventsViewModel.getUser()
    }


    private fun setupList(currentUserId: String) {
        viewBinding.chatListMessages.apply {
            adapter = messageAdapter.apply { setCurrentUserId(currentUserId) }
        }
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatBinding {
        return FragmentChatBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.apply {
            setToolbarTitle(getString(R.string.events))
            setNavIcon(null)
            show()
        }
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.show()
    }

}