package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dam_proyecto_pablo_carbonero.lib.extensions.SortOption
import com.example.dam_proyecto_pablo_carbonero.lib.extensions.getName

@Composable
fun SortSelectorModal(
    currentSortOption: SortOption,
    /*saveMethod: (() -> Unit),*/
    dismissFunction: (() -> Unit),
    sortOptionSelected: ((sort: SortOption) -> Unit)
){
    var selectedOption by remember { mutableStateOf(currentSortOption) }
    AlertDialog(
        title = {
            Text(text = "Sort by")
        },
        text = {
            Column {
                SortOption.entries.forEach { option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                            //.clickable { selectedOption = option }
                            //.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        RadioButton(
                            selected = option == selectedOption,
                            onClick = { selectedOption = option },
                            colors = RadioButtonDefaults.colors(
                                unselectedColor = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = option.getName(),
                            color = MaterialTheme.colorScheme.onSurface
                            //modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        },
        onDismissRequest = {
            dismissFunction()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    sortOptionSelected(selectedOption)
                    dismissFunction()
                }
            ) {
                Text("ok")
            }
        }
    )
}