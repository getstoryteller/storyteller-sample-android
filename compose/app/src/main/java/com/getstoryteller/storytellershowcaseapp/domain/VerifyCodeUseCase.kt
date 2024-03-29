package com.getstoryteller.storytellershowcaseapp.domain

import com.getstoryteller.storytellershowcaseapp.domain.ports.AmplitudeService
import com.getstoryteller.storytellershowcaseapp.domain.ports.AuthRepository
import com.getstoryteller.storytellershowcaseapp.domain.ports.SessionRepository
import com.getstoryteller.storytellershowcaseapp.domain.ports.StorytellerService
import com.getstoryteller.storytellershowcaseapp.remote.entities.TenantSettingsDto
import java.util.UUID

interface VerifyCodeUseCase {
  suspend fun verifyCode(
    code: String,
  ): TenantSettingsDto
}

class VerifyCodeUseCaseImpl(
  private val authRepository: AuthRepository,
  private val sessionRepository: SessionRepository,
  private val storytellerService: StorytellerService,
  private val amplitudeService: AmplitudeService,
) : VerifyCodeUseCase {
  override suspend fun verifyCode(
    code: String,
  ): TenantSettingsDto {
    val settings = authRepository.verifyCode(code)
    sessionRepository.apiKey = settings.androidApiKey
    sessionRepository.userId = UUID.randomUUID().toString()
    storytellerService.initStoryteller()
    amplitudeService.init()
    storytellerService.updateCustomAttributes()
    return TenantSettingsDto(
      topLevelClipsCollection = settings.topLevelClipsCollection,
      tabsEnabled = settings.tabsEnabled,
    )
  }
}
