package com.example.newsappfinalassignment.ui.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.newsappfinalassignment.R
import com.example.newsappfinalassignment.model.Data
import com.example.newsappfinalassignment.ui.MainViewModel
import com.example.newsappfinalassignment.ui.theme.NewsAppfinalAssignmentTheme
import com.example.newsappfinalassignment.util.Screen
import com.example.newsappfinalassignment.util.Util
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun DetailsScreen(
    parent: String,
    uuid: String,
    viewModel: MainViewModel,
    navHostController: NavHostController,
) {
    val newsData by remember { mutableStateOf(viewModel.getNewsUUID(uuid)) }
    if (newsData != null)
        Column {
            Top(title = newsData!!.title) {
                when (parent) {
                    Screen.NewsList.title -> {
                        navHostController.navigate(Screen.NewsList.route)
                    }
                    Screen.SavedNews.title -> {
                        navHostController.navigate(Screen.SavedNews.route)
                    }
                }
            }
            MoreDerails(data = newsData!!)
        }
}

@Composable
private fun Top(title: String, back: () -> Unit) {
    TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
        IconButton(onClick = { back() }) {
            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "back")
        }
        Text(text = title, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}

@Composable
private fun MoreDerails(data: Data) {
    val padding = 12.dp
    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            GlideImage(
                imageModel = { data.imageUrl },
                modifier = Modifier.height(250.dp),
                failure = { ImageBitmap.imageResource(id = R.drawable.placeholder_image) },
                loading = { ImageBitmap.imageResource(id = R.drawable.placeholder_image) },
                imageOptions = ImageOptions(contentScale = ContentScale.Crop)
//                contentScale = ContentScale.Fit,
//                error = ImageBitmap.imageResource(id = R.drawable.placeholder_image),
//                placeHolder = ImageBitmap.imageResource(id = R.drawable.placeholder_image),

            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = data.title, style = MaterialTheme.typography.h5)
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = Util.formatDate(data.publishedAt))
            Text(text = data.source)
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = data.description)
            Spacer(modifier = Modifier.padding(8.dp))
            Text(text = data.snippet)
            Spacer(modifier = Modifier.padding(8.dp))
            val annotatedString = buildAnnotatedString {
                pushStringAnnotation(
                    tag = "url",
                    annotation = data.url
                )
                withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
                    append("Read more")
                }
                pop()
            }
            val uriHandler = LocalUriHandler.current
            ClickableText(
                text = annotatedString,
                onClick = { offset ->
                    annotatedString.getStringAnnotations(tag = "url", start = offset, end = offset)
                        .firstOrNull()?.let {
                            uriHandler.openUri(it.item)
                        }
                }
            )
        }
    }
}


@Composable
@Preview(showBackground = true)
private fun PreviewTop() {
    NewsAppfinalAssignmentTheme {
        Top("Some Random News") {}
    }
}

@Composable
@Preview(showBackground = true)
private fun PreviewDetails() {
    NewsAppfinalAssignmentTheme {
        Column {
            MoreDerails(data = Util.previewNewsData())
        }
    }
}