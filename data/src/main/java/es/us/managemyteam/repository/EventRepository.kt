package es.us.managemyteam.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import es.us.managemyteam.data.database.DatabaseTables
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.RepositoryUtil
import es.us.managemyteam.repository.util.Resource

interface EventRepository {

    suspend fun getEventLocation(): LatLng?

    suspend fun setEventLocation(latLng: LatLng?)

    suspend fun getEvents(): LiveData<Resource<List<EventBo>>>

}

class EventRepositoryImpl : EventRepository {

    private var eventLocation: LatLng? = null
    private val eventsRef = RepositoryUtil.getDatabaseTable(DatabaseTables.EVENT_TABLE)
    private val events = MutableLiveData<Resource<List<EventBo>>>()

    init {
        initializeEventsListener()
    }

    override suspend fun getEventLocation(): LatLng? {
        return eventLocation
    }

    override suspend fun setEventLocation(latLng: LatLng?) {
        this.eventLocation = latLng
    }

    override suspend fun getEvents(): LiveData<Resource<List<EventBo>>> {
        return events
    }

    private fun initializeEventsListener() {
        eventsRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                events.value = Resource.error(Error(serverErrorMessage = databaseError.message))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                events.value =
                    Resource.success(dataSnapshot.children.mapNotNull { it.getValue(EventBo::class.java) })
            }

        })
    }
}