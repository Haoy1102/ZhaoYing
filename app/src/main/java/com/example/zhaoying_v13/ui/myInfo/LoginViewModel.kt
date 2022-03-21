package com.example.zhaoying_v13.ui.myInfo

import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {
    private var phoneNumber:String
        get() {
            return phoneNumber
        }
        set(value) { phoneNumber =value}

    private var password:String
        get() {
            return password
        }
        set(value) { password =value}
}