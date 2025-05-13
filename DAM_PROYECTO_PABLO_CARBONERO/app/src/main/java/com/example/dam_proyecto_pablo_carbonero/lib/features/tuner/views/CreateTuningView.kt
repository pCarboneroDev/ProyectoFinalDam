package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.viewmodels.CreateTuningVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CreateTuningView(navController: NavHostController, vm: CreateTuningVM = hiltViewModel()){
    val context = LocalContext.current

    val noteList by vm.noteList.collectAsState()
    val tuningName by vm.tuningName.collectAsState(initial = "")
    val selectedNotes by vm.selectedNotes.collectAsState()
    val latinNotes by vm.latinNotes.collectAsState()

    var isValid by remember {mutableStateOf(false)}


    Column(
        Modifier.fillMaxSize().padding(top = 50.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = tuningName,
            onValueChange = {
                vm.setTuningName(it)
                isValid = isFormValid(tuningName, selectedNotes)
            },
            colors = OutlinedTextFieldDefaults.colors(
                //focusedTextColor = Color.LightGray
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
                    var result = vm.saveNewTuning()
                    if (result == true){
                        navController.navigate("Tuner"){
                            popUpTo("CreateTuning") { inclusive = true }
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
            Text(text = "Save")
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
