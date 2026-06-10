package com.example.crudsiswa.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.crudsiswa.data.Siswa
import com.example.crudsiswa.data.SiswaDao
import com.example.crudsiswa.ui.theme.AccentPurple
import com.example.crudsiswa.ui.theme.DarkCard
import com.example.crudsiswa.ui.theme.DarkSurface
import com.example.crudsiswa.ui.theme.InputBorder
import com.example.crudsiswa.ui.theme.TextGrey
import com.example.crudsiswa.ui.theme.TextWhite
import kotlinx.coroutines.launch

@Composable
fun MainScreen(dao: SiswaDao) {
    val siswaList by dao.getAllSiswa().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()

    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    // Edit mode state
    var editingSiswa by remember { mutableStateOf<Siswa?>(null) }
    val isEditing = editingSiswa != null

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = AccentPurple,
        unfocusedBorderColor = InputBorder,
        focusedLabelColor = AccentPurple,
        unfocusedLabelColor = TextGrey,
        cursorColor = AccentPurple,
        focusedTextColor = TextWhite,
        unfocusedTextColor = TextWhite,
        focusedContainerColor = DarkCard,
        unfocusedContainerColor = DarkCard
    )

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Header
            item {
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = "Registrasi Siswa",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = "Kelola data siswa",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGrey
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Form Fields
            item {
                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text("Nama") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = textFieldColors,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = textFieldColors,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Add / Update button
            item {
                Button(
                    onClick = {
                        if (nama.isNotBlank() && email.isNotBlank()) {
                            scope.launch {
                                if (isEditing) {
                                    editingSiswa?.let { current ->
                                        dao.updateSiswa(
                                            current.copy(nama = nama, email = email)
                                        )
                                    }
                                    editingSiswa = null
                                } else {
                                    dao.insertSiswa(Siswa(nama = nama, email = email))
                                }
                                nama = ""
                                email = ""
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentPurple
                    ),
                    enabled = nama.isNotBlank() && email.isNotBlank()
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(
                        text = if (isEditing) "Update Siswa" else "Tambah Siswa",
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Spacer(modifier = Modifier.height(28.dp))
            }

            // Section title
            item {
                Text(
                    text = "Daftar Siswa",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Student list
            if (siswaList.isEmpty()) {
                item {
                    Text(
                        text = "Belum ada data siswa.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextGrey,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            } else {
                items(siswaList, key = { it.id }) { siswa ->
                    StudentItem(
                        siswa = siswa,
                        onEdit = {
                            editingSiswa = siswa
                            nama = siswa.nama
                            email = siswa.email
                        },
                        onDelete = {
                            scope.launch {
                                dao.deleteSiswa(siswa)
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
