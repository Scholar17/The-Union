package com.health.theunion.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.health.theunion.R
import com.health.theunion.ui.theme.dimen

@Composable
fun CommonTextField(
    modifier: Modifier = Modifier,
    textFieldLabel: String = "",
    placeholder: String,
    value: String = "",
    countLimit: Int = 40,
    onValueChanged: (String) -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Go,
    onValueCleared: () -> Unit = {},
    isError: Boolean = false,
    errorMessage: String = "",
    keyboardAction: (KeyboardActionScope) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (textFieldLabel.isNotEmpty()) {
            Text(
                modifier = modifier.padding(
                    vertical = MaterialTheme.dimen.small,
                    horizontal = MaterialTheme.dimen.base
                ),
                text = textFieldLabel,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Row(
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = MaterialTheme.dimen.base_7x)
                .clip(RoundedCornerShape(MaterialTheme.dimen.base_2x))
                .border(
                    width = 1.dp,
                    color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(MaterialTheme.dimen.base_2x)
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(MaterialTheme.dimen.base_2x))
            BasicTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.dimen.base)
                    .weight(1f, fill = false),
                value = value,
                onValueChange = {
                        if (it.length <= countLimit) {
                            onValueChanged(it)
                        }
                },
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                decorationBox = { innerTextField ->
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    innerTextField()
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                keyboardActions = KeyboardActions(
                    onDone = keyboardAction,
                    onGo = keyboardAction,
                    onNext = keyboardAction,
                    onPrevious = keyboardAction,
                    onSearch = keyboardAction,
                    onSend = keyboardAction
                ),
                cursorBrush = SolidColor(
                    value = MaterialTheme.colorScheme.primary
                )
            )
            if (value.isNotEmpty()) {
                IconButton(onClick = onValueCleared) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_small_cross),
                        contentDescription = "Trailing Icon",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant

                    )
                }
            }
        }
        Row {
            VisibilityAnimator(
                isVisible = isError,
                errorMessage = errorMessage
            )
        }
    }
}

@Composable
@Preview
private fun ChatTextFieldPreview() {
    Surface {
        CommonTextField(
            placeholder = "Placeholder",
            value = "",
            onValueChanged = {},
            onValueCleared = { },
            isError = false,
            errorMessage = ""
        )
    }
}

@Composable
@Preview
private fun ChatTextFieldErrorPreview() {
    Surface {
        CommonTextField(
            placeholder = "Placeholder",
            value = "Text Data",
            onValueChanged = {},
            onValueCleared = { },
            isError = true,
            errorMessage = "Error message"
        )
    }
}