package es.us.managemyteam.usecase

import es.us.managemyteam.repository.UserRepository

class GetPlayersUc(private val userRepository: UserRepository) {

    suspend operator fun invoke() = userRepository.getPlayers()
}