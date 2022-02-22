package com.example.coroutinesservicesviewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ExampleViewModel : ViewModel() {
    override fun onCleared() {
        Log.d("VM","ViewModel Cleared")
        super.onCleared()
    }

    private val _currentNumber = MutableLiveData<Int?>(0)

    val currentNumber: LiveData<Int?>
        get() = _currentNumber


    private val _currentBoolean = MutableLiveData<Boolean?>(false)
    val currentBoolean: LiveData<Boolean?>
        get() = _currentBoolean

    fun increaseNumber() {
        _currentNumber.value= _currentNumber.value?.plus(1)
    }
    fun changeBool(){
        _currentBoolean.value = !_currentBoolean.value!!
    }
}