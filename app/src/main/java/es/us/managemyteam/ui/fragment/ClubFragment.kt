package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.contract.BaseAdapterClickListener
import es.us.managemyteam.data.model.ClubBo
import es.us.managemyteam.databinding.FragmentClubBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.repository.util.ResourceObserver
import es.us.managemyteam.ui.viewmodel.ClubViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class ClubFragment : BaseFragment<FragmentClubBinding>(), BaseAdapterClickListener<ClubBo> {

    private val clubViewModel: ClubViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClubObserver()
        setupClickListeners()

    }

    private fun setupClubObserver() {
        clubViewModel.getClubData()
            .observe(viewLifecycleOwner, object : ResourceObserver<ClubBo>() {
            override fun onSuccess(response: ClubBo?) {
                response?.let { setupView(it) }
            }

            override fun onError(error: Error) {
                super.onError(error)
                showErrorDialog(
                    getString(error.errorMessageId),
                    getDefaultDialogErrorListener()
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
        clubViewModel.getClub()
    }

    private fun setupClickListeners() {
        viewBinding.clubFabEdit.setOnClickListener {
            findNavController().navigate(R.id.action_club_to_edit_club)
        }
    }

    private fun setupView(club : ClubBo) {
        viewBinding.clubLabelNameValue.text = club.name
        viewBinding.clubLabelDateFundationValue.text = club.dateFundation
        viewBinding.clubLabelPresidentValue.text = club.president
        viewBinding.clubLabelCoachValue.text = club.coach
        viewBinding.clubLabelLocationValue.text = club.location
        viewBinding.clubLabelMailValue.text = club.mail
        viewBinding.clubLabelPhoneNumberValue.text = club.phoneNumber
        viewBinding.clubLabelWebValue.text = club.web
    }

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentClubBinding {
        return FragmentClubBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.apply {
            setToolbarTitle(getString(R.string.club))
            setNavIcon(null)
            show()
        }
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.show()
    }

    override fun onAdapterItemClicked(item: ClubBo, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
