package es.us.managemyteam.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import es.us.managemyteam.data.database.DatabaseTables
import es.us.managemyteam.data.model.EventBo
import es.us.managemyteam.data.model.LocationBo
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.RepositoryUtil
import es.us.managemyteam.repository.util.Resource
import java.util.*

interface EventRepository {

    suspend fun getEvents(): LiveData<Resource<List<EventBo>>>

    suspend fun getEventLocation(): LocationBo?

    suspend fun setEventLocation(location: LocationBo?)

    suspend fun getCurrentNewEvent(): EventBo?

    suspend fun setCurrentNewEvent(event: EventBo?)

    suspend fun createEvent(
        title: String,
        date: Date?,
        type: String,
        description: String?,
        location: LocationBo?,
        assistants: List<UserBo>?
    ): LiveData<Resource<Boolean>>

    suspend fun getEventDetail(uuid: String): LiveData<Resource<EventBo>>

}

class EventRepositoryImpl : EventRepository {

    private var eventLocation: LocationBo? = null
    private val eventsRef = RepositoryUtil.getDatabaseTable(DatabaseTables.EVENT_TABLE)
    private val events = MutableLiveData<Resource<List<EventBo>>>()
    private var currentEvent: EventBo? = EventBo()
    private val eventCreateData = MutableLiveData<Resource<Boolean>>()
    private val eventDetail = MutableLiveData<Resource<EventBo>>()

    init {
        initializeEventsListener()
    }

    override suspend fun getEventLocation(): LocationBo? {
        return eventLocation
    }

    override suspend fun setEventLocation(location: LocationBo?) {
        this.eventLocation = location
    }

    override suspend fun getCurrentNewEvent(): EventBo? {
        return currentEvent
    }

    override suspend fun setCurrentNewEvent(event: EventBo?) {
        currentEvent = event
    }

    override suspend fun createEvent(
        title: String,
        date: Date?,
        type: String,
        description: String?,
        location: LocationBo?,
        assistants: List<UserBo>?
    ): LiveData<Resource<Boolean>> {
        val eventToCreate = EventBo(
            title, date, location, description, assistants, type
        )
        eventsRef.push().setValue(eventToCreate) { databaseError, ref ->
            if (databaseError != null) {
                eventCreateData.value =
                    Resource.error(Error(serverErrorMessage = databaseError.message))
            } else {
                ref.updateChildren(
                    mapOf(
                        Pair("uuid", ref.key)
                    )
                )
                eventCreateData.value = Resource.success(true)
            }
        }
        return eventCreateData
    }

    override suspend fun getEventDetail(uuid: String): LiveData<Resource<EventBo>> {
        eventDetail.postValue(null)
        eventsRef.child(uuid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                eventDetail.value = Resource.error(Error(serverErrorMessage = error.message))
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                eventDetail.value = Resource.success(snapshot.getValue(EventBo::class.java))
            }

        })
        return eventDetail
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