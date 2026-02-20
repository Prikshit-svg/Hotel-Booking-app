package com.example.internshalaprojects

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class GameViewModel(): ViewModel() {
    private val _userInput = mutableStateOf<String?>(null)
    val userInput: MutableState<String?> = _userInput
var count=0
    fun getComputerChoice(): String {
        val computerInput = mutableStateOf<String>(listOf("Rock","Paper","Scissor").random())
        return computerInput.value

    }


fun result() : String{
    val result=mutableStateOf<String>(if ((getComputerChoice()  ==userInput.value) || (getComputerChoice() == "Scissor" && userInput.value == "Paper") || (getComputerChoice() == "Paper" &&userInput.value == "Rock")) "Computer Won"

    else{
        "User Won"
    })
    return result.value

}


}