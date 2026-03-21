package com.example.internshalaprojects



import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindRoomScreen(appViewModel : AppViewModel,
                   navController : NavController,
                   auth : FirebaseAuth,
                   onClick : () -> Unit) {
val itemUiState by appViewModel.itemUiState.collectAsState()
        FindRoomScreenContent(navController = navController,
            appViewModel,
            itemUiState,
            auth,
            onClick)


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindRoomScreenContent(
    navController: NavController,
    appViewModel : AppViewModel,
    itemUiState: AppViewModel.ItemUiState,
    auth : FirebaseAuth,
    onClick :()->Unit
) {

    val context = LocalContext.current

    val cityList = if (itemUiState is AppViewModel.ItemUiState.Success){
itemUiState.items.map { it.name }.distinct()
    }else{
        emptyList()
    }

    var selectedCity by remember { mutableStateOf("Bengaluru") }
    var expanded by remember { mutableStateOf(false) }

    var checkInDate by remember { mutableStateOf("18/12/2023") }
    var checkOutDate by remember { mutableStateOf("20/12/2023") }

    var rooms by remember { mutableIntStateOf(2) }
    var showRoomDialog by remember { mutableStateOf(false) }

    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    fun showDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                onDateSelected(formatter.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                TopAppBar(appViewModel, onClick = {
                    auth.signOut()
                    appViewModel.clearUser(onClick)
                })
            })
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAFC))
        ) {



            Spacer(modifier = Modifier.height(14.dp))

            // Main Card Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
                    .background(Color.White, shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 8.dp)
            ) {

                // Dropdown Location
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true }
                        .padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color(0xFF2196F3)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Where you want to go?",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            text = selectedCity.ifEmpty { "" },
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null
                    )
                }

                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    DropdownMenuItem(
                        text={OutlinedTextField(

                            value = selectedCity,
                            onValueChange = {selectedCity=it}
                        )},onClick = { expanded = false }
                    )
                    cityList.forEach { city ->
                        DropdownMenuItem(
                            text = { Text(city) },
                            onClick = {
                                selectedCity = city
                                expanded = false
                            }
                        )
                    }
                }

                // Checkin Date
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showDatePicker { date ->
                                checkInDate = date
                            }
                        }
                        .padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color(0xFF2196F3)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Checkin Date",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            text = checkInDate,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                // Checkout Date
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showDatePicker { date ->
                                checkOutDate = date
                            }
                        }
                        .padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = null,
                        tint = Color(0xFF2196F3)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Checkout Date",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            text = checkOutDate,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)

                // Number of Rooms
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showRoomDialog = true }
                        .padding(horizontal = 14.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = null,
                        tint = Color(0xFF2196F3)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Number of Rooms",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                        Text(
                            text = rooms.toString(),
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Button Gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
                    .height(48.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF2196F3), Color(0xFF00BCD4))
                        ),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable {
                       appViewModel.setSearchQuery(selectedCity)
                        appViewModel.searchNearbyHotels(selectedCity)
                        navController.navigate("home")
                        },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SEARCH",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }

            if (showRoomDialog) {
                RoomSelectorDialog(
                    rooms = rooms,
                    onDismiss = { showRoomDialog = false },
                    onRoomsChange = { rooms = it }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            InternetItemScreen(appViewModel,
                itemUiState
            )
        }
    }
}

@Composable
fun RoomSelectorDialog(
    rooms: Int,
    onDismiss: () -> Unit,
    onRoomsChange: (Int) -> Unit
) {
    var tempRooms by remember { mutableIntStateOf(rooms) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFDDF4F8),
        shape = RoundedCornerShape(16.dp),
        title = {
            Text(
                text = "Number of Rooms",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black
            )
        },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    if (tempRooms > 1) tempRooms--
                }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = null)
                }

                Text(
                    text = tempRooms.toString(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )

                IconButton(onClick = {
                    if (tempRooms < 10) tempRooms++
                }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onRoomsChange(tempRooms)
                onDismiss()
            }) {
                Text("OK")
            }
        }
    )
}


@Composable
fun BottomNavigationBar(navController : NavController) {

    NavigationBar(
        containerColor = Color.Transparent,
        modifier = Modifier
            .height(70.dp)
            .background(
                brush = Brush.horizontalGradient(
                    colors = listOf(Color(0xFF2196F3), Color(0xFF00BCD4))
                )
            )
    ) {

        NavigationBarItem(
            selected = true,
            onClick = {navController.navigate("home")},
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = {navController.navigate("findroomscreen")},
            icon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
            label = { Text("Where2Go") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )

        NavigationBarItem(
            selected = false,
            onClick = {navController.navigate("faqscreen")},
            icon = { Icon(Icons.Default.Info, contentDescription = null) },
            label = { Text("FAQs") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = Color.White,
                unselectedIconColor = Color.White,
                unselectedTextColor = Color.White,
                indicatorColor = Color.Transparent
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FindRoomScreenPreview() {
    val  appViewModel: AppViewModel = viewModel()
   // FindRoomScreenContent(navController = rememberNavController(),appViewModel)
    InternetItemScreen(appViewModel,
        appViewModel.itemUiState.collectAsState() as AppViewModel.ItemUiState
    )

}
