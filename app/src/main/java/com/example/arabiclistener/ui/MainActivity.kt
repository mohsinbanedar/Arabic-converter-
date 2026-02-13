package com.example.arabiclistener.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewModelScope
import com.example.arabiclistener.data.DetectedNote
import com.example.arabiclistener.data.NoteDatabase
import com.example.arabiclistener.data.NoteRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = NoteRepository(NoteDatabase.getInstance(applicationContext).noteDao())
        setContent {
            MaterialTheme {
                val vm: MainViewModel = viewModel(factory = MainViewModel.factory(repository))
                NotesScreen(
                    notes = vm.notes.collectAsState().value,
                    onEnableService = {
                        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                    }
                )
            }
        }
    }
}

class MainViewModel(repository: NoteRepository) : ViewModel() {
    val notes = repository.observeNotes().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    companion object {
        fun factory(repository: NoteRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return MainViewModel(repository) as T
                }
            }
    }
}

@Composable
private fun NotesScreen(notes: List<DetectedNote>, onEnableService: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Arabic Listener",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Enable Accessibility service to start live detection.",
                style = MaterialTheme.typography.bodyMedium
            )
            Button(onClick = onEnableService) {
                Text("Open Accessibility Settings")
            }

            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(notes, key = { it.id }) { note ->
                    NoteItem(note)
                }
            }
        }
    }
}

@Composable
private fun NoteItem(note: DetectedNote) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Arabic: ${note.sourceArabic}", style = MaterialTheme.typography.bodyMedium)
            Text("English: ${note.englishTranslation}", style = MaterialTheme.typography.bodySmall)
            Text("Hinglish: ${note.hinglishNote}", style = MaterialTheme.typography.bodySmall)
        }
    }
}
