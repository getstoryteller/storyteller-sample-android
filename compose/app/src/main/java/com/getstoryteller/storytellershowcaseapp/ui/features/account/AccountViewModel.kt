package com.getstoryteller.storytellershowcaseapp.ui.features.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.getstoryteller.storytellershowcaseapp.domain.LogoutUseCase
import com.getstoryteller.storytellershowcaseapp.domain.ports.AmplitudeService
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.domain.ports.StorytellerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
  private val logoutUseCase: LogoutUseCase,
  private val sessionRepository: SessionRepository,
  private val storytellerService: StorytellerService,
  private val amplitudeService: AmplitudeService,
) : ViewModel() {
  val isLoggedOut = MutableStateFlow(false)

  fun logout() {
    viewModelScope.launch {
      logoutUseCase.logout()
      isLoggedOut.value = true
    }
  }

  fun reset() {
    sessionRepository.reset()
    storytellerService.initStoryteller()
    amplitudeService.init()
  }
}
