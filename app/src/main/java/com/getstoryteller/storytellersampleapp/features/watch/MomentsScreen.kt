package com.getstoryteller.storytellersampleapp.features.watch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.getstoryteller.storytellersampleapp.domain.Config
import com.getstoryteller.storytellersampleapp.features.main.MainViewModel
import com.getstoryteller.storytellersampleapp.ui.StorytellerEmbeddedClips
import com.storyteller.ui.pager.StorytellerClipsFragment

@Composable
fun MomentsScreen(
  modifier: Modifier,
  config: Config?,
  sharedViewModel: MainViewModel,
  onCommit: (fragment: Fragment, tag: String) -> FragmentTransaction.(containerId: Int) -> Unit,
  getClipsFragment: () -> StorytellerClipsFragment?,
  tag: String
) {

  val reloadDataTrigger by sharedViewModel.reloadMomentsDataTrigger.observeAsState()
  LaunchedEffect(reloadDataTrigger) {
    reloadDataTrigger?.let {
      getClipsFragment()?.reloadData()
    }
  }

  Box(
    modifier = modifier
      .fillMaxSize(),
  ) {
    StorytellerEmbeddedClips(
      modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(),
      onCommit = onCommit(
        StorytellerClipsFragment.create(config?.topLevelCollectionId ?: ""),
        tag,
      ),
    )
  }
}
