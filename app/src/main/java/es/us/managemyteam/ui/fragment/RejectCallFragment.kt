package es.us.managemyteam.ui.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.contract.AcceptListener
import es.us.managemyteam.data.model.CallStatus
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.databinding.FragmentRejectedCallsBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.adapter.MyCallAdapter
import es.us.managemyteam.ui.viewmodel.RejectedCallViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class RejectCallFragment : BaseFragment<FragmentRejectedCallsBinding>(), AcceptListener {

    companion object {
        fun newInstance() = RejectCallFragment()
    }

    private val callAdapter = MyCallAdapter(this, CallStatus.DENIED)
    private val rejectedCallViewModel: RejectedCallViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupList()
        setupCallsObserver()
        setupAcceptPlayerObserver()
    }

    private fun setupList() {
        viewBinding.rejectedCallsListCalls.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )
        viewBinding.rejectedCallsListCalls.adapter = callAdapter
    }

    private fun setupAcceptPlayerObserver() {
        activity?.let {
            rejectedCallViewModel.getAcceptCallData()
                .observe(viewLifecycleOwner, object : ResourceObserver<Boolean>() {
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

    private fun setupCallsObserver() {
        val data = rejectedCallViewModel.getRejectedCallsData()

        viewLifecycleOwner.let {
            data.removeObservers(it)
            data.observe(it, object :
                ResourceObserver<List<EventBo>>() {
                override fun onSuccess(response: List<EventBo>?) {
                    response?.let { list ->
                        callAdapter.setData(list)
                        callAdapter.notifyDataSetChanged()
                        viewBinding.rejectedCallsEmptyResults.root.visibility =
                            if (list.isEmpty()) {
                                View.VISIBLE
                            } else {
                                View.GONE
                            }
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
        rejectedCallViewModel.getRejectedCalls()
    }

//region BaseFragment

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRejectedCallsBinding {
        return FragmentRejectedCallsBinding.inflate(inflater, container, false)
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
        rejectedCallViewModel.acceptCall(uuid)
    }

    override fun onRefused(uuid: String) {
        // no-op
    }

//endregion

}