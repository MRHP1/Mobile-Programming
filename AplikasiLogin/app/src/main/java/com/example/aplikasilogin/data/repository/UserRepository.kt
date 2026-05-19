package com.example.aplikasilogin.data.repository

import com.example.aplikasilogin.data.local.dao.UserDao
import com.example.aplikasilogin.data.local.entity.User

class UserRepository(
    private val dao: UserDao
) {
    suspend fun insert(user: User) {
        dao.insert(user)
    }

    suspend fun login(
        username: String,
        password: String
    ): User? {
        return dao.login(username, password)
    }
}
