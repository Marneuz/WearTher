package com.marneux.marneweather.presentation.views.home.composables

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.marneux.marneweather.R

@Composable
fun prepareLocationPermissionRequest(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
): ActivityResultLauncher<Array<String>> {

    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { isPermitted ->
            val isCoarseLocationPermitted =
                isPermitted.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)
            val isFineLocationPermitted =
                isPermitted.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
            if (isCoarseLocationPermitted || isFineLocationPermitted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    )
}

@Composable
fun ShowPermissionWarningDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onOpenSettings: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(stringResource(id = R.string.home_dialog_title)) },
            text = { Text(stringResource(id = R.string.home_dialog_description)) },
            confirmButton = {
                TextButton(onClick = { onDismiss() }) {
                    Text(stringResource(id = R.string.accept))
                }
            },
            dismissButton = {
                TextButton(onClick = onOpenSettings) {
                    Text(stringResource(id = R.string.settings))
                }
            }
        )
    }
}
