package com.example.internshalaprojects

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import com.google.android.gms.auth.api.identity.Identity
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.internshalaprojects.data.Hotel
import com.example.internshalaprojects.data.ListOfHotels

import com.example.internshalaprojects.network.OtmProperties
import com.example.internshalaprojects.otpScreens.sendOTPScreen
import com.example.internshalaprojects.otpScreens.sendOTPScreen
import com.example.internshalaprojects.ui.theme.InternshalaProjectsTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class MainActivity : ComponentActivity() {
    /*  private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
             context = applicationContext,
             oneTapClient = Identity.getSignInClient(applicationContext)
         )
     }*/

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appViewModel: AppViewModel by viewModels()



        setContent {

            InternshalaProjectsTheme {

NavHostController(appViewModel)
            }
        }
    }
}

@Composable
fun FAQScreen() {
    LocalContext.current

    }
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    appViewModel: AppViewModel,
    onCLick : () -> Unit,
navController : NavController,

    auth : FirebaseAuth,
    user : FirebaseUser?,
    context: Context
) {
    val hotelSearchState by appViewModel.hotelsSearchState.collectAsState()
    val visible = appViewModel.isVisible.collectAsState()
    val searchQuery by appViewModel.searchQuery.collectAsState()


    if (visible.value) {
        offerScreen()
    } else if (user == null) {
        sendOTPScreen(
            context = context,
            appViewModel = appViewModel,
            onClick = { navController.navigate("verifyotpscreen") },
            auth = auth
        )
    } else {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = {
                    TopAppBar(appViewModel, onClick = {
                        auth.signOut()
                        appViewModel.clearUser(onCLick)
                    })
                })
            },
            bottomBar = {
                BottomNavigationBar(navController)
            }) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(128.dp),
                    modifier = Modifier.padding(8.dp),

                    ) {

                    item(span = { GridItemSpan(2) }) {
                        Column(Modifier.fillMaxWidth()) {
                            Image(
                                painterResource(R.drawable.appbanner),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop

                            )
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 3.dp, bottom = 3.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(108, 194, 111, 255)
                                )
                            ) {
                                Text(
                                    text = when (hotelSearchState) {
                                        is AppViewModel.HotelSearchState.Success ->
                                            "Nearby Hotels in \"$searchQuery\""

                                        is AppViewModel.HotelSearchState.Loading ->
                                            "Searching..."

                                        else -> "Show by category"
                                    },
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    when (val state = hotelSearchState) {
                        is AppViewModel.HotelSearchState.Idle -> {

                            items(ListOfHotels().size) { hotel ->
                                HotelCard(ListOfHotels()[hotel], {
                                    val intent =
                                        Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse(ListOfHotels()[hotel].link)
                                        )
                                    context.startActivity(intent)

                                })
                            }
                        }

                        is AppViewModel.HotelSearchState.Success -> {
                            item(span = { GridItemSpan(2) }) {
                                Text(
                                    "${state.hotels.size} hotels found in \"$searchQuery\"",
                                    color = Color.Gray,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
                                )
                            }
                            items(state.hotels.size) { hotel ->
                                FoursquareHotelCard(state.hotels[hotel])
                            }
                        }
                        is AppViewModel.HotelSearchState.Loading -> {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        CircularProgressIndicator()
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            "Searching hotels in \"$searchQuery\"...",
                                            color = Color.Gray,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }

                       is AppViewModel.HotelSearchState.Empty -> {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        "No hotels found in \"$searchQuery\".\nTry a different city name.",
                                        color = Color.Gray,
                                        fontSize = 14.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                        is AppViewModel.HotelSearchState.Error -> {
                            item(span = { GridItemSpan(2) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            "Something went wrong",
                                            color = Color.Red,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            state.message,
                                            color = Color.Gray,
                                            fontSize = 12.sp,
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }

                    }
                }
            }

        }

    }
}

@Composable
fun HotelCard(hotel : Hotel,onCLick :()->Unit){
    Card(onClick = {
          onCLick()
    },modifier=Modifier.padding(4.dp),
        colors= CardDefaults.cardColors(
containerColor = Color(248,221,248,255)
        )) {
        Column(modifier = Modifier.fillMaxSize()) {
            Image(painterResource(hotel.image), contentDescription = hotel.name,
                modifier = Modifier
                    .fillMaxWidth()

                    .clip(RoundedCornerShape(8.dp))

                )
            Row(
                Modifier

                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(hotel.name, style = MaterialTheme.typography.bodyLarge)

                Text("★"+hotel.rating, style = MaterialTheme.typography.labelSmall)
            }
            Text("₹${hotel.priceRange}-₹${hotel.priceRange+5000}/night", style = MaterialTheme.typography.bodySmall)

        }
    }
}

@Composable
fun offerScreen(){
    Column(
        Modifier
            .fillMaxSize()
            .background(color = Color.Blue),
        verticalArrangement = Arrangement.Center,
    ) {
        Image(painterResource(R.drawable.modernmegasale), contentDescription = "Modern Mega Sale on all hotel bookings upto 70%",
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth()
                .aspectRatio(7f / 9f)
                .clip(shape = RoundedCornerShape(25)))
    }
}

@Preview(showBackground = true)
@Composable
fun FAQScreenPreview() {
    TopAppBar(appViewModel = viewModel(),{} )
}
@Composable
fun TopAppBar(appViewModel : AppViewModel,onClick:()->Unit= {}){
    Row(Modifier
        .fillMaxWidth()
        .padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(appViewModel.screenTitle.collectAsState().value, fontSize = 30.sp)
Image(painterResource(R.drawable.logout),contentDescription = "Logout", modifier = Modifier
    .size(120.dp)
    .clickable { onClick() })
    }
}


@Composable
fun FoursquareHotelCard(hotel: OtmProperties) {
   val context=LocalContext.current
    Card(
        onClick = {
            val searchQuery=Uri.encode("${hotel.name} hotel official website")
            val intent= Intent(Intent.ACTION_VIEW,Uri.parse("https://www.google.com/search?q=$searchQuery"))
        context.startActivity(intent)
                  },
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(248, 221, 248, 255)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Placeholder image area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        Color(0xFFCE9BCE),
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = null,
                    tint = Color(0xFF7A3D78),
                    modifier = Modifier.size(36.dp)
                )
            }

            Column(modifier = Modifier.padding(8.dp)) {

                // Hotel name
                Text(
                    text = hotel.name.ifEmpty { "Unnamed Hotel" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = Color(0xFF1565C0),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                // Category/kind
                hotel.kinds?.split(",")?.firstOrNull()?.let { kind ->
                    Text(
                        text = kind.replace("_", " ")
                            .replaceFirstChar { it.uppercase() },
                        fontSize = 11.sp,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Star rating
                hotel.rate?.let { rate ->
                    Text(
                        text = "★".repeat(rate) + "☆".repeat(3 - rate),
                        fontSize = 12.sp,
                        color = Color(0xFFFFA000)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                // Distance
                hotel.dist?.let { dist ->
                    Text(
                        text = if (dist < 1000) "${dist.toInt()}m away"
                        else "${"%.1f".format(dist / 1000.0)}km away",
                        fontSize = 10.sp,
                        color = Color(0xFF2196F3),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
