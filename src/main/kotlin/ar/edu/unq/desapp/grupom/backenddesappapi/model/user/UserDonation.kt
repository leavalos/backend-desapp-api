package ar.edu.unq.desapp.grupom.backenddesappapi.model.user

import ar.edu.unq.desapp.grupom.backenddesappapi.model.Donation
import ar.edu.unq.desapp.grupom.backenddesappapi.model.Project
import ar.edu.unq.desapp.grupom.backenddesappapi.model.exceptions.DoNotHaveDonationPrivilege
import java.time.LocalDateTime

class UserDonation : User {

    var points: Double = 00.00
    var madeDonations:MutableList<Donation> = mutableListOf()

    constructor(mail:String, password:String, nickName:String): super(mail, password, nickName)


    override fun earnPoints(points: Double) {
        this.points += points
    }

    override fun donate(money: Double, comment: String, project: Project) {
        val donation = Donation(money, comment, this.nickname(), LocalDateTime.now(), project.name())
        project.receiveDonationFrom(this, donation)
    }

    override fun addDonation(donation: Donation) {
        this.madeDonations.add(donation)
    }

    override fun points() : Double {
        return this.points
    }

    override fun madeDonations() : MutableList<Donation> {
        return this.madeDonations
    }

}