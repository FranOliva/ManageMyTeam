package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.GetPlayersUc
import es.us.managemyteam.usecase.GetUserUc
import es.us.managemyteam.util.CustomMediatorLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyTeamViewModel
    (
    getUserUc: GetUserUc,
    private val getPlayersUc: GetPlayersUc
) : BaseLoggedViewModel(getUserUc) {

    private val players: CustomMediatorLiveData<Resource<List<UserBo>>> = CustomMediatorLiveData()

    init {
        getPlayers()
    }

    fun getPlayers() {
        viewModelScope.launch(Dispatchers.Main) {
            players.setData(Resource.loading())
            withContext(Dispatchers.IO) {
                players.changeSource(Dispatchers.Main, getPlayersUc(true))
            }
        }
    }

    fun getPlayersData(): LiveData<Resource<List<UserBo>>> {
        return players.liveData()
    }

}