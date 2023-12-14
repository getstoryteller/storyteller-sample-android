package com.getstoryteller.storytellersampleapp.services

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import com.getstoryteller.storytellersampleapp.ads.StorytellerAdsDelegate
import com.storyteller.Storyteller
import com.storyteller.domain.ads.entities.StorytellerAdRequestInfo
import com.storyteller.domain.entities.UserActivity
import com.storyteller.domain.entities.UserActivityData
import com.storyteller.domain.entities.UserInput
import com.storyteller.domain.entities.ads.StorytellerAd
import com.storyteller.domain.usecases.attributes.UserAttributes
import com.storyteller.ui.list.StorytellerDelegate
import timber.log.Timber
import javax.inject.Inject

interface StorytellerService {
    fun initStoryteller()
    fun updateCustomAttributes()
}

class StorytellerServiceImpl @Inject constructor(
    private val sessionService: SessionService,
    private val storytellerAdsDelegate: StorytellerAdsDelegate
) : StorytellerService {

    companion object {
        private const val LANGUAGE_ATTRIBUTE_KEY = "language"
        private const val FAV_TEAM_ATTRIBUTE_KEY = "favoriteTeam"
        private const val HAS_ACCOUNT_ATTRIBUTE_KEY = "hasAccount"
    }

    override fun initStoryteller() {
        Storyteller.apply {
            this.storytellerDelegate = storytellerAdsDelegate
            this.initialize(
                apiKey = sessionService.apiKey ?: "",
                userInput = sessionService.userId?.let { UserInput(it) },
                onSuccess = {
                    Timber.i("Storyteller initialized")
                },
                onFailure = {
                    Timber.i("Storyteller error $it")
                }
            )
        }
    }

    override fun updateCustomAttributes() {
        sessionService.language?.let {
            Storyteller.user.setCustomAttribute(LANGUAGE_ATTRIBUTE_KEY, it)
        } ?: Storyteller.user.removeCustomAttribute(LANGUAGE_ATTRIBUTE_KEY)
        sessionService.team?.let {
            Storyteller.user.setCustomAttribute(FAV_TEAM_ATTRIBUTE_KEY, it)
        } ?: Storyteller.user.removeCustomAttribute(FAV_TEAM_ATTRIBUTE_KEY)
        sessionService.hasAccount.let {
            Storyteller.user.setCustomAttribute(HAS_ACCOUNT_ATTRIBUTE_KEY, if (it) "yes" else "no")
        }
        if (sessionService.trackEvents) {
            Storyteller.enableEventTracking()
        } else {
            Storyteller.disableEventTracking()
        }
    }
}