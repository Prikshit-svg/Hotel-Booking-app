package com.example.internshalaprojects

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.internshalaprojects.data.Internetitem
import com.example.internshalaprojects.network.HotelApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModel : ViewModel() {

    private val _screenTitle = MutableStateFlow<String>("Home")
    val screenTitle: MutableStateFlow<String> get() = _screenTitle
    fun setScreenTitle(title:String){
        _screenTitle.value=title
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
    fun clearUser(){
        _user.value=null
        _phoneNumber.value=null
        _verificationId.value=""
        _otp.value=""
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
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential,auth : FirebaseAuth,context : Context) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(context as Activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "signInWithCredential:success")

                    val user = task.result?.user
                    if (user != null) {
                        setUser(user)
                    }
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        Toast.makeText(context,"the otp you have entered is invalid. Please try again", Toast.LENGTH_SHORT).show()
                    }
                    // Update UI
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

offerScreenJob=viewModelScope.launch(Dispatchers.Default) {
    delay(2000)
    removeOffer()

}

        getHotelItems()
    }


}
