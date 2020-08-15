package es.us.managemyteam.ui.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.data.model.ClubBo
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.databinding.FragmentEditClubBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.viewmodel.EditClubViewModel
import es.us.managemyteam.util.DateUtil
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

const val CLUB_DATE_FORMAT = "dd/MM/yyyy"

class EditClubFragment : BaseFragment<FragmentEditClubBinding>() {

    private val editClubViewModel: EditClubViewModel by viewModel()
    private var userIsAdmin = false
    private var clubUuid = ""
    private var selectedDate = Calendar.getInstance()

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
        setupGetClubObserver()
        setupEditClubObserver()
    }

    //region Observers
    private fun setupGetClubObserver() {
        editClubViewModel.getClubData()
            .observe(viewLifecycleOwner, object : ResourceObserver<ClubBo>() {
                override fun onSuccess(response: ClubBo?) {
                    response?.let {
                        clubUuid = it.uuid ?: ""
                        setupView(it)
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
                        viewBinding.editClubContainerContent.visibility = GONE
                        viewBinding.clubProgressBar.startAnimation()
                    } else {
                        viewBinding.editClubContainerContent.visibility = VISIBLE
                        viewBinding.clubProgressBar.stopAnimationAndHide()
                    }
                }

            })
    }

    private fun setupEditClubObserver() {
        editClubViewModel.editClubData()
            .observe(viewLifecycleOwner, object : ResourceObserver<Boolean>() {
                override fun onSuccess(response: Boolean?) {
                    Toast.makeText(
                        context,
                        getString(R.string.edit_club_success),
                        Toast.LENGTH_SHORT
                    ).show()
                    popBack()
                }

                override fun onError(error: Error) {
                    super.onError(error)
                    showErrorDialog(getString(error.errorMessageId))
                }

                override fun onLoading(loading: Boolean) {
                    super.onLoading(loading)
                    viewBinding.editClubBtnSave.showLoading(loading)
                }
            })
    }

    private fun setupUserIsAdminObserver() {
        editClubViewModel.getUserData()
            .observe(viewLifecycleOwner, object : ResourceObserver<UserBo>() {
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
        setupDateClickListener()
        viewBinding.editClubBtnSave.setOnClickListener {
            clickOnSave()
        }
    }

    private fun setupView(club: ClubBo) {
        viewBinding.editClubEditName.setText(club.name)
        viewBinding.editClubEditDateFundation.setText(club.dateFundation?.let {
            DateUtil.format(
                it,
                CLUB_DATE_FORMAT
            )
        })
        viewBinding.editClubEditPresident.setText(club.president)
        viewBinding.editClubEditCoach.setText(club.coach)
        viewBinding.editClubEditLocation.setText(club.location)
        viewBinding.editClubEditMail.setText(club.mail)
        viewBinding.editClubEditPhoneNumber.setText(club.phoneNumber.toString())
        viewBinding.editClubEditWeb.setText(club.web)
    }

    //region Clicks

    private fun clickOnSave() {
        val name = viewBinding.editClubEditName.text.trim()
        val sdf = SimpleDateFormat(CLUB_DATE_FORMAT, Locale.getDefault())
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
            clubUuid,
            name,
            dateFundation,
            location,
            president,
            coach,
            phoneNumber,
            mail,
            web
        )
    }

    private fun setupDateClickListener() {

        viewBinding.editClubEditDateFundation.clickListener {
            showDateDialog(
                selectedDate,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    updateSelectedDate(dayOfMonth, month + 1, year)
                })
        }
    }

    //endregion

    //region Dialogs

    private fun updateSelectedDate(day: Int, month: Int, year: Int) {
        selectedDate = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, day)
            set(Calendar.MONTH, month)
            set(Calendar.YEAR, year)
        }
        viewBinding.editClubEditDateFundation.setText(
            String.format(
                getString(R.string.edit_club_date_format),
                day,
                month,
                year
            )
        )
    }

    private fun showDateDialog(
        date: Calendar,
        dateListener: DatePickerDialog.OnDateSetListener
    ) {
        val year = date[Calendar.YEAR]
        val month = date[Calendar.MONTH]
        val day = date[Calendar.DAY_OF_MONTH]

        context?.let {
            DatePickerDialog(
                it,
                R.style.DialogTheme,
                dateListener,
                year,
                month,
                day
            ).show()

        }
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
        bottomNavigationView.hide()
    }


}