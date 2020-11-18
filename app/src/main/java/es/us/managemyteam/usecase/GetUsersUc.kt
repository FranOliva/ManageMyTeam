package es.us.managemyteam.usecase

import es.us.managemyteam.repository.AdminRepository

class GetUsersUc(private val adminRepository: AdminRepository) {

    suspend operator fun invoke() = adminRepository.getUsers()
}