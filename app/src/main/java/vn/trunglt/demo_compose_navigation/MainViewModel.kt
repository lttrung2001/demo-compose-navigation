package vn.trunglt.demo_compose_navigation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute

class MainViewModel(
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val profile: Profile = savedStateHandle.toRoute()
}