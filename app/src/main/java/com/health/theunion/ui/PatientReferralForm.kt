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
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import com.health.theunion.domain.events.PatientReferralFormEvent
import com.health.theunion.navigation.Destinations
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PatientReferralForm(
    vm: PatientReferralFormViewModel,
    navController: NavController
) {

    val date = vm._date.value
    val keyboardController = LocalSoftwareKeyboardController.current
    val context = LocalContext.current

    val error = vm.patientReferralError.collectAsState()
    val loading = vm.patientReferralLoading.collectAsState()
    val form = vm.patientReferralForm.collectAsState()

    val township = arrayOf("AMP", "AMT", "CAT", "CMT", "PTG", "PGT", "MHA")
    val townshipExpended = remember { mutableStateOf(false) }
    val townshipSelected = remember { mutableStateOf(township[0]) }

    val referFrom = arrayOf("VOL1", "VOL2")
    val referFromExpended = remember { mutableStateOf(false) }
    val referFromSelected = remember { mutableStateOf(referFrom[0]) }

    val referTo = arrayOf("Union", "THD", "Other")
    val referToExpended = remember { mutableStateOf(false) }
    val referToSelected = remember { mutableStateOf(referTo[0]) }

    val signAndSymptom = arrayOf("Fever", "Weight Loss", "Cough more than Two Weeks")
    val signAndSymptomExpended = remember { mutableStateOf(false) }
    val signAndSymptomSelected = remember { mutableStateOf(signAndSymptom[0]) }


    val radioOptions = listOf("Male", "Female")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf("") }

    val snackState = remember { SnackbarHostState() }

    if (loading.value) {
        LoadingDialog(
            dismissOnOutside = true,
            dismissOnBackPress = true,
            message = stringResource(id = R.string.loading_login),
            onDismissClick = {}
        )
    }

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
                vm.onActionPatientReferralForm(
                    PatientReferralFormAction.ShowErrorDialog
                )
            } else {
                vm.onActionPatientReferralForm(
                    PatientReferralFormAction.ChangeDate(
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
        vm.patientReferralFormEvent.collectLatest {
            when (it) {
                PatientReferralFormEvent.DatePickerClick -> {
                    datePickerDialog.show()
                }

                PatientReferralFormEvent.NavigateToHistoryList -> {
                    navController.navigate(Destinations.PatientReferralList.route) {
                        popUpTo(Destinations.PatientReferralForm.route) {
                            inclusive = true
                            saveState = false
                        }
                    }
                }

                is PatientReferralFormEvent.ShowSnackBar -> {
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
                vm.onActionPatientReferralForm(
                    PatientReferralFormAction.ErrorDialogOk
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
                    text = "Patient Referral Form",
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
        content = { paddingValues ->
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
                    colors = ButtonDefaults.buttonColors(
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = 0.14f
                        )
                    ),
                    onClick = {
                        vm.onActionPatientReferralForm(
                            action = PatientReferralFormAction.DatePickerClick
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
                        vm.onActionPatientReferralForm(
                            PatientReferralFormAction.ChangeUserName(
                                name = it
                            )
                        )
                    },
                    placeholder = stringResource(id = R.string.name),
                    value = form.value.name,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    onValueCleared = {
                        vm.onActionPatientReferralForm(
                            PatientReferralFormAction.ChangeUserName(
                                name = ""
                            )
                        )
                    },
                    isError = error.value.errorName,
                    errorMessage = stringResource(id = R.string.name_empty)
                )
                VerticalSpacer(size = MaterialTheme.dimen.base)
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimen.base_2x)
                ) {
                    radioOptions.forEach { text ->
                        Column(modifier = Modifier) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = (text == selectedOption),
                                    onClick = {
                                        if (text != radioOptions[0]) {
                                            vm.onActionPatientReferralForm(
                                                action = PatientReferralFormAction.chooseRadio(
                                                    sex = 0
                                                )
                                            )
                                        } else {
                                            vm.onActionPatientReferralForm(
                                                action = PatientReferralFormAction.chooseRadio(
                                                    sex = 1
                                                )
                                            )
                                        }
                                        onOptionSelected(text)
                                    }
                                )
                                Text(
                                    text = text,
                                )
                            }
                        }
                    }
                }
                VerticalSpacer(size = MaterialTheme.dimen.base)
                CommonTextField(
                    modifier = Modifier
                        .width(MaterialTheme.dimen.extra_large),
                    onValueChanged = {
                        vm.onActionPatientReferralForm(
                            PatientReferralFormAction.ChangeAge(
                                age = it.toInt()
                            )
                        )
                    },
                    placeholder = stringResource(id = R.string.age),
                    value = if (form.value.age == 0) "" else form.value.age.toString(),
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                    onValueCleared = {
                        vm.onActionPatientReferralForm(
                            PatientReferralFormAction.ChangeAge(
                                age = 0
                            )
                        )
                    },
                    isError = error.value.errorAge,
                    errorMessage = stringResource(id = R.string.age_over_120)
                )
                VerticalSpacer(size = MaterialTheme.dimen.base)
                Text(
                    text = "Select Township",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ExposedDropdownMenuBox(
                        expanded = townshipExpended.value,
                        onExpandedChange = {
                            townshipExpended.value = !townshipExpended.value
                        }
                    ) {
                        TextField(
                            value = townshipSelected.value,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {},
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = townshipExpended.value,
                            onDismissRequest = { townshipExpended.value = false }
                        ) {
                            township.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        townshipSelected.value = item
                                        townshipExpended.value = false
                                        vm.onActionPatientReferralForm(
                                            PatientReferralFormAction.ChangeTownship(
                                                township = item
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
                VerticalSpacer(size = MaterialTheme.dimen.base_2x)
                CommonTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onValueChanged = {
                        vm.onActionPatientReferralForm(
                            PatientReferralFormAction.ChangeAddress(
                                address = it
                            )
                        )
                    },
                    placeholder = stringResource(id = R.string.address),
                    value = form.value.address,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next,
                    onValueCleared = {
                        vm.onActionPatientReferralForm(
                            PatientReferralFormAction.ChangeAddress(
                                address = ""
                            )
                        )
                    },
                    isError = error.value.errorAddress,
                    errorMessage = stringResource(id = R.string.address_empty)
                )
                VerticalSpacer(size = MaterialTheme.dimen.base)
                Text(
                    text = "Select Refer From",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ExposedDropdownMenuBox(
                        expanded = referFromExpended.value,
                        onExpandedChange = {
                            referFromExpended.value = !referFromExpended.value
                        }
                    ) {
                        TextField(
                            value = referFromSelected.value,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {},
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = referFromExpended.value,
                            onDismissRequest = { referFromExpended.value = false }
                        ) {
                            referFrom.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        referFromSelected.value = item
                                        referFromExpended.value = false
                                        vm.onActionPatientReferralForm(
                                            PatientReferralFormAction.ChangeReferFrom(
                                                referFrom = item
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
                VerticalSpacer(size = MaterialTheme.dimen.base)
                Text(
                    text = "Select Refer To",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ExposedDropdownMenuBox(
                        expanded = referToExpended.value,
                        onExpandedChange = {
                            referToExpended.value = !referToExpended.value
                        }
                    ) {
                        TextField(
                            value = referToSelected.value,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {},
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = referToExpended.value,
                            onDismissRequest = { referToExpended.value = false }
                        ) {
                            referTo.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        referToSelected.value = item
                                        referToExpended.value = false
                                        vm.onActionPatientReferralForm(
                                            PatientReferralFormAction.ChangeReferTo(
                                                referTo = item
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
                VerticalSpacer(size = MaterialTheme.dimen.base)
                Text(
                    text = "Select Sign and Symptom",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    ExposedDropdownMenuBox(
                        expanded = signAndSymptomExpended.value,
                        onExpandedChange = {
                            signAndSymptomExpended.value = !signAndSymptomExpended.value
                        }
                    ) {
                        TextField(
                            value = signAndSymptomSelected.value,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = {},
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = signAndSymptomExpended.value,
                            onDismissRequest = { signAndSymptomExpended.value = false }
                        ) {
                            signAndSymptom.forEach { item ->
                                DropdownMenuItem(
                                    text = { Text(text = item) },
                                    onClick = {
                                        signAndSymptomSelected.value = item
                                        signAndSymptomExpended.value = false
                                        vm.onActionPatientReferralForm(
                                            PatientReferralFormAction.ChangeSignAndSymptom(
                                                signAndSymptom = item
                                            )
                                        )
                                    }
                                )
                            }
                        }
                    }
                }
                VerticalSpacer(size = MaterialTheme.dimen.base_2x)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        modifier = Modifier.width(MaterialTheme.dimen.base_12x),
                        onClick = {
                            vm.onActionPatientReferralForm(
                                PatientReferralFormAction.SubmitClick
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpandIcon(expanded: Boolean) {
    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
}