package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTabsModal(
    saveMethod: ((String) -> Unit),
    dismissFunction: (() -> Unit),
    currentTabs: String,
    context: Context
){
    var tabs by remember { mutableStateOf(currentTabs) }
    val clipboardManager = LocalClipboardManager.current

    ModalBottomSheet(
        onDismissRequest = { dismissFunction() },
        content = {

            Box(
                modifier = Modifier
                    .padding(10.dp)
            ){
                Column(Modifier.padding(10.dp)) {

                    Row(Modifier.fillMaxWidth()) {
                        Text("Tabs", style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold))
                        Spacer(Modifier.weight(1f))
                        Button(onClick = {
                            val annotatedString = clipboardManager.getText()
                            if(isGuitarTabFormatFlexible(annotatedString.toString())){
                                tabs = annotatedString.toString()
                            }
                            else{
                                Toast.makeText(context, "El formato no es correcto", Toast.LENGTH_SHORT).show()
                            }

                        }) { Text("Paste from clipboard") }
                    }
                    TabsBox(tabs)
                }
                if(tabs.isNotEmpty())
                    Button(
                        onClick = { saveMethod(tabs) },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 8.dp) // Optional padding from bottom
                    ) {
                        Text("Save")
                    }
            }

        }
    )
}

fun isGuitarTabFormatFlexible(text: String): Boolean {
    val tabLineRegex = Regex("^[A-Ga-g#b0-9]{1,2}\\|[-\\d~hpbx/\\\\|()*\\s]+$")

    val lines = text.lines()
    val tabLines = lines.filter { tabLineRegex.matches(it.trim()) }

    return tabLines.size >= 4
}
