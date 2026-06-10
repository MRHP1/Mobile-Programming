package com.example.crudsiswa.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.crudsiswa.data.Siswa
import com.example.crudsiswa.ui.theme.AccentPurple
import com.example.crudsiswa.ui.theme.AccentRed
import com.example.crudsiswa.ui.theme.AvatarBlue
import com.example.crudsiswa.ui.theme.AvatarGreen
import com.example.crudsiswa.ui.theme.AvatarIndigo
import com.example.crudsiswa.ui.theme.AvatarOrange
import com.example.crudsiswa.ui.theme.AvatarPink
import com.example.crudsiswa.ui.theme.AvatarTeal
import com.example.crudsiswa.ui.theme.DarkCard
import com.example.crudsiswa.ui.theme.TextGrey
import com.example.crudsiswa.ui.theme.TextWhite

private val avatarColors = listOf(
    AvatarBlue, AvatarGreen, AvatarOrange,
    AvatarPink, AvatarTeal, AvatarIndigo
)

private fun getInitials(nama: String): String {
    val words = nama.trim().split("\\s+".toRegex())
    return when {
        words.size >= 2 -> "${words[0].first().uppercaseChar()}${words[1].first().uppercaseChar()}"
        words.isNotEmpty() && words[0].isNotEmpty() -> "${words[0].first().uppercaseChar()}"
        else -> "?"
    }
}

@Composable
fun StudentItem(
    siswa: Siswa,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorIndex = (siswa.id % avatarColors.size)
    val avatarColor = avatarColors[colorIndex]
    val initials = getInitials(siswa.nama)

    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar circle with initials
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(avatarColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = TextWhite,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Name and email
            androidx.compose.foundation.layout.Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = siswa.nama,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = TextWhite
                )
                Text(
                    text = siswa.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextGrey
                )
            }

            // Edit & Delete buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                IconButton(onClick = onEdit) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = AccentPurple,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Hapus",
                        tint = AccentRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
