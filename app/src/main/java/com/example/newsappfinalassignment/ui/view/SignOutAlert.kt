package com.example.newsappfinalassignment.ui.view

import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun SignOutAlert(shouldShow: Boolean, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    if (shouldShow)
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Are you sure?") },
            confirmButton = {
                Button(onClick = {
                    onConfirm()
                }) {
                    Text(text = "Sign out")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text(text = "Cancel")
                }
            }
        )
}
