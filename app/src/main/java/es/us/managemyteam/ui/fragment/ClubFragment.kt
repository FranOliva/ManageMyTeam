package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
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

    }

    private fun setupView() {
        viewBinding.apply {
            clubLabelNameValue.text = item.name
            }
            clubAdapter?.let {
                adapter = it
            }
        }
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
