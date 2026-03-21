package com.example.internshalaprojects

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internshalaprojects.data.Internetitem

import com.example.internshalaprojects.network.HotelApi
import com.example.internshalaprojects.network.OpenTripMapApi
import com.example.internshalaprojects.network.OtmProperties
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.internshalaprojects.BuildConfig

class AppViewModel : ViewModel() {

    // Write a message to the database
    val database = Firebase.database
    val myRef = database.getReference()

    fun addToDatabase(item: Internetitem){
        //myRef.push().setValue(item) to be used when data is to be added to the database throigh the app
    }
    private val _screenTitle = MutableStateFlow<String>("Home")
    val screenTitle: MutableStateFlow<String> get() = _screenTitle
    fun setScreenTitle(title:String){
        _screenTitle.value=title
    }
    // In AppViewModel.kt

    fun fillPlaceItems() {
        // 1. SET THE STATE TO LOADING BEFORE you attach the listener.
        _itemUiState.value = ItemUiState.Loading

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val databaseItem = mutableListOf<Internetitem>()
                for (childSnapshot in dataSnapshot.children) {
                    val item = childSnapshot.getValue(Internetitem::class.java)
                    item?.let {
                        databaseItem.add(it)
                    }
                }
                // 2. ON SUCCESS: Update the state with the list of items.
                _itemUiState.value = ItemUiState.Success(databaseItem)
            }

            override fun onCancelled(error: DatabaseError) {
                // 3. ON FAILURE: Log the error and update the state to Error.
                Log.w("AppViewModel", "Failed to read Firebase value.", error.toException())
                _itemUiState.value = ItemUiState.Error
            }
        })
    }

    private val _isVisible=MutableStateFlow(true)
    val isVisible: StateFlow<Boolean> =_isVisible.asStateFlow()
    private val _itemUiState = MutableStateFlow<ItemUiState>(ItemUiState.Loading)
    val itemUiState: StateFlow<ItemUiState> = _itemUiState.asStateFlow()

    // For userAsks
    private val _userAsks = MutableStateFlow(false)
    val userAsks: StateFlow<Boolean> = _userAsks.asStateFlow()

    // For timer
    private val _timer = MutableStateFlow(60)
    val timer: StateFlow<Int> = _timer.asStateFlow()

    //val Context.dataStore : DataStore<Preferences> by preferencesDataStore("user_preferences")


private val _user=MutableStateFlow<FirebaseUser?>(null)
    val user: StateFlow<FirebaseUser?> get() =_user
private val _logout= MutableStateFlow<Boolean>(false)
    val logout: StateFlow<Boolean> get() =_logout
    fun setLogout(value: Boolean) {
        _logout.value = value
    }
    private val _phoneNumber=MutableStateFlow<String?>(null)
    val phoneNumber: StateFlow<String?> get() =_phoneNumber
private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading : StateFlow<Boolean> get() = _isLoading

    fun setIsLoading(value: Boolean) {
        _isLoading.value = value
    }

    private val _verificationId = MutableStateFlow<String>("")
    val verificationId: StateFlow<String> get() =_verificationId.asStateFlow()

    fun setVerificationId(verificationId: String) {
        _verificationId.value = verificationId
    }

