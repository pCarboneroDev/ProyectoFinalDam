package com.example.dam_proyecto_pablo_carbonero.lib.utils

data class MessageManager(
    val isSuccess: Boolean,
    val message: String = "Unexpected error. Try again later"
)
