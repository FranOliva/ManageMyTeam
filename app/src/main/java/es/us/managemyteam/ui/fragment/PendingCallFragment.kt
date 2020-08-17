package es.us.managemyteam.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.contract.AcceptListener
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.databinding.FragmentPendingCallsBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.adapter.MyCallAdapter
import es.us.managemyteam.ui.viewmodel.CallViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class PendingCallFragment : BaseFragment<FragmentPendingCallsBinding>(), AcceptListener {

    companion object {

        fun newInstance() = PendingCallFragment()
    }

    private val callAdapter = MyCallAdapter(this)
    private val callViewModel: CallViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupCallsObserver()
        setupAcceptPlayerObserver()
        setupRejectPlayerObserver()
    }


    private fun setupList() {
        viewBinding.pendingCallsListCalls.adapter = callAdapter
    }

    private fun setupRejectPlayerObserver() {
        activity?.let {
            callViewModel.getRejectCallData()
                .observe(it, object : ResourceObserver<Boolean>() {
                    override fun onSuccess(response: Boolean?) {
                        response?.let {
                            showInformationDialog("Tu respuesta se envió con éxito")
                        }
                    }

                    override fun onLoading(loading: Boolean) {
                        showLoader(loading)
                    }

                    override fun onError(error: Error) {
                        super.onError(error)
                        showErrorDialog(getString(error.errorMessageId))
                    }
                })
        }
    }

    private fun setupAcceptPlayerObserver() {
        activity?.let {
            callViewModel.getAcceptCallData()
                .observe(it, object : ResourceObserver<Boolean>() {
                    override fun onSuccess(response: Boolean?) {
                        response?.let {
                            showInformationDialog("Tu respuesta se envió con éxito")
                        }
                    }

                    override fun onError(error: Error) {
                        super.onError(error)
                        showErrorDialog(getString(error.errorMessageId))
                    }
                })
        }
    }

    private fun setupCallsObserver() {
        val data = callViewModel.getPendingCallsData()
        viewLifecycleOwner.let {
            data.removeObservers(it)
            data.observe(it, object :
                ResourceObserver<List<EventBo>>() {
                override fun onSuccess(response: List<EventBo>?) {
                    response?.let { list ->
                        callAdapter.setData(list)
                        callAdapter.notifyDataSetChanged()
                    }
                }

                override fun onLoading(loading: Boolean) {
                    showLoader(loading)
                }

                override fun onError(error: Error) {
                    super.onError(error)
                    showErrorDialog(error.serverErrorMessage ?: "",
                        DialogInterface.OnClickListener { _, _ ->
                            popBack()
                        })
                }
            })
        }
        callViewModel.getPendingCalls()
    }

//region BaseFragment

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPendingCallsBinding {
        return FragmentPendingCallsBinding.inflate(inflater, container, false)
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
        callViewModel.acceptCall(uuid)
    }

    override fun onRefused(uuid: String) {
        callViewModel.rejectCall(uuid, "No quiero ir que no vamos a ganar joder")
    }

//endregion

}