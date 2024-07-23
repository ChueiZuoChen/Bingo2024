package com.example.pottisbingo.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.example.pottisbingo.R
import com.example.pottisbingo.TAG
import com.example.pottisbingo.model.Member
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainScreenViewModel) {
    val shouldShowDialog = remember {
        mutableStateOf(viewModel.member.value?.nickName.isNullOrEmpty())
    }
    if (shouldShowDialog.value) {
        NickNameDialog(member = viewModel.member.value) {
            Log.d(TAG, "MainScreen: $it")
            viewModel.member.value?.nickName = it.first
            shouldShowDialog.value = it.second
        }
    }

    val avatarIds = intArrayOf(
        R.drawable.avatar_0,
        R.drawable.avatar_1,
        R.drawable.avatar_2,
        R.drawable.avatar_3,
        R.drawable.avatar_4,
        R.drawable.avatar_5,
        R.drawable.avatar_6
    )
    BoxWithConstraints {
        ConstraintLayout(modifier = Modifier.fillMaxSize(), constraintSet = constraints()) {
            // avatar
            val avatarIconActionState = remember { mutableStateOf(false) }
            Image(
                modifier = Modifier
                    .clickable { avatarIconActionState.value = !avatarIconActionState.value }
                    .size(40.dp)
                    .layoutId("avatarIcon"), painter = painterResource(id = avatarIds[viewModel.member.value?.avatarId ?: 0]), contentDescription = ""
            )
            if (avatarIconActionState.value) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId("avatarIconRow")
                        .padding(start = 30.dp, end = 30.dp), horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    avatarIds.forEachIndexed { index, value ->
                        Image(modifier = Modifier
                            .clickable {
                                FirebaseDatabase
                                    .getInstance()
                                    .getReference("users")
                                    .child(viewModel.member.value?.uid.toString())
                                    .child("avatarId")
                                    .setValue(index)
                                avatarIconActionState.value = false
                            }
                            .size(40.dp), painter = painterResource(id = value), contentDescription = null)
                    }
                }
            }

            // nick name
            Text(
                modifier = Modifier
                    .layoutId("nickNameText")
                    .clickable { shouldShowDialog.value = true }, fontSize = 30.sp, text = viewModel.member.value?.nickName.toString()
            )

            // floating action button
            val floatingActionState = remember { mutableStateOf(false) }
            FloatingActionButton(modifier = Modifier.layoutId("floatingActionButton"), onClick = { floatingActionState.value = !floatingActionState.value })
            {
                if (floatingActionState.value) {
                    Icon(Icons.Filled.KeyboardArrowUp, "Floating action button.")
                } else {
                    Icon(Icons.Filled.KeyboardArrowDown, "Floating action button.")
                }
            }
            if (floatingActionState.value) {
                FloatingActionButton(modifier = Modifier
                    .layoutId("signOutButton")
                    .width(100.dp), onClick = { FirebaseAuth.getInstance().signOut() })
                {
                    Text(text = "SignOut")
                }
                FloatingActionButton(modifier = Modifier
                    .layoutId("newGameButton")
                    .width(100.dp), onClick = { /*TODO: new game navigate*/ })
                {
                    Text(text = "New Game")
                }
            }
        }
    }
}

private fun constraints() = ConstraintSet {
    val floatingActionButton = createRefFor("floatingActionButton")
    val signOutButton = createRefFor("signOutButton")
    val newGameButton = createRefFor("newGameButton")
    val nickNameText = createRefFor("nickNameText")
    val avatarIcon = createRefFor("avatarIcon")
    val avatarIconRow = createRefFor("avatarIconRow")
    constrain(floatingActionButton) {
        bottom.linkTo(parent.bottom, margin = 60.dp)
        end.linkTo(parent.end, margin = 60.dp)
    }
    constrain(signOutButton) {
        bottom.linkTo(floatingActionButton.bottom, margin = 80.dp)
        end.linkTo(parent.end, margin = 60.dp)
    }
    constrain(newGameButton) {
        bottom.linkTo(signOutButton.bottom, margin = 80.dp)
        end.linkTo(parent.end, margin = 60.dp)
    }
    constrain(nickNameText) {
        top.linkTo(parent.top, margin = 60.dp)
        end.linkTo(parent.end, margin = 60.dp)
    }
    constrain(avatarIcon) {
        top.linkTo(parent.top, margin = 60.dp)
        end.linkTo(nickNameText.start, margin = 10.dp)
    }
    constrain(avatarIconRow) {
        top.linkTo(avatarIcon.bottom, margin = 16.dp)
        end.linkTo(parent.end, margin = 10.dp)
        start.linkTo(parent.start, margin = 10.dp)
    }
}

@Composable
fun NickNameDialog(
    member: Member?,
    onConfirmRequest: (Pair<String, Boolean>) -> Unit,
) {
    var nickName by remember { mutableStateOf(member?.nickName ?: "") }
    AlertDialog(
        title = { Text(text = "NickName") },
        text = {
            OutlinedTextField(value = nickName, onValueChange = { nickName = it }, maxLines = 1)
        },
        onDismissRequest = {
        },
        confirmButton = {
            TextButton(onClick = {
                member?.let {
                    FirebaseDatabase.getInstance()
                        .getReference("users")
                        .child(it.uid)
                        .child("nickName")
                        .setValue(nickName)
                }
                onConfirmRequest(Pair(nickName, false))
            }) {
                Text(text = "Confirm")
            }
        }
    )
}