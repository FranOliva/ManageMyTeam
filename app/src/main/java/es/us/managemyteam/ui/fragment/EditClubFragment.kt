package es.us.managemyteam.ui.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import es.us.managemyteam.R
import es.us.managemyteam.databinding.FragmentEditClubBinding
import es.us.managemyteam.extension.*

class EditClubFragment : BaseFragment<FragmentEditClubBinding>() {

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEditClubBinding {
        return FragmentEditClubBinding.inflate(inflater, container, false)
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