package es.us.managemyteam.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.databinding.FragmentEditClubBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.viewmodel.EditClubViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class EditClubFragment : BaseFragment<FragmentEditClubBinding>() {

    private val editClubViewModel: EditClubViewModel by viewModel()

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditClubBinding {
        return FragmentEditClubBinding.inflate(inflater, container, false)
    }

    private fun setupObservers() {
       // setupEditClubObserver()
    }

    /*

    //region Observers
    private fun setupEditClubObserver() {
        editClubViewModel.editClubData()
            .observe(viewLifecycleOwner, object : ResourceObserver<Boolean>() {
                override fun onSuccess(response: Boolean?) {
                    response?.let {
                        Toast.makeText(
                            context,
                            getString(R.string.edit_club_success),
                            Toast.LENGTH_LONG
                        )
                            .show()
                        popBack()
                    }
                }

                override fun onError(error: Error) {
                    super.onError(error)
                    showErrorDialog(
                        getString(error.errorMessageId)
                    )
                }

            })
    }



     */
    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.apply {
            setToolbarTitle(getString(R.string.edit_club_title))
            setNavIcon(ContextCompat.getDrawable(context, R.drawable.ic_back))
            setNavAction { popBack() }
            show()
        }
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.show()
    }


}