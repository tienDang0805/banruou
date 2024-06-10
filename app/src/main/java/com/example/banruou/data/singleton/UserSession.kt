package com.example.banruou.data.singleton

import com.example.banruou.data.model.UserInfoGetMeResponse
import com.example.banruou.data.model.UserResponse

object UserSession {
    var userInfo: UserInfoGetMeResponse? = null
    var userResponse : UserResponse? = null
}

