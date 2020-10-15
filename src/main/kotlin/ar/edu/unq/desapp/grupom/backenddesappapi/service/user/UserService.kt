package ar.edu.unq.desapp.grupom.backenddesappapi.service.user

import ar.edu.unq.desapp.grupom.backenddesappapi.model.exceptions.user.UserNotFoundException
import ar.edu.unq.desapp.grupom.backenddesappapi.model.user.User
import ar.edu.unq.desapp.grupom.backenddesappapi.model.user.UserDonation
import ar.edu.unq.desapp.grupom.backenddesappapi.model.user.UserValidator
import ar.edu.unq.desapp.grupom.backenddesappapi.persistence.user.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService: IUserService{

    @Autowired
    lateinit var userRepository: UserRepository

    override fun getUsers(): List<User> {
        return userRepository.findAll()
    }

    @Transactional
    override fun addUser(user: User) {
        userRepository.save(user)
    }

    override fun getUserById(userId: Long): User {
        return userRepository.findById(userId).orElseThrow { UserNotFoundException() }
    }

    override fun createUserDonation(user: UserDonation): User {
        UserValidator.validateUser(user.obtainMail(), user.obtainPassword(), user.obtainNickName())
        this.checkIfEmailAlreadyExists(user.obtainMail())
        val userDonation = UserDonation(user.obtainMail(), user.obtainPassword(), user.obtainNickName())
        this.addUser(userDonation)
        return userDonation
    }

    override fun putUser(userId: Long, newUser: User) {
        val foundUser = this.getUserById(userId)
        newUser.setId(foundUser.getId())
    }

    override fun deleteUser(userId: Long) {
        val foundUser = this.getUserById(userId)
        userRepository.delete(foundUser)
    }

    private fun checkIfEmailAlreadyExists(email: String) : Boolean {
        return this.userRepository.checkIfEmailAlreadyExists(email)
    }
}