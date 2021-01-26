package com.devfutech.nusantecttest.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class HomeViewModel @ViewModelInject constructor() : ViewModel() {

    val result = MutableLiveData<Double>()

    fun operationCalculate(input: List<Double>, operation: String) {
        var caluculate = input[0]
        when (operation) {
            "+" -> for (i in 1 until input.size) {
                caluculate += input[i]
            }
            "-" -> for (i in 1 until input.size) {
                caluculate -= input[i]
            }
            "*" -> for (i in 1 until input.size) {
                caluculate *= input[i]
            }
            "/" -> for (i in 1 until input.size) {
                caluculate /= input[i]
            }
        }
        result.postValue(caluculate)
    }
}