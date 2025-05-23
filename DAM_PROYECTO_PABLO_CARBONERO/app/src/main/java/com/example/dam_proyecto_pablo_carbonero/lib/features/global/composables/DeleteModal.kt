package com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DeleteModal(
    dismissFunction: (() -> Unit),
    onDeletePressed: (() -> Unit)
){

    AlertDialog(
        title = { Text("DELETE") },
        text = { Text("Deleting an element may delete other ones in the app if they depend of it") },
        onDismissRequest = { dismissFunction() },
        confirmButton = {
            TextButton(
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                ),
                onClick = {
                    onDeletePressed()
                    dismissFunction()
                }
            ) {
                Text("Delete", color = Color.White)
            }
        }
    )

}