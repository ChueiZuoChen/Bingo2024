package com.example.pottisbingo.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.pottisbingo.model.Member

class MainScreenViewModel : ViewModel() {
    val member = mutableStateOf<Member?>(null)
    val roomId = mutableStateOf<String?>(null)
    val isCreator = mutableStateOf<Boolean>(false)
}