package com.example.pottisbingo.ui.screens

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pottisbingo.model.NumberButton


@Composable
fun DrawLinesWithDots(numbers: MutableList<NumberButton>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        repeat(5) { rowIndex ->
            Log.d("potti", "DrawLinesWithDots: $rowIndex")
//            LineWithDots(numbers[rowIndex * 5+1].number)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(5) { dotIndex ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(60.dp)
                    ) {
                        Canvas(modifier = Modifier.size(70.dp)) {
                            drawCircleDot()
                        }
                        Text(text = "${numbers[(rowIndex) * (dotIndex)].number}", style = TextStyle(fontSize = 30.sp, color = Color.Blue, fontWeight = FontWeight.Bold))
                    }
                }
            }
        }
    }
}

@Composable
fun LineWithDots(startingNumber: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(5) { dotIndex ->
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(60.dp)
            ) {
                Canvas(modifier = Modifier.size(70.dp)) {
                    drawCircleDot()
                }
                Text(text = "${startingNumber + dotIndex}", style = TextStyle(fontSize = 30.sp, color = Color.Blue, fontWeight = FontWeight.Bold))
            }
        }
    }
}

fun DrawScope.drawCircleDot() {
    drawCircle(
        color = Color.Gray,
        radius = 80f
    )
}
