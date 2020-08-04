package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.*
import es.us.managemyteam.data.model.ClubBo
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.GetClubUc
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ClubViewModel(
private val getClubUc: GetClubUc
) : ViewModel() {

    private val club: MediatorLiveData<Resource<ClubBo>> = MediatorLiveData()
    private var clubSource: LiveData<Resource<ClubBo>> = MutableLiveData()

    init {
        getClub()
    }

    fun getClub() {
        viewModelScope.launch(Dispatchers.Main) {
            club.value = Resource.loading()
            club.removeSource(clubSource)
            withContext(Dispatchers.IO) {
                clubSource = getClubUc()
            }
            club.addSource(clubSource) {
                club.value = it
            }
        }
    }

    fun getClubData(): LiveData<Resource<ClubBo>> {
        return club
    }


}