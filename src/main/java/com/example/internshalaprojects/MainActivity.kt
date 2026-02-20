package com.example.internshalaprojects

import android.content.Context
import android.content.Intent

import android.net.Uri
import android.os.Bundle
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card

import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.internshalaprojects.data.Hotel
import com.example.internshalaprojects.data.ListOfHotels
import com.example.internshalaprojects.otpScreens.sendOTPScreen
import com.example.internshalaprojects.ui.theme.InternshalaProjectsTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlin.getValue


class MainActivity : ComponentActivity() {
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
fun FAQScreen(viewModel : GameViewModel) {
    val context= LocalContext.current

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

val visible=appViewModel.isVisible.collectAsState()

    if(visible.value){
        offerScreen()
    }else if (user==null){
        sendOTPScreen(context=context, appViewModel = appViewModel,onClick={navController.navigate("verifyotpscreen")}, auth = auth)
    }else{
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                TopAppBar(title = { TopAppBar(appViewModel,onClick={
                    auth.signOut()
appViewModel.clearUser()
                }) })
            },
            bottomBar = {
                BottomNavigationBar(navController)
            }) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(128.dp),
                    modifier = Modifier.padding(8.dp),

                    ) {
                    item(span = { GridItemSpan(2) }){
                        Column(Modifier.fillMaxWidth()) {
                            Image(painterResource(R.drawable.appbanner), contentDescription = null,
                                modifier=Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp))
                                    , contentScale = ContentScale.Crop

                               )
                            Card(modifier = Modifier.fillMaxWidth(),
                                colors= CardDefaults.cardColors(
                                    containerColor = androidx.compose.ui.graphics.Color(108,194,111,255)
                                )) {
Text(text="Show by category",
    modifier = Modifier.padding(horizontal = 10.dp),
    fontSize = 20.sp,
    fontWeight = FontWeight.SemiBold,
    color= Color.White
    )
                            }
                        }
                    }
                    items(ListOfHotels().size) { hotel ->
                        HotelCard(ListOfHotels()[hotel], {

                           //if (appViewModel.userAsks.value) {
                               //val intent =
                                 //  Intent(Intent.ACTION_VIEW, Uri.parse(ListOfHotels()[hotel].link))
                              // context.startActivity(intent)
                           //}else{
                              onCLick()
                           //}
                        })
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
containerColor = androidx.compose.ui.graphics.Color(248,221,248,255)
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
    Row(Modifier.fillMaxWidth().padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Text(appViewModel.screenTitle.collectAsState().value, fontSize = 30.sp)
Image(painterResource(R.drawable.logout),contentDescription = "Logout", modifier = Modifier.size(120.dp).clickable{onClick()})
    }
}