package es.us.managemyteam.usecase

import es.us.managemyteam.repository.AdminRepository

class GetPlayersUc(private val adminRepository: AdminRepository) {

    suspend operator fun invoke() = adminRepository.getPlayersNotEnabled()
}