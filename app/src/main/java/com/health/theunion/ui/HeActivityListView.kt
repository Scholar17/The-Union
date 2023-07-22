package com.health.theunion.ui

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import com.health.theunion.R
import com.health.theunion.domain.events.HeActivityListEvent
import com.health.theunion.navigation.Destinations
import com.health.theunion.presentation.HeActivityListAction
import com.health.theunion.ui.components.ButtonType
import com.health.theunion.ui.components.CommonDialog
import com.health.theunion.ui.components.EmptyScreen
import com.health.theunion.ui.components.VerticalSpacer
import com.health.theunion.ui.theme.dimen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HeActivityListView(
    vm: HeActivityListViewModel,
    navController: NavController
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    /** date picker */
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val calendar = GregorianCalendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val currentDateInMilli = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val fullDate = dateFormat.format(calendar.time)
            val fullDateInMilli = calendar.timeInMillis
            Timber.tag("fullDate").d("$fullDateInMilli")
            Timber.tag("currentDate").d("${Calendar.getInstance().timeInMillis}")
            if (fullDateInMilli > GregorianCalendar.getInstance().timeInMillis) {
                vm.onActionHeActivityList(
                    HeActivityListAction.ShowErrorDialog
                )
            } else {
                vm.onActionHeActivityList(
                    HeActivityListAction.ChangeDate(
                        date = fullDate,
                        dateInMilli = fullDateInMilli
                    )
                )
            }
        },
        Calendar.getInstance().get(Calendar.YEAR),
        Calendar.getInstance().get(Calendar.MONTH),
        Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
    )

    LaunchedEffect(key1 = true) {
        vm._dateInMilli.value = vm.getCurrentDateInMilli()
        vm.historyListEvent.collectLatest {
            when (it) {
                HeActivityListEvent.NavigateToHeActivityForm -> {
                    navController.navigate(Destinations.HeActivityForm.route)
                }

                is HeActivityListEvent.NavigateToHistoryDetail -> {
                    navController.navigate(Destinations.HeActivityDetail.passId(dataId = it.id))
                }

                HeActivityListEvent.ShowDateTimeDialog -> {
                }
            }
        }
    }

    if (vm.historyListState.value.shouldShowDialog) {
        CommonDialog(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.delete_confirm_title),
            text = stringResource(id = R.string.delete_confirm),
            dismissButtonLabel = stringResource(id = R.string.cancel_button),
            dismissAction = {
                vm.onActionHeActivityList(
                    action = HeActivityListAction.DismissDeleteConfirmDialog
                )
            },
            confirmButtonLabel = stringResource(id = R.string.confirm_button),
            confirmButtonType = ButtonType.SOLID_BUTTON,
            confirmButtonAction = {
                scope.launch {
                    vm.onActionHeActivityList(
                        action = HeActivityListAction.DeleteHistoryItem(vm.historyListState.value.deleteItem)
                    )
                    vm.getHistory()
                }
            },
            confirmButtonColors = MaterialTheme.colorScheme.error,
            confirmButtonLabelColors = MaterialTheme.colorScheme.onError
        )
    }

    if (vm.historyListState.value.shouldShowDeleteAllDialog) {
        CommonDialog(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.delete_confirm_title),
            text = stringResource(id = R.string.delete_all_confirm),
            dismissButtonLabel = stringResource(id = R.string.cancel_button),
            dismissAction = {
                vm.onActionHeActivityList(
                    action = HeActivityListAction.DismissDeleteAllConfirmDialog
                )
            },
            confirmButtonLabel = stringResource(id = R.string.confirm_button),
            confirmButtonType = ButtonType.SOLID_BUTTON,
            confirmButtonAction = {
                scope.launch {
                    vm.onActionHeActivityList(
                        action = HeActivityListAction.DeleteAllHistoryItem
                    )
                }
            },
            confirmButtonColors = MaterialTheme.colorScheme.error,
            confirmButtonLabelColors = MaterialTheme.colorScheme.onError
        )
    }

    Scaffold(topBar = {
        TopAppBar(
            title = {
                Text(
                    text = "HE Activity List",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            actions = {
                IconButton(onClick = {
                    vm.onActionHeActivityList(
                        action = HeActivityListAction.ShowDeleteAllConfirmDialog
                    )
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Delete,
                        contentDescription = "Delete All Data",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                IconButton(onClick = {
                    vm.onActionHeActivityList(
                        action = HeActivityListAction.NavigateToHeActivityForm
                    )
                }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add New Data",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.primary)
        )
    },
        content = {
            if (vm.result.isEmpty()) {
                Column(modifier = Modifier.padding(it)) {
                    EmptyScreen(text = vm._titleName.value)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
                        )
                        .padding(
                            horizontal = MaterialTheme.dimen.base_2x
                        )
                ) {
                    itemsIndexed(vm.result) { index, result ->
                        if (index == 0) {
                            VerticalSpacer(size = MaterialTheme.dimen.base)
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = MaterialTheme.dimen.base)
                                .clip(shape = RoundedCornerShape(MaterialTheme.dimen.base_2x))
                                .clickable(onClick = {
                                    vm.onActionHeActivityList(
                                        action = HeActivityListAction.ShowHistoryDetail(id = result.id!!)
                                    )
                                })
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(
                                    horizontal = MaterialTheme.dimen.base_2x,
                                    vertical = MaterialTheme.dimen.base
                                ),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.base_2x),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(
//                            modifier = Modifier.weight(1f, false),
                                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.base)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        MaterialTheme.dimen.base
                                    ),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = result.date,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(
                                                MaterialTheme.dimen.tiny
                                            )
                                        ) {
                                            Text(
                                                modifier = Modifier.align(Alignment.CenterVertically),
                                                text = result.address,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                        }

                                    }
                                    IconButton(
                                        modifier = Modifier,
                                        onClick = {
                                            vm.onActionHeActivityList(
                                                action = HeActivityListAction.ShowDeleteConfirmDialog(
                                                    id = result.id!!
                                                )
                                            )
                                        }
                                    ) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.ic_outline_delete_24),
                                            contentDescription = "Delete",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(MaterialTheme.dimen.small_icon)
                                        )
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(
                                        MaterialTheme.dimen.base
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(shape = RoundedCornerShape(MaterialTheme.dimen.base))
                                            .background(
                                                MaterialTheme.colorScheme.primary.copy(
                                                    alpha = 0.05f
                                                )
                                            )
                                            .padding(
                                                vertical = MaterialTheme.dimen.small,
                                                horizontal = MaterialTheme.dimen.base
                                            )
                                    ) {
                                        Text(
                                            text = "Male",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                        Text(
                                            text = result.male.toString(),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(shape = RoundedCornerShape(MaterialTheme.dimen.base))
                                            .background(
                                                MaterialTheme.colorScheme.primary.copy(
                                                    alpha = 0.05f
                                                )
                                            )
                                            .padding(
                                                vertical = MaterialTheme.dimen.small,
                                                horizontal = MaterialTheme.dimen.base
                                            )
                                    ) {
                                        Text(
                                            text = "Female",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                        Text(
                                            text = result.female.toString(),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    )


}