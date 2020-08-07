package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.us.managemyteam.data.model.ClubBo
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.GetClubUc
import es.us.managemyteam.usecase.GetUserUc
import es.us.managemyteam.util.CustomMediatorLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClubViewModel(
    private val getClubUc: GetClubUc,
    getUserUc: GetUserUc
) : BaseLoggedViewModel(getUserUc) {

    private val club: CustomMediatorLiveData<Resource<ClubBo>> = CustomMediatorLiveData()

    init {
        getClub()
    }

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

}