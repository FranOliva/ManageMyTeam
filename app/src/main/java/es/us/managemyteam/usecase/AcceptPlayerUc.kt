package es.us.managemyteam.usecase

import es.us.managemyteam.repository.AdminRepository

class AcceptPlayerUc(private val adminRepository: AdminRepository) {

    suspend operator fun invoke(uuid: String) = adminRepository.acceptPlayer(uuid)
}