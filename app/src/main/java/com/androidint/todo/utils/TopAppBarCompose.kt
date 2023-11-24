package com.androidint.todo.utils

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarCompose(
    title: String,
    navigate: (destination: String) -> Unit
) {
    val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "This is my text to send.")
        type = "text/plain"
    }
    val shareIntent = Intent.createChooser(sendIntent, null)
    val context = LocalContext.current

    Column {
        TopAppBar(
            title = {
                Text(title)
            },
            Modifier.background(Color.Black),
            navigationIcon = {
                IconButton(onClick = {
                    Toast.makeText(context, "Menu clicked", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(Icons.Filled.Menu, null)
                }
            }, actions = {
                IconButton(onClick = { context.startActivity(shareIntent) }) {
                    Icon(Icons.Filled.Share, null)
                }
                IconButton(onClick = {
                    Toast.makeText(context, "calendar clicked", Toast.LENGTH_SHORT).show()
                    navigate(Screens.timeline)
                }) {
                    Icon(Icons.Filled.DateRange, null)
                }
            })
    }

}