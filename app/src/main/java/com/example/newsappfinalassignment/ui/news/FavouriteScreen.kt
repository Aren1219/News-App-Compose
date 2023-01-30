package com.example.newsappfinalassignment.ui.news

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.newsappfinalassignment.model.Data
import com.example.newsappfinalassignment.ui.MainViewModel
import com.example.newsappfinalassignment.ui.view.SignOutAlert
import com.example.newsappfinalassignment.util.Screen

@Composable
fun FavouriteScreen(
    viewModel: MainViewModel,
    navHostController: NavHostController,
    listState: LazyListState,
    signOut: () -> Unit
) {
    var deleteAlert by remember { mutableStateOf(false) }
    var deleteData: Data? by remember { mutableStateOf(null) }

    var signOutAlert by remember { mutableStateOf(false) }

    SignOutAlert(shouldShow = signOutAlert, onDismiss = { signOutAlert = false }) {
        signOut()
    }

    if (deleteAlert) {
        AlertDialog(
            onDismissRequest = { deleteAlert = false },
            title = { Text(text = "Are you sure?") },
            text = { Text(text = "You may not find the article anymore") },
            confirmButton = {
                Button(onClick = {
                    if (deleteData != null) {
                        viewModel.deleteNews(deleteData!!)
                        deleteData = null
                        deleteAlert = false
                    }
                }) {
                    Text(text = "Delete")
                }
            },
            dismissButton = {
                Button(onClick = { deleteAlert = false }) {
                    Text(text = "Cancel")
                }
            }
        )
    }

    Scaffold(topBar = {
        TopAppBar(
            backgroundColor = MaterialTheme.colors.primary,
            title = { Text(text = "Favourite") },
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
    ) { innerPadding ->
        val list = viewModel.savedList.observeAsState()
        if (list.value.isNullOrEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No saved news",
                    style = MaterialTheme.typography.h5
                )
            }
        } else {
            NewsListUi(
                list = list.value!!,
                onSelect = { uuid ->
                    navHostController.navigate(
                        Screen.NewsDetails.path + Screen.SavedNews.title + "/" + uuid
                    )
                },
                onSave = { data ->
                    deleteData = data
                    deleteAlert = true
                },
                listState = listState,
                icon = Icons.Default.Delete,
            )
        }
    }
}