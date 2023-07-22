package com.health.theunion.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import com.health.theunion.ui.components.VerticalSpacer
import com.health.theunion.ui.theme.dimen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeActivityDetail(
    vm: HeActivityDetailViewModel,
    navController: NavController,
    id: Long
) {


    Scaffold(modifier = Modifier, topBar = {
        TopAppBar(
            modifier = Modifier,
            title = {
                Text(
                    text = "HE Activity History Detail",
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
                        VerticalSpacer(MaterialTheme.dimen.base_2x)
                        RowComponent(
                            label = "Date",
                            data = result.date,
                            background = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                        RowComponent(
                            label = "Address",
                            data = result.address,
                        )
                        RowComponent(
                            label = "Volunteer",
                            data = result.volunteer,
                            background = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                        RowComponent(
                            label = "HE Attendees List",
                            data = result.heAttendeesList
                        )
                        RowComponent(
                            label = "Male Count",
                            data = result.male.toString(),
                            background = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                        RowComponent(
                            label = "Female Count",
                            data = result.female.toString()
                        )
                        VerticalSpacer(MaterialTheme.dimen.base)
                    }
                }
            }
        }
    }
}