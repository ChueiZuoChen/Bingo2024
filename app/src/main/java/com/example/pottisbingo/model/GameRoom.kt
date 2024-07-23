package com.example.pottisbingo.model

data class GameRoom(
    var id: String = "",
    var title: String = "Welcome",
    var status: Int = 0,
    var init: Member? = null,
    var join: Member? = null,
)

