package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.data.model.ClubBo
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.databinding.FragmentEditClubBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.viewmodel.EditClubViewModel
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class EditClubFragment : BaseFragment<FragmentEditClubBinding>() {

    private val editClubViewModel: EditClubViewModel by viewModel()
    private var userIsAdmin = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupUserIsAdminObserver()
        setupClickListeners()
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditClubBinding {
        return FragmentEditClubBinding.inflate(inflater, container, false)
    }

    private fun setupObservers() {
        setupEditClubObserver()
    }

    //region Observers
    private fun setupEditClubObserver() {
        editClubViewModel.getClubData()
            .observe(viewLifecycleOwner, object : ResourceObserver<ClubBo>() {
                override fun onSuccess(response: ClubBo?) {
                    response?.let {
                        setupView(it)
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

                override fun onLoading(loading: Boolean) {
                    super.onLoading(loading)
                    if (loading) {
                        viewBinding.clubProgressBar.startAnimation()
                    } else {
                        viewBinding.clubProgressBar.stopAnimationAndHide()
                    }
                }

            })
    }

    private fun setupUserIsAdminObserver(){
        editClubViewModel.getUserData().observe(viewLifecycleOwner, object : ResourceObserver<UserBo>(){
            override fun onSuccess(response: UserBo?) {
                response?.let {
                    userIsAdmin = it.isAdmin()
                    editClubViewModel.getClub()
                }
            }
        })
        editClubViewModel.getUser()
    }

    private fun setupClickListeners() {
        viewBinding.editClubFabSave.setOnClickListener {
            findNavController().navigate(R.id.action_club_to_edit_club)
            clickOnSave()
        }
    }

    private fun setupView(club : ClubBo) {
        viewBinding.editClubEditName.setText(club.name)
        viewBinding.editClubEditDateFundation.setText(club.dateFundation)
        viewBinding.editClubEditPresident.setText(club.president)
        viewBinding.editClubEditCoach.setText(club.coach)
        viewBinding.editClubEditLocation.setText(club.location)
        viewBinding.editClubEditMail.setText(club.mail)
        viewBinding.editClubEditPhoneNumber.setText(club.phoneNumber.toString())
        viewBinding.editClubEditWeb.setText(club.web)

        viewBinding.editClubFabSave.visibility = if (userIsAdmin){
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    //region Clicks

    private fun clickOnSave() {
        val name = viewBinding.editClubEditName.text.trim()
        val sdf = SimpleDateFormat(getString(R.string.date_time_format), Locale.getDefault())
        val dateFundation = if (!viewBinding.editClubEditDateFundation.text.isBlank()) {
            sdf.parse(viewBinding.editClubEditDateFundation.text)
        } else {
            null
        }
        val president = viewBinding.editClubEditPresident.text.trim()
        val coach = viewBinding.editClubEditCoach.text.trim()
        val location = viewBinding.editClubEditLocation.text.trim()
        val mail = viewBinding.editClubEditMail.text.trim()
        val phoneNumber = viewBinding.editClubEditPhoneNumber.text.trim()
        val web = viewBinding.editClubEditWeb.text.trim()

        getFocusedView().hideKeyboard()

        editClubViewModel.editClub(
            name,
            dateFundation,
            president,
            coach,
            location,
            phoneNumber,
            mail,
            web
        )
    }


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