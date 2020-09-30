package ar.edu.unq.desapp.grupom.backenddesappapi.service.user

import ar.edu.unq.desapp.grupom.backenddesappapi.model.user.User

interface IUserService {

    fun getUsers(): List<User>

    fun addUser(user: User)

    fun putUser(userId: Long, newUser: User)

    fun deleteUser(userId: Long)

}