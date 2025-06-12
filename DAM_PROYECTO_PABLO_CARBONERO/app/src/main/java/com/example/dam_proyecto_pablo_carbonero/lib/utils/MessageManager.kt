package com.example.dam_proyecto_pablo_carbonero.lib.utils

/**
 * Para gestionar los toast que se muestran al usuario
 */
data class MessageManager(
    val isSuccess: Boolean,
    val message: String = "Unexpected error. Try again later"
)
