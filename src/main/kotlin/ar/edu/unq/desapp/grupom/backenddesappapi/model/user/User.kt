package ar.edu.unq.desapp.grupom.backenddesappapi.model.user

import ar.edu.unq.desapp.grupom.backenddesappapi.model.Donation
import ar.edu.unq.desapp.grupom.backenddesappapi.model.Location
import ar.edu.unq.desapp.grupom.backenddesappapi.model.Project
import ar.edu.unq.desapp.grupom.backenddesappapi.model.exceptions.user.DoNotHaveDonationPrivilege
import ar.edu.unq.desapp.grupom.backenddesappapi.model.exceptions.user.DoNotHaveRootPrivilege
import ar.edu.unq.desapp.grupom.backenddesappapi.model.exceptions.user.InvalidEmailException
import lombok.Generated
import java.time.LocalDate
import java.util.regex.Pattern.compile
import javax.persistence.*

@Entity
abstract class User(
        @Generated
        open var mail: String,

        @Generated
        open var password: String,

        @Generated
        open var nickName: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    open var id: Long? = null

    init {
        this.validateMail()
    }

    open fun setId(id: Long) {
        this.id = id
    }

    open fun getId(): Long {
        return this.id!!
    }

    fun isMail(email : String) : Boolean {
        return compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        ).matcher(email).matches()
    }

    fun validateMail() {
        if (!this.isMail(mail)) {
            throw InvalidEmailException(mail)
        }
    }

    open fun obtainMail() : String {
        return this.mail
    }

    open fun obtainPassword() : String {
        return this.password
    }

    open fun obtainNickName() : String {
        return this.nickName
    }

    open fun setNickname(aNickname :  String) {
        this.nickName = aNickname
    }

    open fun earnPoints(points: Double) {
        throw DoNotHaveDonationPrivilege()
    }

    open fun donate(money: Double, comment: String, project: Project) {
        throw DoNotHaveDonationPrivilege()
    }

    open fun finishProject(location: Location) {
        throw DoNotHaveRootPrivilege()
    }

    open fun addProject(location: Location, project: Project) {
        throw DoNotHaveRootPrivilege()
    }

    open fun addDonation(donation: Donation) {
        throw DoNotHaveDonationPrivilege()
    }

    open fun points() : Double {
        throw DoNotHaveDonationPrivilege()
    }

    open fun madeDonations() : MutableList<Donation> {
        throw DoNotHaveDonationPrivilege()
    }

    open fun createProject(name : String, moneyFactor: Double, beginningDate : LocalDate,
                           finishDate : LocalDate, minPercentage: Int)  : Project {
        throw DoNotHaveRootPrivilege()
    }

    open fun madeOneDonationInThisMonth(): Boolean {
        throw DoNotHaveDonationPrivilege()
    }

    open fun countDonationsMadeInThisMonth(): Int {
        throw DoNotHaveDonationPrivilege()
    }
}
