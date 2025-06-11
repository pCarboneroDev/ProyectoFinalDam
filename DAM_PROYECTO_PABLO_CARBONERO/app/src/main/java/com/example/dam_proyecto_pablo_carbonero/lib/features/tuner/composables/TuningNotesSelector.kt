package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel

@Composable
fun TuningNotesSelector(
    selectedTuning: TuningWithNotesModel?,
    onNoteSelected: (MusicNote) -> Unit,
    latinNotes: Boolean,
    selectedNote: MusicNote?
){

    Column {
        selectedTuning?.noteList?.forEachIndexed { index, note ->
            val isSelected = note == selectedNote
            val guitarString = (5 - index * 0.5).toInt()

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable { onNoteSelected(note) },
                //horizontalAlignment = Alignment.CenterHorizontally
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)//.size(60.dp) // Tamaño circular fijo
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                            shape = CircleShape
                        ),
                    //.clickable { onNoteSelected(note); i = index },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (latinNotes == true) note.latinName else note.englishName,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Box(
                    modifier = Modifier
                        //.fillMaxWidth()
                        .weight(5f)
                        .height(guitarString.dp)
                        .background(color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSecondary)
                )
            }
        }
    }
}