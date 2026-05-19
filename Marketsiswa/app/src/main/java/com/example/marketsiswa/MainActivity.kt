package com.example.marketsiswa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
// 1. Data Model
data class Product(
val id: Long = System.currentTimeMillis(),
val name: String,
val price: String,
val description: String
)
class MainActivity : ComponentActivity() {
override fun onCreate(savedInstanceState: Bundle?) {
super.onCreate(savedInstanceState)
setContent {
MarketplaceTheme {
MainScreen()
}
}
}
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
// State Management
var currentScreen by remember { mutableStateOf("home") }
val productList = remember { mutableStateListOf<Product>() }
val snackbarHostState = remember { SnackbarHostState() }
val scope = rememberCoroutineScope()
// Inisialisasi data awal
LaunchedEffect(Unit) {
if (productList.isEmpty()) {
productList.add(Product(name = "Brownies Lumer", price = "15000", description = "Cokelat melimpah."))
productList.add(Product(name = "Kaos Custom", price = "85000", description = "Bahan adem."))
}
}
Scaffold(
snackbarHost = { SnackbarHost(snackbarHostState) },
topBar = {
CenterAlignedTopAppBar(
title = {
Text(
if (currentScreen == "add") "Tambah Produk" else "MarketSiswa",
fontWeight = FontWeight.Bold
)
},
navigationIcon = {
if (currentScreen == "add") {
IconButton(onClick = { currentScreen = "home" }) {
Icon(Icons.Default.ArrowBack, contentDescription = "Back")
}
}
}
)
},
bottomBar = {
NavigationBar {
NavigationBarItem(
selected = currentScreen == "home",
onClick = { currentScreen = "home" },
label = { Text("Home") },
icon = { Icon(Icons.Default.Home, contentDescription = null) }
)
NavigationBarItem(
selected = currentScreen == "profile",
onClick = { currentScreen = "profile" },
label = { Text("Profile") },
icon = { Icon(Icons.Default.Person, contentDescription = null) }
)
}
},
floatingActionButton = {
if (currentScreen == "home") {
ExtendedFloatingActionButton(
onClick = { currentScreen = "add" },
containerColor = MaterialTheme.colorScheme.primaryContainer,
contentColor = MaterialTheme.colorScheme.onPrimaryContainer
) {
Icon(Icons.Default.Add, contentDescription = null)
Spacer(Modifier.width(8.dp))
Text("Jual")
}
}
}
) { innerPadding ->
Box(modifier = Modifier.padding(innerPadding)) {
when (currentScreen) {
"home" -> HomeScreen(productList)
"add" -> AddProductScreen(
onProductAdded = { newProduct ->
productList.add(0, newProduct)
scope.launch {
currentScreen = "home"
snackbarHostState.showSnackbar("Produk berhasil ditambahkan!")
}
}
)
"profile" -> ProfileScreen()
}
}
}
}
// --- UI COMPONENTS ---
@Composable
fun HomeScreen(products: List<Product>) {
LazyColumn(
modifier = Modifier.fillMaxSize(),
contentPadding = PaddingValues(16.dp),
verticalArrangement = Arrangement.spacedBy(12.dp)
) {
item {
Text("Halo, Siswa!", fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
Text("Mau belanja apa hari ini?", color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
}
items(products) { product ->
Card(
modifier = Modifier.fillMaxWidth(),
shape = RoundedCornerShape(16.dp)
) {
Column(modifier = Modifier.padding(16.dp)) {
Row(
modifier = Modifier.fillMaxWidth(),
horizontalArrangement = Arrangement.SpaceBetween
) {
Text(product.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
Text("Rp ${product.price}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Black)
}
Spacer(Modifier.height(4.dp))
Text(product.description, color = Color.DarkGray, fontSize = 14.sp)
}
}
}
}
}
@Composable
fun AddProductScreen(onProductAdded: (Product) -> Unit) {
var name by remember { mutableStateOf("") }
var price by remember { mutableStateOf("") }
var desc by remember { mutableStateOf("") }
var isLoading by remember { mutableStateOf(false) }
val scope = rememberCoroutineScope()
Column(
modifier = Modifier
.fillMaxSize()
.padding(24.dp),
verticalArrangement = Arrangement.spacedBy(16.dp)
) {
OutlinedTextField(
value = name,
onValueChange = { name = it },
label = { Text("Nama Produk") },
modifier = Modifier.fillMaxWidth(),
shape = RoundedCornerShape(12.dp)
)
OutlinedTextField(
value = price,
onValueChange = { price = it },
label = { Text("Harga") },
keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
modifier = Modifier.fillMaxWidth(),
shape = RoundedCornerShape(12.dp)
)
OutlinedTextField(
value = desc,
onValueChange = { desc = it },
label = { Text("Deskripsi") },
modifier = Modifier.fillMaxWidth(),
minLines = 3,
shape = RoundedCornerShape(12.dp)
)
Button(
onClick = {
isLoading = true
scope.launch {
delay(1000) // Simulasi loading (FR-04)
onProductAdded(Product(name = name, price = price, description = desc))
isLoading = false
}
},
modifier = Modifier.fillMaxWidth().height(56.dp),
enabled = name.isNotBlank() && price.isNotBlank() && !isLoading,
shape = RoundedCornerShape(12.dp)
) {
if (isLoading) {
CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
} else {
Text("Simpan Produk", fontWeight = FontWeight.Bold)
}
}
}
}
@Composable
fun ProfileScreen() {
Column(
modifier = Modifier.fillMaxSize(),
horizontalAlignment = Alignment.CenterHorizontally,
verticalArrangement = Arrangement.Center
) {
Icon(Icons.Default.AccountCircle, contentDescription = null, modifier = Modifier.size(100.dp), tint = Color.LightGray)
Text("John Siswa", fontSize = 20.sp, fontWeight = FontWeight.Bold)
Text("XII RPL 1", color = Color.Gray)
}
}
@Composable
fun MarketplaceTheme(content: @Composable () -> Unit) {
MaterialTheme(
colorScheme = lightColorScheme(
primary = Color(0xFF6200EE),
secondary = Color(0xFF03DAC6)
),
content = content
)
}