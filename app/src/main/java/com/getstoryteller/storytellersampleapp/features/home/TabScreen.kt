package com.getstoryteller.storytellersampleapp.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.getstoryteller.storytellersampleapp.ui.PullToRefresh
import com.getstoryteller.storytellersampleapp.ui.StorytellerItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TabScreen(
    tabId: String,
    viewModel: TabViewModel,
    rootNavController: NavController,
    isRefreshing: Boolean
) {
    LaunchedEffect(key1 = tabId, block = {
        viewModel.loadTab(tabId)
    })

    val pageUiState by viewModel.uiState.collectAsState()
    val refreshState = rememberPullRefreshState(
        refreshing = pageUiState.isRefreshing,
        onRefresh = { viewModel.onRefresh() }
    )
    val listState = rememberLazyListState()
    val listItems = pageUiState.tabItems
    var columnHeightPx by remember {
        mutableIntStateOf(0)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.surface)
            .pullRefresh(
                state = refreshState
            )
            .onGloballyPositioned {
                columnHeightPx = it.size.height
            }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(top = 12.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            state = listState
        ) {
            itemsIndexed(items = listItems) { _, uiModel ->
                StorytellerItem(
                    uiModel = uiModel,
                    isRefreshing = pageUiState.isRefreshing || isRefreshing,
                    navController = rootNavController
                )
            }
        }

        PullToRefresh(
            modifier = Modifier
                .align(Alignment.TopCenter),
            state = refreshState,
            refreshing = pageUiState.isRefreshing || isRefreshing
        )
    }
}