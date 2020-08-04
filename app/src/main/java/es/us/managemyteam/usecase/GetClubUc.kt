package es.us.managemyteam.usecase

import es.us.managemyteam.repository.ClubRepository

class GetClubUc(private val clubRepository: ClubRepository) {

    suspend operator fun invoke() = clubRepository.getClub()

}