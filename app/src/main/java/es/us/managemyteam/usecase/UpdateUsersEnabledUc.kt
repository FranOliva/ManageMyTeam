package es.us.managemyteam.usecase

import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.repository.AdminRepository

class UpdateUsersEnabledUc(private val adminRepository: AdminRepository) {

    suspend operator fun invoke(users: List<UserBo>) =
        adminRepository.updateUsersEnable(users)

}