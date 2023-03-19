package com.keelim.nandadiagnosis.navigation

class NandaRoute {
}
sealed class NandaRoutes(val route: String){
    object Login: NandaRoutes(route = "Login")
    object Main: NandaRoutes(route = "Main")
}
