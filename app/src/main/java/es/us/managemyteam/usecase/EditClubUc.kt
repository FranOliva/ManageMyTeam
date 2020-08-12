package es.us.managemyteam.usecase

import androidx.lifecycle.LiveData
import es.us.managemyteam.repository.ClubRepository
import es.us.managemyteam.repository.util.Resource

class EditClubUc(private val clubRepository: ClubRepository) {

    suspend operator fun invoke(
        name: String,
        dateFundation: String,
        location: String,
        president: String,
        coach: String,
        phoneNumber: Long,
        mail: String,
        web: String
    ): LiveData<Resource<Boolean>> {

        return clubRepository.editClub(
            name,
            dateFundation,
            location,
            president,
            location,
            coach,
            phoneNumber,
            mail,
            web
        )
    }
}