package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import kotlin.collections.forEach


@Composable
fun TuningSelector(
    selectedTuning: TuningWithNotesModel?,
    tunings: List<TuningWithNotesModel>,
    latinNotes: Boolean,
    onTuningSelected: (TuningWithNotesModel) -> Unit,
    navController: NavHostController,
){
    var expanded by remember { mutableStateOf(false) }
    Box(
        Modifier
            .height(60.dp)
            .width(270.dp)
            .clickable(
                onClick = {expanded = !expanded}
            ),
        contentAlignment = Alignment.CenterStart,
    ){
        Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
            Column() {
                Text(selectedTuning?.tuning!!.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Row() {
                    selectedTuning?.noteList?.forEach { note ->
                        Text(text = if (latinNotes == true) note.latinName + " " else note.englishName + " ",
                            fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground)
                    }
                }
            }
            Icon(Icons.Default.ArrowDropDown, contentDescription = "More options", tint = MaterialTheme.colorScheme.primary)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            Modifier.background(color = MaterialTheme.colorScheme.secondary)
        ) {
            tunings.forEach { tuning ->
                DropdownMenuItem(
                    text = {
                        Text(tuning.tuning.name)
                    },
                    onClick = { onTuningSelected(tuning); expanded = false }
                )
            }
        }
    }

    if(selectedTuning?.tuning?.name != "Standard Tuning"){
        IconButton(onClick = {
            navController.navigate("EditTuning/${selectedTuning!!.tuning.id}")
        }) {
            Icon(Icons.Default.Create, contentDescription = "edit tunning", tint = Color.LightGray)
        }
    }

    IconButton(onClick = {
        navController.navigate("CreateTuning")
    }) {
        Icon(Icons.Default.Add, contentDescription = "add tunning", tint = Color.LightGray)
    }
}