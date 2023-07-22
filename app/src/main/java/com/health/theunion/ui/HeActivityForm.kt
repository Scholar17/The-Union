package com.health.theunion.ui

import android.app.DatePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.health.theunion.R
import com.health.theunion.domain.events.HeActivityFormEvent
import com.health.theunion.domain.events.PatientReferralFormEvent
import com.health.theunion.navigation.Destinations
import com.health.theunion.presentation.HeActivityFormAction
import com.health.theunion.presentation.PatientReferralFormAction
import com.health.theunion.ui.components.ButtonType
import com.health.theunion.ui.components.CommonDialog
import com.health.theunion.ui.components.CommonTextField
import com.health.theunion.ui.components.LoadingDialog
import com.health.theunion.ui.components.VerticalSpacer
import com.health.theunion.ui.theme.dimen
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.GregorianCalendar

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun HeActivityForm(
    vm: HeActivityFormViewModel,
    navController: NavController
) {
    val date = vm._date.value
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val error = vm.heActivityError.collectAsState()
    val loading = vm.heActivityLoading.collectAsState()
    val form = vm.patientReferralForm.collectAsState()

    val volunteer = arrayOf("Vol1", "Vol2")
    var volunteerExpended = remember { mutableStateOf(false) }
    var volunteerSelected = remember { mutableStateOf(volunteer[0]) }

    if(loading.value){
        LoadingDialog(
            dismissOnOutside = true,
            dismissOnBackPress = true,
            message = stringResource(id = R.string.loading_login),
            onDismissClick = {}
        )
    }
    val snackState = remember { SnackbarHostState() }

    /** date picker */
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            val calendar = GregorianCalendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val currentDateInMilli = Calendar.getInstance()
            val dateFormet = SimpleDateFormat("yyyy-MM-dd")
            val fullDate = dateFormet.format(calendar.time)
            val fullDateInMilli = calendar.timeInMillis
            Timber.tag("fullDate").d("$fullDateInMilli")
            Timber.tag("currentDate").d("${Calendar.getInstance().timeInMillis}")
            if (fullDateInMilli > GregorianCalendar.getInstance().timeInMillis) {
                vm.onActionHeActivityForm(
                    HeActivityFormAction.ShowErrorDialog
                )
            } else {
                vm.onActionHeActivityForm(
                    HeActivityFormAction.ChangeDate(
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
        vm.heActivityFormEvent.collectLatest {
            when (it) {
                HeActivityFormEvent.DatePickerClick -> {
                    datePickerDialog.show()
                }
                HeActivityFormEvent.NavigateToHistoryList -> {
                    navController.navigate(Destinations.HeActivityList.route){
                        popUpTo(Destinations.HeActivityForm.route){
                            inclusive = true
                            saveState = false
                        }
                    }
                }
                is HeActivityFormEvent.ShowSnackBar -> {
                    snackState.showSnackbar(
                        message = it.message
                    )
                }
            }
        }
    }

    if (vm._formState.value.shouldShowErrorDialog) {
        CommonDialog(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(id = R.string.date_error_title),
            text = stringResource(id = R.string.date_error),
            confirmButtonLabel = stringResource(id = R.string.ok),
            confirmButtonType = ButtonType.TONAL_BUTTON,
            confirmButtonAction = {
                vm.onActionHeActivityForm(
                    HeActivityFormAction.ErrorDialogOk
                )
            },
            confirmButtonColors = MaterialTheme.colorScheme.onPrimary,
            confirmButtonLabelColors = MaterialTheme.colorScheme.primary
        )
    }

    Scaffold(modifier = Modifier, topBar = {
        TopAppBar(
            modifier = Modifier,
            title = {
                Text(
                    text = "HE Activity Form",
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            },
            colors = TopAppBarDefaults.smallTopAppBarColors(MaterialTheme.colorScheme.primary),
        )
    },
        snackbarHost = {
            SnackbarHost(snackState)
        },
        content = {paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
                    )
                    .padding(paddingValues)
                    .padding(
                        top = MaterialTheme.dimen.base_4x,
                        start = MaterialTheme.dimen.base_2x,
                        end = MaterialTheme.dimen.base_2x
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            keyboardController?.hide()
                        }
                    )
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.base)
            ) {
                Button(
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)),
                    onClick = {
                        vm.onActionHeActivityForm(
                            action = HeActivityFormAction.DatePickerClick
                        )
                    },
                    contentPadding = PaddingValues(MaterialTheme.dimen.small),
                ) {
                    Row(modifier = Modifier.align(Alignment.CenterVertically)) {
                        Text(date, color = MaterialTheme.colorScheme.outline)
                        Icon(
                            imageVector = Icons.Outlined.ArrowDropDown,
                            contentDescription = "Dropdown",
                            tint = MaterialTheme.colorScheme.onSurface.copy(0.5f),
                            modifier = Modifier
                                .size(20.dp)
                        )
                    }
                }
                VerticalSpacer(size = MaterialTheme.dimen.base_2x)
                CommonTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChanged = {
                        vm.onActionHeActivityForm(
                            HeActivityFormAction.ChangeAddress(
                                address = it
                            )
                        )
                    },
                    placeholder = stringResource(id = R.string.address),
                    value = form.value.address,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    onValueCleared = {
                        vm.onActionHeActivityForm(
                            HeActivityFormAction.ChangeAddress(
                                address = ""
                            )
                        )
                    },
                    isError = error.value.errorAddress,
                    errorMessage = stringResource(id = R.string.address_empty)
                )
                VerticalSpacer(size = MaterialTheme.dimen.base)
                Text(text = "Select Volunteer", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimaryContainer)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ExposedDropdownMenuBox(
                        expanded = volunteerExpended.value,
                        onExpandedChange = {
                            volunteerExpended.value = !volunteerExpended.value
                        }
                    ) {
                        TextField(
                            value = volunteerSelected.value,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {},
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = volunteerExpended.value,
                            onDismissRequest = { volunteerExpended.value = false }
                        ) {
                            volunteer.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        volunteerSelected.value = item
                                        volunteerExpended.value = false
                                        vm.onActionHeActivityForm(
                                            HeActivityFormAction.ChangeVolunteer(
                                                volunteer = item
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
                VerticalSpacer(size = MaterialTheme.dimen.base)
                CommonTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChanged = {
                        vm.onActionHeActivityForm(
                            HeActivityFormAction.ChangeAttendeeList(
                                attendeeList = it
                            )
                        )
                    },
                    placeholder = stringResource(id = R.string.attendee_list),
                    value = form.value.attendeesList,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    onValueCleared = {
                        vm.onActionHeActivityForm(
                            HeActivityFormAction.ChangeAttendeeList(
                                attendeeList = ""
                            )
                        )
                    },
                    isError = error.value.errorAttendee,
                    errorMessage = stringResource(id = R.string.attendee_empty)
                )
                VerticalSpacer(size = MaterialTheme.dimen.base_2x)
                CommonTextField(
                    modifier = Modifier
                        .width(MaterialTheme.dimen.extra_large),
                    onValueChanged = {
                        vm.onActionHeActivityForm(
                            HeActivityFormAction.ChangeMale(
                                male = it.toInt()
                            )
                        )
                    },
                    placeholder = stringResource(id = R.string.male),
                    value = if(form.value.male == 0) "" else form.value.male.toString(),
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    onValueCleared = {
                        vm.onActionHeActivityForm(
                            HeActivityFormAction.ChangeMale(
                                male = 0
                            )
                        )
                    },
                    isError = error.value.errorMale,
                    errorMessage = stringResource(id = R.string.male_empty)
                )
                VerticalSpacer(size = MaterialTheme.dimen.base_2x)
                CommonTextField(
                    modifier = Modifier
                        .width(MaterialTheme.dimen.extra_large),
                    onValueChanged = {
                        vm.onActionHeActivityForm(
                            HeActivityFormAction.ChangeFemale(
                                female = it.toInt()
                            )
                        )
                    },
                    placeholder = stringResource(id = R.string.female),
                    value = if(form.value.female == 0) "" else form.value.female.toString(),
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    onValueCleared = {
                        vm.onActionHeActivityForm(
                            HeActivityFormAction.ChangeFemale(
                                female = 0
                            )
                        )
                    },
                    isError = error.value.errorFemale,
                    errorMessage = stringResource(id = R.string.female_empty)
                )
                VerticalSpacer(size = MaterialTheme.dimen.base_2x)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.width(MaterialTheme.dimen.base_12x),
                        onClick = {
                            vm.onActionHeActivityForm(
                                HeActivityFormAction.SubmitClick
                            )
                            keyboardController!!.hide()
                        },
                        shape = CircleShape,
                    ) {
                        Text(text = stringResource(id = R.string.submit))
                    }
                }
                VerticalSpacer(size = MaterialTheme.dimen.base_2x)
            }
        }
    )

}