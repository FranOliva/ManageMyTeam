package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import es.us.managemyteam.ui.activity.MainActivity

abstract class BaseFragment<Any : ViewBinding> : Fragment() {

    companion object {
        const val ANIMATION_DELAYER_TIME = 200L
    }

    lateinit var viewBinding: Any

    abstract fun inflateViewBinding(inflater: LayoutInflater, container: ViewGroup?): Any

    abstract fun setupToolbar(toolbar: Toolbar)

    //abstract fun setupBottomBar(bottomNavigationView: CommonBottomNavigationView)

    /*fun getToolbar() = (activity as MainActivity).getToolbar()

    fun notifyVerticalMenuChanged() {
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

    fun getToolbar() = (activity as MainActivity).getToolbar()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = inflateViewBinding(inflater, container)
        return viewBinding.root
    }

    fun setToolbarNavAction(listener: (View) -> Unit) {
        getToolbar().setNavigationOnClickListener(listener)
    }

    private fun setupMenuViews() {
        if (activity is MainActivity) {
            setupToolbar((activity as MainActivity).getToolbar().apply { setToolbarNavAction {} })
            //setupBottomBar((activity as MainActivity).getBottomBar())
        }
    }

}