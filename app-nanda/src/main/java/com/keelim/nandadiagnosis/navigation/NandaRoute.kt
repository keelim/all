package com.keelim.nandadiagnosis.navigation

class NandaRoute
sealed class NandaRoutes(val route: String) {
    // auth
    object Auth : NandaRoutes(route = "auth")
    object Profile : NandaRoutes(route = "auth/profile")
    object Login : NandaRoutes(route = "auth/login")

    // main
    object Main : NandaRoutes(route = "main")
}
