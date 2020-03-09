package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.FirebaseDatabase
import es.us.managemyteam.extension.setNavAction
import es.us.managemyteam.ui.activity.MainActivity

abstract class BaseFragment<Any : ViewBinding> : Fragment() {

    companion object {
        const val ANIMATION_DELAYER_TIME = 200L
    }

    lateinit var viewBinding: Any

    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): Any

    abstract fun setupToolbar(toolbar: Toolbar)

    abstract fun setupBottomBar(bottomNavigationView: BottomNavigationView)

    fun getToolbar() = (activity as MainActivity).getToolbar()

    /*fun notifyVerticalMenuChanged() {
        (activity as MainActivity).getVerticalNavigation().notifyUserChanged()
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        setupMenuViews()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = inflateViewBinding(inflater, container)
        return viewBinding.root
    }


    private fun setupMenuViews() {
        if (activity is MainActivity) {
            setupToolbar((activity as MainActivity).getToolbar().apply { setNavAction { } })
            setupBottomBar((activity as MainActivity).getBottomBar())
        }
    }

    fun getDatabase() = FirebaseDatabase.getInstance()

}