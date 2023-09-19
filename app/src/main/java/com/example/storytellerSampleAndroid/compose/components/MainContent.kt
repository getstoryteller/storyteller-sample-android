package com.example.storytellerSampleAndroid.compose.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.storytellerSampleAndroid.compose.JetpackComposeViewModel
import com.example.storytellerSampleAndroid.compose.components.items.ChangeUserContainer
import com.example.storytellerSampleAndroid.compose.components.items.Header
import com.example.storytellerSampleAndroid.compose.components.items.NavigatedToApp
import com.example.storytellerSampleAndroid.compose.components.items.ToggleDarkModeContainer
import com.storyteller.domain.entities.Error
import com.storyteller.domain.entities.StorytellerListViewCellType
import com.storyteller.domain.entities.StorytellerListViewStyle
import com.storyteller.ui.compose.StorytellerClipsGridView
import com.storyteller.ui.compose.StorytellerClipsRowView
import com.storyteller.ui.compose.StorytellerComposeController
import com.storyteller.ui.compose.StorytellerStoriesGridView
import com.storyteller.ui.compose.StorytellerStoriesRowView
import com.storyteller.ui.list.StorytellerClipsView
import com.storyteller.ui.list.StorytellerListViewDelegate
import com.storyteller.ui.list.StorytellerStoriesView

@Composable
fun MainScreen(
  modifier: Modifier = Modifier,
  paddingValues: PaddingValues,
  onRefresh: () -> Unit,
  viewModel: JetpackComposeViewModel,
  controller: StorytellerComposeController,
  storytellerListViewDelegate: StorytellerListViewDelegate,
  onUserNavigatedToApp: (String) -> Unit,
  openSettings: () -> Unit
) {
  val coroutineScope = rememberCoroutineScope()
  val listState = rememberLazyListState()

  val refreshing by remember { viewModel.refreshing }
  val ptrState = rememberPullRefreshState(refreshing, onRefresh)
  val rotation = animateFloatAsState(ptrState.progress * 120)
  val darkMode by remember { viewModel.isDarkMode }
  Box(
    modifier = modifier
      .fillMaxSize()
      .padding(paddingValues)
      .pullRefresh(state = ptrState)
  ) {
    LaunchedEffect(key1 = darkMode) {
      val uiStyle = if (darkMode) {
        StorytellerListViewStyle.DARK
      } else {
        StorytellerListViewStyle.LIGHT
      }
      controller.forEach { item ->
        item.uiStyle = uiStyle
      }
    }

    var shouldShowRow by remember { mutableStateOf(true) }

    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      horizontalAlignment = Alignment.CenterHorizontally,
      state = listState
    ) {
      item {
        Header("Row View")
      }
      item {
        StorytellerStoriesRowView(
          modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
          tag = "row",
          controller = controller
        ) {
          delegate = storytellerListViewDelegate
          configuration = StorytellerStoriesView.ListConfiguration(
            cellType = StorytellerListViewCellType.SQUARE,
            categories = listOf()
          )
          reloadData()
        }
      }
      item {
        Header("Grid View")
      }
      item {
        StorytellerStoriesGridView(
          modifier = Modifier.fillMaxWidth(),
          tag = "grid",
          controller = controller
        ) {
          delegate = storytellerListViewDelegate
            configuration = StorytellerStoriesView.ListConfiguration(
                cellType = StorytellerListViewCellType.SQUARE,
                categories = listOf()
            )
          reloadData()
        }
      }
      item {
        Header("Clips Row View")
      }
      item {
        StorytellerClipsRowView(
          modifier = Modifier.fillMaxWidth(),
          tag = "clips_row",
          controller = controller
        ) {
          delegate = storytellerListViewDelegate
          configuration = StorytellerClipsView.ListConfiguration(
            cellType = StorytellerListViewCellType.SQUARE,
            collection = "clipssample"
          )
          reloadData()
        }
      }
      item {
        Header("Clips Grid View")
      }
      item {
        StorytellerClipsGridView(
          modifier = Modifier.fillMaxWidth(),
          tag = "clips_grid",
          controller = controller
        ) {
          delegate = storytellerListViewDelegate
          configuration = StorytellerClipsView.ListConfiguration(
            cellType = StorytellerListViewCellType.SQUARE,
            collection = "clipssample"
          )
          reloadData()
        }
      }

      item {
        AnimatedVisibility(visible = shouldShowRow) {
          StorytellerClipsGridView(
            modifier = Modifier.fillMaxWidth(),
            tag = "non-existent",
            controller = controller
          ) {
            delegate = hasDataStorytellerDelegate { hasData ->
              shouldShowRow = hasData
            }
            cellType = StorytellerListViewCellType.SQUARE
            collection = "non-existent"
            reloadData()
          }
        }
      }

      item {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
          horizontalArrangement = Arrangement.Center
        ) {
          Button(onClick = openSettings) {
            Text(text = "Open Settings")
          }
        }
      }
      item {
        Row(
          modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          val userNavigatedToApp by viewModel.userNavigatedToApp
            .collectAsStateWithLifecycle(initialValue = "")
          NavigatedToApp(text = userNavigatedToApp, onUserNavigatedToApp = onUserNavigatedToApp)
        }
      }

      item {
        Row(
          modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
          horizontalArrangement = Arrangement.SpaceAround
        ) {
          ChangeUserContainer(onRefresh, coroutineScope, listState, viewModel)
          ToggleDarkModeContainer {
            viewModel.toggleDarkMode()
          }
        }
      }
    }

    PullToRefresh(
      modifier = Modifier
        .align(Alignment.TopCenter),
      state = ptrState,
      rotation = rotation.value,
      refreshing = refreshing
    )
  }
}

interface EmptyStorytellerDelegate : StorytellerListViewDelegate {
  override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) = Unit

  override fun onDataLoadStarted() = Unit

  override fun onPlayerDismissed() = Unit

}

fun hasDataStorytellerDelegate(callback: (Boolean) -> Unit) = object : EmptyStorytellerDelegate {
  override fun onDataLoadComplete(success: Boolean, error: Error?, dataCount: Int) {
    callback(dataCount > 0)
  }
}