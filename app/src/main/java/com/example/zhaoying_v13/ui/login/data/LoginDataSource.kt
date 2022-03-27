package com.example.zhaoying_v13.ui.login.data

import com.example.zhaoying_v13.database.UserDatabase
import com.example.zhaoying_v13.ui.login.data.model.LoggedInUser
import java.io.IOException
import java.util.*

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    fun login(phonenumber: String, password: String): Result<LoggedInUser> {
        try {
            // TODO: handle loggedInUser authentication 发送网络请求
                //UUID改为返回的userID
                if (phonenumber=="15603781240"&&password=="123456"){
                    val testUser = LoggedInUser(UUID.randomUUID().toString(), 200)
                    return Result.Success(testUser)
                }

            //Default设置
            val fakeUser = LoggedInUser(UUID.randomUUID().toString(), 200)
            return Result.Success(fakeUser)

        } catch (e: Throwable) {
            return Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}