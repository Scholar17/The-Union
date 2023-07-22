package com.health.theunion.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LoadingDialog(
    modifier: Modifier = Modifier,
    message: String,
    dismissOnBackPress: Boolean = true,
    dismissOnOutside: Boolean = true,
    onDismissClick: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissClick,
        properties = DialogProperties(
            dismissOnBackPress = dismissOnBackPress,
            dismissOnClickOutside = dismissOnOutside
        )
    ) {
        ProgressDialogContent(
            modifier = modifier,
            message = message
        )
    }
}