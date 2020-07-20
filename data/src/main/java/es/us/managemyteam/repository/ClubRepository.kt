package es.us.managemyteam.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import es.us.managemyteam.data.database.DatabaseTables
import es.us.managemyteam.data.model.ClubBo
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.repository.util.RepositoryUtil
import es.us.managemyteam.repository.util.Resource

interface ClubRepository {

    suspend fun getClub(): LiveData<Resource<ClubBo>>

    suspend fun createClub(
        name: String,
        dateFundation: String,
        location: String,
        president: String,
        coach: String,
        phoneNumber: String,
        mail: String,
        web: String
    ): LiveData<Resource<Boolean>>

}

class ClubRepositoryImpl : ClubRepository {

    private val clubRef = RepositoryUtil.getDatabaseTable(DatabaseTables.CLUB_TABLE)
    private val club = MutableLiveData<Resource<ClubBo>>()
    private val clubCreateData = MutableLiveData<Resource<Boolean>>()

    init {
        initializeClubListener()
    }

    override suspend fun getClub(): LiveData<Resource<ClubBo>> {
        return club
    }

    override suspend fun createClub(
        name: String,
        dateFundation: String,
        location: String,
        president: String,
        coach: String,
        phoneNumber: String,
        mail: String,
        web: String
    ): LiveData<Resource<Boolean>> {
        clubRef.push().setValue(
            ClubBo(
                name, dateFundation, location, president, coach, phoneNumber, mail, web
            )
        ) { databaseError, _ ->
            if (databaseError != null) {
                clubCreateData.value =
                    Resource.error(Error(serverErrorMessage = databaseError.message))
            } else {
                clubCreateData.value = Resource.success(true)
            }
        }
        return clubCreateData
    }

    private fun initializeClubListener() {
        clubRef.addValueClubListener(object : ValueClubListener {
            override fun onCancelled(databaseError: DatabaseError) {
                club.value = Resource.error(Error(serverErrorMessage = databaseError.message))
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                club.value =
                    Resource.success(dataSnapshot.children.mapNotNull { it.getValue(ClubBo::class.java) })
            }

        })
    }

}