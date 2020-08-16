package es.us.managemyteam.ui.viewmodel

import es.us.managemyteam.usecase.GetUserUc

class UserProfileViewModel(
    getUserUc: GetUserUc
) : BaseLoggedViewModel(getUserUc)


