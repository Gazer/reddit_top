package com.example.reddittop.models

data class Data(
    val after: String,
    val before: Any,
    val children: List<Children>,
    val modhash: String
)