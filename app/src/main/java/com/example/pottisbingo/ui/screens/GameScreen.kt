package com.example.pottisbingo.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import com.example.pottisbingo.model.NumberButton
import com.google.firebase.database.FirebaseDatabase

private const val TAG = "GameScreen"

object GameStatus {
    const val STATUS_INIT = 0
    const val STATUS_CREATED = 1
    const val STATUS_JOINED = 2
    const val STATUS_CREATOR_TURN = 3
    const val STATUS_JOINER_TURN = 4
    const val STATUS_CREATOR_BINGO = 5
    const val STATUS_JOINER_BINGO = 6
}

@Composable
fun GameScreen(
    viewModel: MainScreenViewModel,
    onNavigateToMainScreen: () -> Unit,
) {
    val isCreator = viewModel.isCreator.value
    val roomId = viewModel.roomId.value!!

    if (isCreator) {
        for (i in 1..25) {
            FirebaseDatabase.getInstance().getReference("rooms")
                .child(roomId)
                .child("number")
                .child(i.toString())
                .setValue(false)
        }
        FirebaseDatabase.getInstance().getReference("rooms")
            .child(roomId)
            .child("status")
            .setValue(GameStatus.STATUS_CREATED)
    } else {
        FirebaseDatabase.getInstance().getReference("rooms")
            .child(roomId)
            .child("status")
            .setValue(GameStatus.STATUS_JOINED)
    }

    val numberMap = hashMapOf<Int, Int>()
    var buttons = mutableListOf<NumberButton>()
    for (i in 0..24) {
        val button = NumberButton(i + 1)
        buttons.add(button)
    }
    buttons.shuffle()
    buttons.forEachIndexed { index, numberButton ->
        Log.d(TAG, "$index : $numberButton")
    }
    DrawLinesWithDots(buttons)

}