private val _otp=MutableStateFlow<String>("")
    val otp: StateFlow<String> get() =_otp

    fun saveOtp(otp: String) {
        _otp.value = otp
    }


    fun savePhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }


    fun redirectToOfficialWebsite(){
        _userAsks.value=true
    }
    fun removeOffer(){
        _isVisible.value=false
    }
    lateinit var offerScreenJob: Job
    lateinit var internetJob: Job
    sealed interface ItemUiState{
        data class Success(val items: List<Internetitem>):ItemUiState
        object Error:ItemUiState
        object Loading:ItemUiState
    }
    fun setUser(user: FirebaseUser){
        _user.value=user
    }
    fun clearUser(onClick: () -> Unit){
        _user.value=null
        _phoneNumber.value=null
        _verificationId.value=""
        _otp.value=""
        onClick()
    }
    fun runTimer(){
        viewModelScope.launch {
            _timer.value = 60 // Reset the timer by updating its .value
            while (_timer.value > 0) {
                delay(1000)
                _timer.value-- // Decrement the .value
            }
        }
    }
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential, auth: FirebaseAuth, context: Context) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->  // ✅ removed context as Activity
                if (task.isSuccessful) {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "signInWithCredential:success")
                    val user = task.result?.user
                    if (user != null) {
                        setUser(user)
                    }
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(
                            context,
                            "The OTP you entered is invalid. Please try again.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
    }


    // In AppViewModel.kt
    fun getHotelItems() {
         viewModelScope.launch {
            // It's good practice to set a Loading state first
            _itemUiState.value = ItemUiState.Loading
            try {
                // Now, the network call is safely wrapped
                val listResult = HotelApi.retrofitservice.getItems()
                _itemUiState.value = ItemUiState.Success(listResult)

            } catch (e: Exception) {
                // If the network call fails for ANY reason, this block will be executed
                // Log the error to see it in Logcat during debugging
                Log.e("AppViewModel", "Failed to fetch hotel items: ${e.message}")
                _itemUiState.value = ItemUiState.Error
            }
        }
    }


    init {
        // Force reCAPTCHA flow for testing (Bypasses SafetyNet/Play Integrity)
        //FirebaseAuth.getInstance().getFirebaseAuthSettings().forceRecaptchaFlowForTesting(true)


        offerScreenJob=viewModelScope.launch(Dispatchers.Default) {
    delay(2000)
    removeOffer()

}

        fillPlaceItems()
    }
    private val _authResult= MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>>get()  =_authResult

    private val _email = mutableStateOf("")
    private val _password = mutableStateOf("")
    val password:String get() = _password.value
    val email: String get() = _email.value
    fun signUp(auth:FirebaseAuth,context: Context,enteredEmail:String,enteredPass:String){

            auth.createUserWithEmailAndPassword(enteredEmail, enteredPass)
                .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
if (user!=null){
    setUser(user)
}
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }




    }



    fun logIn(auth:FirebaseAuth,context: Context,enteredEmail:String,enteredPass:String){

        auth.signInWithEmailAndPassword(enteredEmail, enteredPass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    if (user!=null){
                        setUser(user)
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()

                }
            }
    }

    fun signInWithGoogle(
        context: Context,
        auth: FirebaseAuth,
        credentialManager: CredentialManager
    ) {
        viewModelScope.launch {
            try {
                val googleIdOptions= GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(true)
                    .setServerClientId("448526694612-ckjg5c6ravbvogem1ufm0qtghib6oo2n.apps.googleusercontent.com")
                    .setAutoSelectEnabled(true)
                    .build()
                val request= GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOptions)
                    .build()
                val result=credentialManager.getCredential(
                    context,
                    request
                )
                handleGoogleSignIn(result, auth)

            }catch (e: NoCredentialException){//NoCredentialException is thrown when no matching credential is found on the device for your request.
                try {
                val googleIdOptions= GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("448526694612-ckjg5c6ravbvogem1ufm0qtghib6oo2n.apps.googleusercontent.com")
                    .build()
                val request= GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOptions)
                    .build()
                val result=credentialManager.getCredential(
                    context,
                    request
                )
                handleGoogleSignIn(result, auth)
                }catch(e: GetCredentialException){
                    Log.e(TAG, "Google Sign-In failed: ${e.message}")
                }


            }catch(e: GetCredentialException) {
                Log.e(TAG, "Google Sign-In failed: ${e.message}")
            }
            }

        }


    /*above function works in two attempts:
First attempt — checks if the user has previously signed in to your app with Google before (setFilterByAuthorizedAccounts(true)). If yes, it can even sign them in automatically without showing any popup. This is the ideal case for returning users.
Second attempt (fallback) — if no previously authorized account is found, it shows all Google accounts on the device (setFilterByAuthorizedAccounts(false)) so the user can pick one. This is for new users signing in for the first time.
If both fail, it logs the error.*/

    private fun handleGoogleSignIn(result: GetCredentialResponse, auth : FirebaseAuth){
        val credential = result.credential
        when(credential){
            is CustomCredential->{
                if (credential.type== GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                    try {
                        val googleIdTokenCredential= GoogleIdTokenCredential.createFrom(credential.data)
                        val firebaseCredential= GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken,null)
                        auth.signInWithCredential(firebaseCredential)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    val user = auth.currentUser
                                    if (user != null) setUser(user)
                                    Log.d(TAG, "Google Sign-In: success")
                                } else {
                                    Log.w(TAG, "Google Sign-In: failure", task.exception)
                                }
                            }
                    }catch (e: GoogleIdTokenParsingException){
                        Log.e(TAG, "Invalid Google ID token", e)

                    }
                }
            }
            else -> Log.e(TAG, "Unexpected credential type")
        }
    }/*
    This is the private helper that processes whatever credential Google returns.
It does 3 things:
Checks the credential type — makes sure it's actually a Google ID token and not some other credential type
Extracts the ID token — gets the actual Google ID token from the credential
Signs into Firebase — converts the Google token into a Firebase credential and calls auth.signInWithCredential(), which is the same Firebase sign-in you already use for phone auth. On success it calls setUser() to save the user in your ViewModel state.
    */



    private val _hotelSearchState=MutableStateFlow<HotelSearchState>(HotelSearchState.Idle)
    val hotelsSearchState:StateFlow<HotelSearchState> =_hotelSearchState.asStateFlow()

    private val _hotels = MutableStateFlow<List<OtmProperties>>(emptyList())
    val hotels: StateFlow<List<OtmProperties>> = _hotels.asStateFlow()

    // Replace HotelSearchState Success:
    sealed interface HotelSearchState {
        object Idle : HotelSearchState
        object Loading : HotelSearchState
        object Empty : HotelSearchState
        data class Success(val hotels: List<OtmProperties>) : HotelSearchState
        data class Error(val message: String) : HotelSearchState
    }

    // Replace searchNearbyHotels function:
    fun searchNearbyHotels(city: String) {
        if (city.isBlank()) return

        viewModelScope.launch {
            _hotelSearchState.value = HotelSearchState.Loading
            try {
                // Step 1 — get coordinates
                val geoResult = OpenTripMapApi.service.getCityCoordinates(
                    cityName = city,
                    apiKey = BuildConfig.OPEN_TRIP_MAP_API_KEY
                )

                // Step 2 — search hotels near those coordinates
                val hotelResult = OpenTripMapApi.service.searchNearbyHotels(
                    lat = geoResult.lat,
                    lon = geoResult.lon,
                    apiKey = BuildConfig.OPEN_TRIP_MAP_API_KEY
                )

                val hotels = hotelResult.features.map { it.properties }

                if (hotels.isEmpty()) {
                    _hotelSearchState.value = HotelSearchState.Empty
                } else {
                    _hotels.value = hotels
                    _hotelSearchState.value = HotelSearchState.Success(hotels)
                }

            } catch (e: Exception) {
                _hotelSearchState.value = HotelSearchState.Error(
                    e.message ?: "Unknown error"
                )
                Log.e(TAG, "Hotel search failed: ${e.message}")
            }
        }
    }
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

}
