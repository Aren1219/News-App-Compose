package com.example.newsappfinalassignment.ui.news

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.newsappfinalassignment.R
import com.example.newsappfinalassignment.model.Data
import com.example.newsappfinalassignment.ui.MainViewModel
import com.example.newsappfinalassignment.ui.view.SignOutAlert
import com.example.newsappfinalassignment.util.Resource
import com.example.newsappfinalassignment.util.Screen
import com.example.newsappfinalassignment.util.Util.formatDate
import com.example.newsappfinalassignment.util.Util.previewNewsDataList
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun NewsListScreen(
    viewModel: MainViewModel,
    navHostController: NavHostController,
    listState: LazyListState,
    signOut: () -> Unit
) {
    val context = LocalContext.current
    val newsList = viewModel.newsList.observeAsState()

    var signOutAlert by remember {
        mutableStateOf(false)
    }

    SignOutAlert(shouldShow = signOutAlert, onDismiss = { signOutAlert = false }) {
        signOut()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                backgroundColor = MaterialTheme.colors.primary,
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    Button(
                        onClick = { signOutAlert = true },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Text(text = "Sign out")
                    }
                }
            )
        }
    ) { paddingValues ->
        if (newsList.value is Resource.Error<*>) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(text = newsList.value!!.message!!, style = MaterialTheme.typography.h5)
            }
        } else {
            newsList.value?.data?.let {
                NewsListUi(
                    list = it,
                    loadMore = { viewModel.getNewsList() },
                    onSelect = { uuid ->
                        navHostController.navigate(
                            Screen.NewsDetails.path + Screen.NewsList.title + "/" + uuid
                        )
                    },
                    listState = listState,
                    onSave = { data ->
                        viewModel.saveNews(data)
                        Toast.makeText(context, "News saved!", Toast.LENGTH_SHORT).show()
                    },
                    icon = Icons.Default.Add
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (newsList.value is Resource.Loading)
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(20.dp)
                    )
            }
        }
    }
}

@Composable
fun NewsListUi(
    list: List<Data>,
    loadMore: () -> Unit = {},
    onSelect: (String) -> Unit,
    onSave: (Data) -> Unit,
    listState: LazyListState,
    icon: ImageVector
) {
    val p = 12.dp
//    val listState = rememberLazyListState()
    LazyColumn(
        state = listState,
        contentPadding = PaddingValues(p),
        verticalArrangement = Arrangement.spacedBy(p)
    ) {
        items(list) { item ->
            NewsItemUi(
                data = item,
                onClick = { onSelect(item.uuid) },
                favourite = { onSave(item) },
                icon = icon
            )
        }
    }
    listState.OnBottomReached {
        loadMore()
    }
}

@Composable
fun LazyListState.OnBottomReached(
    loadMore: () -> Unit
) {
    //derive state for checking if need to loading more items
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf true
            lastVisibleItem.index == layoutInfo.totalItemsCount - 1
        }
    }
    // Convert the state into a cold flow and collect
    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }.collect {
            if (it) loadMore()
        }
    }
}

@Composable
fun NewsItemUi(
    data: Data,
    onClick: () -> Unit,
    favourite: () -> Unit,
    icon: ImageVector
) {
    val cardHeight = 150.dp
    val cardPadding = 12.dp
//    var isSaved by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(cardHeight)
            .clickable { onClick() },
        elevation = 8.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(cardPadding)
                .fillMaxSize()
        ) {
            GlideImage(
                imageModel = { data.imageUrl },
                modifier = Modifier
                    .size(cardHeight - cardPadding * 2)
                    .align(Alignment.CenterVertically)
                    .padding(end = cardPadding),
                failure = { ImageBitmap.imageResource(id = R.drawable.placeholder_image) },
            )
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.h6,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                )
                Column(
                    modifier = Modifier.align(Alignment.BottomStart),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(text = formatDate(data.publishedAt))
                    Text(text = data.source)
                }
                IconButton(
                    onClick = {
                        favourite()
//                        isSaved = true
                    },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(40.dp),
                ) {
                    Icon(
//                        imageVector = if (!isSaved) Icons.Default.FavoriteBorder else Icons.Default.Favorite,
                        imageVector = icon,
                        contentDescription = "favourite",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    NewsListUi(previewNewsDataList(), {}, {}, {}, rememberLazyListState(), icon = Icons.Default.Add)
}