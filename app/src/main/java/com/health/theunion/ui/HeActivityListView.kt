package com.health.theunion.ui

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.health.theunion.R
import com.health.theunion.domain.events.HeActivityListEvent
import com.health.theunion.navigation.Destinations
import com.health.theunion.presentation.HeActivityListAction
import com.health.theunion.presentation.PatientReferralListAction
import com.health.theunion.ui.components.ButtonType
import com.health.theunion.ui.components.CommonDialog
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar

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


}