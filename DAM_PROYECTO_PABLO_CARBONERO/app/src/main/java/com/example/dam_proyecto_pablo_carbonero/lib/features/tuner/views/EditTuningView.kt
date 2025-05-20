package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.CreateHeader
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.viewmodels.CreateTuningVM
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.viewmodels.EditTuningVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch




@Composable
fun EditTuningView(navController: NavHostController, vm: EditTuningVM = hiltViewModel()){
    val context = LocalContext.current

    val noteList by vm.noteList.collectAsState()
    val tuningName by vm.tuningName.collectAsState(initial = "")
    val selectedNotes by vm.selectedNotes.collectAsState()
    val latinNotes by vm.latinNotes.collectAsState()
    var notes = ""
    notes += selectedNotes.map { note -> "${note.englishName} " }
    var isValid by remember {mutableStateOf(false)}



    Column(
        Modifier
            .fillMaxSize()
            .systemBarsPadding().padding(horizontal = 10.dp),
        //horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CreateHeader(
            title = "Edit tuning",
            saveMethod = {
                if (isValid){
                    CoroutineScope(Dispatchers.Main).launch {
                        var result = vm.updateTuning()
                        if (result == true){
                            navController.navigate("Tuner"){
                                popUpTo("EditTuning") { inclusive = true }
                            }
                        }
                        else{
                            Toast.makeText(context,"ERROR",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else{
                    Toast.makeText(context,"Complete all the fields",Toast.LENGTH_SHORT).show()
                }

            },
            navController = navController
        )


        Spacer(Modifier.height(16.dp))


        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = tuningName,
            onValueChange = {
                vm.setTuningName(it)
                isValid = isFormValid(tuningName, selectedNotes)
            },
            label = { Text("Tuning name") },
            singleLine = true
        )

        Spacer(Modifier.height(24.dp))

        for (i in 5 downTo 0){
            var expanded by remember { mutableStateOf(false) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text(text = "String ${i + 1}: ", modifier = Modifier.weight(1f))

                // Improved note selector button
                Box(
                    modifier = Modifier
                        .weight(2f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .clickable { expanded = true }
                        .padding(12.dp)
                ) {
                    Text(
                        text = when {
                            selectedNotes[i].englishName == "0" -> "Select note"
                            latinNotes -> selectedNotes[i].latinName
                            else -> selectedNotes[i].englishName
                        },
                        color = if (selectedNotes[i].englishName == "0") Color.Gray else Color.Black
                    )
                }

                //DropDown para las notas
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    //Modifier.background(color = MaterialTheme.colorScheme.secondary)
                ) {
                    noteList.forEach { note ->
                        DropdownMenuItem(
                            text = {
                                Text(if (latinNotes == true) note.latinName else note.englishName)
                            },
                            onClick = {
                                selectedNotes[i] = note
                                isValid = isFormValid(tuningName, selectedNotes)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Text(
            text = "Tuning: " + selectedNotes.reversed().joinToString(" - ") {
                if (it.englishName == "0") "?" else if (latinNotes) it.latinName else it.englishName
            },
            modifier = Modifier.padding(top = 16.dp),
            color = if (selectedNotes.any { it.englishName == "0" }) Color.Red else MaterialTheme.colorScheme.onSurface
        )

        Button(
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    vm.borrarAfinacion()
                }
                navController.navigate("Tuner"){
                    popUpTo("EditTuning") { inclusive = true }
                }
            }
        ) {
            Text("Delete")
        }
    }
}

private fun isFormValid(tuningName: String, selectedNotes: Array<MusicNote>?): Boolean{
    var isValid = true;

    if (tuningName.isEmpty()){
        isValid = false
    }

    for(note in selectedNotes!!){
        if(note.englishName == "0"){
            isValid = false;
        }
    }
    return isValid;
}




/*
@Composable
fun EditTuningView(navController: NavHostController, vm: EditTuningVM = hiltViewModel()){
    val context = LocalContext.current
    val noteList by vm.noteList.collectAsState()
    val tuningName by vm.tuningName.collectAsState(initial = "")
    val selectedNotes by vm.selectedNotes.collectAsState()
    val latinNotes by vm.latinNotes.collectAsState()

    var isValid by remember {mutableStateOf(true)}


    Column(
        Modifier.fillMaxSize().padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(onClick = {
            CoroutineScope(Dispatchers.Main).launch {
                vm.borrarAfinacion()
            }
            navController.navigate("Tuner"){
                popUpTo("Tuner") { inclusive = true }
            }
        }) {
            Icon(Icons.Default.Delete, contentDescription = "delete tunning", tint = Color.Red)
        }

        OutlinedTextField(
            value = tuningName,
            onValueChange = {
                vm.setTuningName(it)
                isValid = isFormValid(tuningName, selectedNotes)
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                unfocusedTextColor = MaterialTheme.colorScheme.onBackground,
            )
        )

        for (i in 0..5){
            var expanded by remember { mutableStateOf(false) }
            Row {
                Text(text = "Cuerda ${i+1}: ")

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .background(
                            color = Color.LightGray,
                            shape = CircleShape
                        )
                        .clickable() { expanded = true }.padding(15.dp)
                ) {
                    Text(
                        text = if (selectedNotes!![i].englishName == "0"){
                            ""
                        }
                        else{
                            if (latinNotes == true) (selectedNotes!![i].latinName)
                            else (selectedNotes!![i].englishName)
                        },
                        //color = MaterialTheme.colorScheme.onSecondary
                    )
                }

                //DropDown para las notas
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    Modifier.background(color = MaterialTheme.colorScheme.secondary)
                ) {
                    noteList?.forEach { note ->
                        DropdownMenuItem(
                            text = {
                                Text(if (latinNotes == true) note.latinName else note.englishName)
                            },
                            onClick = {
                                selectedNotes!![i] = note
                                isValid = isFormValid(tuningName, selectedNotes)
                            }
                        )
                    }
                }
            }
        }

        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    var result = vm.updateTuning()
                    if (result == true){
                        navController.navigate("Tuner"){
                            popUpTo("Tuner") { inclusive = true }
                        }
                    }
                    else{
                        Toast.makeText(context,"ERROR",Toast.LENGTH_SHORT).show()
                    }
                }
            },
            enabled = isValid
        )
        {
            Text(text = "Guardar")
        }

    }
}

private fun isFormValid(tuningName: String, selectedNote: Array<MusicNote>?): Boolean{
    var isValid = true;

    if (tuningName.isEmpty()){
        isValid = false
    }

    for(note in selectedNote!!){
        if(note.englishName == "0"){
            isValid = false;
        }
    }
    return isValid;
}*/