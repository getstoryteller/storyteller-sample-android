package com.getstoryteller.storytellershowcaseapp.ui.features.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.getstoryteller.storytellershowcaseapp.domain.Config
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.domain.ports.StorytellerService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class OptionSelectViewModel @Inject constructor(
  private val sessionRepository: SessionRepository,
  private val storytellerService: StorytellerService,
) : ViewModel() {
  private val _uiState = MutableStateFlow(OptionSelectUIModel())
  val uiState: StateFlow<OptionSelectUIModel> = _uiState.asStateFlow()

  private val _reloadMainScreen = MutableLiveData<String?>()
  val reloadMainScreen: LiveData<String?> = _reloadMainScreen

  fun setupOptionType(
    config: Config,
    optionSelectType: OptionSelectType,
  ) {
    when (optionSelectType) {
      OptionSelectType.LANGUAGE -> {
        _uiState.value =
          OptionSelectUIModel(
            title = "Language",
            selectedOption = sessionRepository.language,
            type = OptionSelectType.LANGUAGE,
            options =
            config.languages.map {
              KeyValueUiModel(
                key = it.key,
                value = it.value,
              )
            }.toMutableList().apply {
              add(0, KeyValueUiModel(null, "Not Set"))
            },
          )
      }

      OptionSelectType.FAVORITE_TEAM -> {
        _uiState.value =
          OptionSelectUIModel(
            title = "Favorite Team",
            type = OptionSelectType.FAVORITE_TEAM,
            selectedOption = sessionRepository.team,
            options =
            config.teams.map {
              KeyValueUiModel(
                key = it.key,
                value = it.value,
              )
            }.toMutableList().apply {
              add(0, KeyValueUiModel(null, "Not Set"))
            },
          )
      }

      OptionSelectType.HAS_ACCOUNT -> {
        _uiState.value =
          OptionSelectUIModel(
            title = "Favorite Team",
            type = OptionSelectType.HAS_ACCOUNT,
            selectedOption = if (sessionRepository.hasAccount) "yes" else "no",
            options =
            listOf(
              KeyValueUiModel(
                key = "no",
                value = "No",
              ),
              KeyValueUiModel(
                key = "yes",
                value = "Yes",
              ),
            ),
          )
      }

      OptionSelectType.EVENT_TRACKING -> {
        _uiState.value =
          OptionSelectUIModel(
            title = "Allow Event Tracking",
            type = OptionSelectType.EVENT_TRACKING,
            selectedOption = if (sessionRepository.trackEvents) "yes" else "no",
            options =
            listOf(
              KeyValueUiModel(
                key = "no",
                value = "No",
              ),
              KeyValueUiModel(
                key = "yes",
                value = "Yes",
              ),
            ),
          )
      }
    }
  }

  fun selectOption(
    key: String?,
  ) {
    _uiState.update { it.copy(selectedOption = key) }
    when (_uiState.value.type) {
      OptionSelectType.HAS_ACCOUNT -> sessionRepository.hasAccount = key == "yes"
      OptionSelectType.LANGUAGE -> sessionRepository.language = key
      OptionSelectType.FAVORITE_TEAM -> sessionRepository.team = key
      OptionSelectType.EVENT_TRACKING -> sessionRepository.trackEvents = key == "yes"
    }
    storytellerService.updateCustomAttributes()
    _reloadMainScreen.value = key
  }

  fun onReloadingDone() {
    _reloadMainScreen.value = null
  }
}

data class OptionSelectUIModel(
  val title: String = "",
  val type: OptionSelectType = OptionSelectType.HAS_ACCOUNT,
  val selectedOption: String? = null,
  val options: List<KeyValueUiModel> = listOf(),
)

data class KeyValueUiModel(
  val key: String?,
  val value: String,
)
