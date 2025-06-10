package com.example.simpleweather.ui.welcome

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WelcomeViewModel : ViewModel() {
    private val _welcomeState = MutableStateFlow<WelcomeState>(WelcomeState.Initial)
    val welcomeState: StateFlow<WelcomeState> = _welcomeState.asStateFlow()

    fun startWelcomeSequence() {
        _welcomeState.value = WelcomeState.ShowingSun
    }

    fun completeWelcomeSequence() {
        _welcomeState.value = WelcomeState.Complete
    }
}
