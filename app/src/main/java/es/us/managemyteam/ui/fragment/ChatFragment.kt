package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.data.model.MessageBo
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.databinding.FragmentChatBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.adapter.MessageAdapter
import es.us.managemyteam.ui.view.input.EditableTextChangeListener
import es.us.managemyteam.ui.viewmodel.ChatViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ChatFragment : BaseFragment<FragmentChatBinding>() {

    private var messageAdapter: MessageAdapter = MessageAdapter()
    private val chatViewModel: ChatViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUserObserver()
        setupWatcher()
        viewBinding.chatBtnSend.setOnClickListener {
            clickOnSend()
        }
    }

    private fun setupUserObserver() {
        chatViewModel.getMessagesData().observe(viewLifecycleOwner, object :
            ResourceObserver<List<MessageBo>>() {
            override fun onSuccess(response: List<MessageBo>?) {
                response?.let {
                    messageAdapter.setData(it)
                    messageAdapter.notifyDataSetChanged()
                    viewBinding.chatListMessages.scrollToPosition(messageAdapter.data.lastIndex)
                }
            }
        })
        chatViewModel.getUserData()
            .observe(viewLifecycleOwner, object : ResourceObserver<UserBo>() {
                override fun onSuccess(response: UserBo?) {
                    chatViewModel.currentUser = response
                    response?.let {
                        setupList(it.uuid ?: "")
                        chatViewModel.getMessages()
                    }
                }
            })
        chatViewModel.getUser()
    }


    private fun setupList(currentUserId: String) {
        viewBinding.chatListMessages.apply {
            adapter = messageAdapter.apply { setCurrentUserId(currentUserId) }
        }
    }

    private fun setupWatcher() {
        viewBinding.chatInputMessage.listener = object : EditableTextChangeListener {
            override fun onTextChanged(text: String) {
                viewBinding.chatBtnSend.isEnabled = !text.isBlank()
                viewBinding.chatBtnSend.setBackgroundResource(
                    if (text.isBlank()) {
                        R.color.primaryWithAlpha
                    } else {
                        R.color.primary
                    }
                )
            }
        }
    }

    private fun clickOnSend() {
        chatViewModel.currentUser?.let {
            val message = viewBinding.chatInputMessage.text.trim()
            chatViewModel.postMessage(message, it.uuid ?: "", it.name ?: "")
            viewBinding.chatInputMessage.setText("")
            getFocusedView().hideKeyboard()
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