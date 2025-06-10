package com.example.simpleweather.ui.welcome

sealed class WelcomeState {
    object Initial : WelcomeState()
    object ShowingSun : WelcomeState()
    object ShowingText : WelcomeState()
    object Complete : WelcomeState()
} 