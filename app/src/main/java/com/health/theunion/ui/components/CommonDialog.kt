package com.health.theunion.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.DialogProperties

enum class ButtonType {
    SOLID_BUTTON,
    TONAL_BUTTON,
    TEXT_BUTTON
}

@Composable
fun CommonDialog(
    modifier: Modifier,
    title: String = "",
    text: String = "",

    dismissButtonLabel: String = "",
    dismissAction: () -> Unit = {},

    confirmButtonLabel: String = "",
    confirmButtonType: ButtonType? = null,
    confirmButtonColors: Color,
    confirmButtonLabelColors: Color,
    confirmButtonAction: () -> Unit = {}

) {
    AlertDialog(
        modifier = modifier.fillMaxWidth(),
        onDismissRequest = { dismissAction() },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        title = {
            if (title.isNotEmpty()) {
                Text(text = title)
            }
        },
        text = {
            if (text.isNotEmpty()) {
                Text(text = text)
            }
        },
        dismissButton = {
            if (dismissButtonLabel.isNotEmpty()) {
                TextButton(onClick = { dismissAction() }) {
                    Text(
                        text = dismissButtonLabel,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            confirmButtonType?.let {
                when (confirmButtonType) {
                    ButtonType.SOLID_BUTTON -> {
                        confirmButtonLabel?.let {
                            Button(
                                onClick = confirmButtonAction,
                                colors = ButtonDefaults.buttonColors(confirmButtonColors)
                            ) {
                                Text(
                                    text = confirmButtonLabel,
                                    color = confirmButtonLabelColors
                                )
                            }
                        }
                    }
                    ButtonType.TONAL_BUTTON -> {
                        if (confirmButtonLabel.isNotEmpty()) {
                            FilledTonalButton(onClick = { confirmButtonAction() }) {
                                Text(text = confirmButtonLabel)
                            }
                        }
                    }
                    ButtonType.TEXT_BUTTON -> { }
                }
            }
        },
    )

}