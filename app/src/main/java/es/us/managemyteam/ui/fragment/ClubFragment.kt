package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.View.VISIBLE
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.contract.BaseAdapterClickListener
import es.us.managemyteam.data.model.ClubBo
import es.us.managemyteam.databinding.FragmentClubBinding
import es.us.managemyteam.extension.setNavIcon
import es.us.managemyteam.extension.setToolbarTitle
import es.us.managemyteam.extension.show
import kotlinx.android.synthetic.main.fragment_club.*

class ClubFragment : BaseFragment<FragmentClubBinding>(), BaseAdapterClickListener<ClubBo> {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        viewBinding.clubFabEditClub.setOnClickListener {
            findNavController().navigate(R.id.action_club_to_edit_club)
        }
    }

    private fun setupView() {
        viewBinding.clubLabelNameValue.visibility = VISIBLE
        viewBinding.clubLabelDateFundationValue.visibility = VISIBLE
        viewBinding.clubLabelPresidentValue.visibility = VISIBLE
        viewBinding.clubLabelCoachValue.visibility = VISIBLE
        viewBinding.clubLabelLocationValue.visibility = VISIBLE
        viewBinding.clubLabelMailValue.visibility = VISIBLE
        viewBinding.clubLabelPhoneNumberValue.visibility = VISIBLE
        viewBinding.clubLabelWebValue.visibility = VISIBLE
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
