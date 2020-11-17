package es.us.managemyteam.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.repository.util.Resource
import es.us.managemyteam.usecase.GetUsersUc
import es.us.managemyteam.util.CustomMediatorLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UnableUsersViewModel(
    private val getUsersUc: GetUsersUc
) : ViewModel() {

    private val users: CustomMediatorLiveData<Resource<List<UserBo>>> = CustomMediatorLiveData()

    fun getUsers() {
        viewModelScope.launch(Dispatchers.Main) {
            users.setData(Resource.loading())
            withContext(Dispatchers.IO) {
                users.changeSource(Dispatchers.Main, getUsersUc())
            }
        }
    }

    fun getUsersData(): LiveData<Resource<List<UserBo>>> {
        return users.liveData()
    }


}