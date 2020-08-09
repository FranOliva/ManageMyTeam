package es.us.managemyteam.usecase

import es.us.managemyteam.data.model.UserBo
import es.us.managemyteam.repository.AdminRepository

class AcceptPlayerUc(private val adminRepository: AdminRepository) {

    suspend operator fun invoke(user: UserBo) = adminRepository.acceptPlayer(user)
}