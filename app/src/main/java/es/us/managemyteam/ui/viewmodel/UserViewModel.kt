package es.us.managemyteam.ui.viewmodel

import es.us.managemyteam.usecase.GetUserUc

class UserViewModel(
    getUserUc: GetUserUc
) : BaseLoggedViewModel(getUserUc)


