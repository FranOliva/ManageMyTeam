package es.us.managemyteam.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import es.us.managemyteam.R
import es.us.managemyteam.contract.BaseAdapterClickListener
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.database.DatabaseTables
import es.us.managemyteam.databinding.FragmentEventsBinding
import es.us.managemyteam.extension.*
import es.us.managemyteam.ui.adapter.EventsAdapter


class EventsFragment : BaseFragment<FragmentEventsBinding>(), BaseAdapterClickListener<EventBo> {

    private val eventsRef = getDatabase().getReference(DatabaseTables.EVENT_TABLE)
    private var events = arrayListOf<EventBo>()
    private var eventsAdapter: EventsAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupList()
        initializeEventsListener()
        setupClickListeners()

    }

    private fun setupClickListeners() {
        viewBinding.eventsFabCreateEvent.setOnClickListener {
            findNavController().navigate(R.id.action_events_to_create_event)
        }
    }

    private fun initializeEventsListener() {
        eventsRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                showErrorDialog(databaseError.message, getDefaultDialogErrorListener())
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                events.clear()
                dataSnapshot.children.forEach {
                    val eventData = it.getValue(EventBo::class.java)
                    eventData?.let { event ->
                        events.add(event)
                    }
                }
                eventsAdapter?.setData(events)
                eventsAdapter?.notifyDataSetChanged()
            }

        })
    }

    private fun setupList() {
        viewBinding.eventsList.apply {
            if (eventsAdapter == null) {
                eventsAdapter = EventsAdapter(this@EventsFragment)
            }
            eventsAdapter?.let {
                adapter = it
            }
        }
    }


    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentEventsBinding {
        return FragmentEventsBinding.inflate(inflater, container, false)
    }

    override fun setupToolbar(toolbar: Toolbar) {
        toolbar.apply {
            setToolbarTitle(getString(R.string.events))
            setNavIcon(null)
            show()
        }
    }

    override fun setupBottomBar(bottomNavigationView: BottomNavigationView) {
        bottomNavigationView.show()
    }

    override fun onAdapterItemClicked(item: EventBo, position: Int) {
        // TODO: go to detail
    }

}