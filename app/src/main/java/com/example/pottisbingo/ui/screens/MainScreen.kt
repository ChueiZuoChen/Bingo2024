package com.example.pottisbingo.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.pottisbingo.TAG
import com.google.firebase.auth.FirebaseAuth


@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainScreenViewModel) {
    val shouldShowDialog = remember {
        mutableStateOf(true)
    }
    if (shouldShowDialog.value) {
        NickNameDialog {
            Log.d(TAG, "MainScreen: $it")
            viewModel.member.value?.nickName = it.first
            shouldShowDialog.value = it.second
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = viewModel.member.value.toString())
        Button(onClick = { FirebaseAuth.getInstance().signOut() }) {
            Text(text = "SignOut")
        }

    }

}

@Composable
fun NickNameDialog(
    onConfirmRequest: (Pair<String, Boolean>) -> Unit,
) {
    var text by remember { mutableStateOf("") }
    AlertDialog(
        title = { Text(text = "NickName") },
        text = {
            OutlinedTextField(value = text, onValueChange = { text = it }, maxLines = 1)
        },
        onDismissRequest = {
        },
        confirmButton = {
            TextButton(onClick = {
                onConfirmRequest(Pair(text, false))
            }) {
                Text(text = "Confirm")
            }
        }
    )
}