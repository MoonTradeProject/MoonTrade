package com.example.moontrade.navigation


object NavRoutes {
    const val ONBOARDING = "onboarding"
    const val WELCOME = "welcome"
    const val ACCOUNT_CREATION = "account_creation"
    const val ADD_MAIL = "add_mail"
    const val CONFIRM_MAIL = "confirm_mail"
    const val ENTER_CODE = "enter_code"
    const val CREATE_PASSWORD = "create_password"
    const val SUCCESSFUL_REGISTRATION = "successful_registration"
    const val LOGIN = "login"
    const val HOME = "home"
    const val MARKETS = "markets"
    const val TRADE = "trade"
    const val RATINGS = "ratings"
    const val TOURNAMENTS = "tournaments"
    const val PLAYER_PROFILE = "player_profile/{playerId}"

    const val PROFILE = "profile"
    const val MARKET_DETAIL = "market_detail"
    fun marketDetailRoute(symbol: String) = "$MARKET_DETAIL/$symbol"
    const val SETTINGS = "settings"
    const val PROFILE_EDIT = "profile_edit"
    const val USER_ORDERS = "user_orders"
}
