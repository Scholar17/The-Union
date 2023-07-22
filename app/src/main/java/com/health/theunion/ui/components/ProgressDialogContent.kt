package com.health.theunion.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import com.health.theunion.ui.theme.dimen

@Composable
fun ProgressDialogContent(
    modifier: Modifier = Modifier,
    message: String
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(MaterialTheme.dimen.base_2x))
            .background(MaterialTheme.colorScheme.surface)
            .padding(
                horizontal = MaterialTheme.dimen.base_2x,
                vertical = MaterialTheme.dimen.base_3x
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = modifier.size(MaterialTheme.dimen.base_4x)
        )
        VerticalSpacer(size = MaterialTheme.dimen.base_2x)
        Text(
            text = message,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
    }
}