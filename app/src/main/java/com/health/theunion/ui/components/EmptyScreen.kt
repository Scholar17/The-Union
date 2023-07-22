package com.health.theunion.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.health.theunion.R
import com.health.theunion.ui.theme.dimen

@Composable
fun EmptyScreen(text: String) {
    Box(modifier = Modifier
        .fillMaxSize(),
        contentAlignment = Alignment.Center) {
        Text(text = text, modifier = Modifier
            .align(Alignment.TopStart)
            .padding(horizontal = MaterialTheme.dimen.base_2x, vertical = MaterialTheme.dimen.base), fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.outline)
        Image(painter = painterResource(id = R.drawable.empty), contentDescription = "Empty View", modifier = Modifier
            .align(Alignment.Center))
    }
}