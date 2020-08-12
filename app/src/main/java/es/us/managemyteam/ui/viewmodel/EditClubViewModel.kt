package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import es.us.managemyteam.R
import es.us.managemyteam.data.model.ClubBo
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.GetClubUc
import es.us.managemyteam.usecase.GetUserUc
import es.us.managemyteam.repository.util.Error
import es.us.managemyteam.usecase.EditClubUc
import es.us.managemyteam.util.CustomMediatorLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditClubViewModel(
    private val getClubUc: GetClubUc,
    getUserUc: GetUserUc,
    private val editClubUc: EditClubUc
) : BaseLoggedViewModel(getUserUc) {

    private val club: CustomMediatorLiveData<Resource<ClubBo>> = CustomMediatorLiveData()
    private val editClub = CustomMediatorLiveData<Resource<ClubBo>>()

    fun getClub() {
        viewModelScope.launch(Dispatchers.Main) {
            club.setData(Resource.loading(data = null))
            withContext(Dispatchers.IO) {
                club.changeSource(
                    Dispatchers.Main,
                    getClubUc()
                )
            }
        }
    }

    fun getClubData(): LiveData<Resource<ClubBo>> {
        return club.liveData()
    }

    fun editClub(
        name: String,
        dateFundation: String,
        location: String,
        president: String,
        coach: String,
        phoneNumber: Long,
        mail: String,
        web: String
    ) =
        viewModelScope.launch(Dispatchers.Main) {
            if (validateForm(name, dateFundation, location, president, coach, phoneNumber, mail)) {
                withContext(Dispatchers.IO) {
                    editClub.changeSource(
                        Dispatchers.Main,
                        editClubUc(name, dateFundation, location, president, coach, phoneNumber, mail, web)
                    )
                }
            }
        }

    private fun validateForm(
        name: String,
        dateFundation: String,
        location: String,
        president: String,
        coach: String,
        phoneNumber: Long,
        mail: String
    ): Boolean {
        return when {
            name.isBlank() -> {
                editClub.setData(
                    Resource.error(
                        Error(R.string.edit_club_error_name)
                    )
                )
                false
            }
            dateFundation.isBlank() -> {
                editClub.setData(
                    Resource.error(
                        Error(R.string.edit_club_error_date_fundation)
                    )
                )
                false
            }
            location.isBlank() -> {
                editClub.setData(
                    Resource.error(
                        Error(R.string.edit_club_error_location)
                    )
                )
                false
            }
            president.isBlank() -> {
                editClub.setData(
                    Resource.error(
                        Error(R.string.edit_club_error_president)
                    )
                )
                false
            }
            coach.isBlank() -> {
                editClub.setData(
                    Resource.error(
                        Error(R.string.edit_club_error_coach)
                    )
                )
                false
            }
            phoneNumber.toDouble().isNaN() -> {
                editClub.setData(
                    Resource.error(
                        Error(R.string.edit_club_error_phone_number)
                    )
                )
                false
            }
            mail.matches(Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")) -> {
                editClub.setData(
                    Resource.error(
                        Error(R.string.edit_club_error_mail)
                    )
                )
                false
            }
            else -> true
        }
    }

}