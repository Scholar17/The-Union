package com.health.theunion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.health.theunion.R
import com.health.theunion.ui.components.VerticalSpacer
import com.health.theunion.ui.theme.dimen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientReferralDetail(navController: NavController, vm: PatientReferralDetailViewModel, id: Long) {

    Scaffold(modifier = Modifier, topBar = {
        TopAppBar(
            modifier = Modifier,
            title = {
                Text(
                    text = "Patient Referral History Detail",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.primary),

            )
    }) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
                )
                .padding(horizontal = MaterialTheme.dimen.base_2x)
        ) {
            itemsIndexed(vm.result) { index, result ->
                if (result.id == id) {
                    Column(
                        modifier = Modifier
                            .padding(it)
                            .padding(vertical = MaterialTheme.dimen.base_2x)
                            .clip(shape = RoundedCornerShape(MaterialTheme.dimen.base))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(vertical = MaterialTheme.dimen.base_2x),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = MaterialTheme.dimen.base_2x),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.padding(end = MaterialTheme.dimen.base_2x),
                                text = stringResource(id = R.string.age),
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.outline
                            )
                            Box(
                                modifier = Modifier
                                    .width(MaterialTheme.dimen.base_6x)
                                    .height(MaterialTheme.dimen.base_6x)
                                    .clip(shape = CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                    Text(
                                        text = result.age.toString(),
                                        style = MaterialTheme.typography.titleLarge,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                            }
                            Text(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = MaterialTheme.dimen.base_2x),
                                text = vm.detailDateTime(milli = result.saveDateMilli),
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                        VerticalSpacer(MaterialTheme.dimen.base_2x)
                        RowComponent(
                            label = "Name",
                            data = result.name,
                            background = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                        RowComponent(
                            label = "Sex",
                            data = if(result.sex == 0) "Female" else "Male",
                        )
                        RowComponent(
                            label = "Refer Date",
                            data = result.referDate,
                            background = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                        RowComponent(
                            label = "Township",
                            data = result.township
                        )
                        RowComponent(
                            label = "Address",
                            data = result.address,
                            background = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                        RowComponent(
                            label = "Refer From",
                            data = result.referFrom
                        )
                        RowComponent(
                            label = "Refer To",
                            data = result.referTo,
                            background = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                        VerticalSpacer(MaterialTheme.dimen.base)
                        RowComponent(
                            label = "Sign and Symptom",
                            data = result.signAndSymptom
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RowComponent(
    label: String = "",
    data: String = "",
    background: Color = MaterialTheme.colorScheme.surface,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(background)
            .padding(
                vertical = MaterialTheme.dimen.base_2x,
                horizontal = MaterialTheme.dimen.base_2x
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(4f),
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            modifier = Modifier.weight(3f),
            text = data,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.End
        )
    }
}