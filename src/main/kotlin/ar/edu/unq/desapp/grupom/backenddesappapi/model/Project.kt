package ar.edu.unq.desapp.grupom.backenddesappapi.model

import ar.edu.unq.desapp.grupom.backenddesappapi.model.exceptions.*
import java.lang.RuntimeException
import java.time.LocalDate

class Project {

    var name: String
    var donations: MutableList<Donation>
    var moneyFactor: Double
    private var beginningDate: LocalDate
    private var finishDate: LocalDate
    private var isFinished: Boolean
    private var location: Location
    var minPercentageToFinish: Int

    constructor(name: String, beginningDate: LocalDate, finishDate: LocalDate, location: Location) {
        this.verifyName(name)
        this.name = name
        this.donations = mutableListOf()
        this.moneyFactor = 1000.0
        this.beginningDate = beginningDate
        this.finishDate = finishDate
        this.isFinished = false
        this.location = location
        this.minPercentageToFinish = 100
    }

    constructor(
            name: String,
            moneyFactor: Double,
            beginningDate: LocalDate,
            finishDate: LocalDate,
            location: Location,
            minPercentage: Int,
            donations: MutableList<Donation>) {
        this.verifyParameters(name, moneyFactor, minPercentage, beginningDate, finishDate)
        this.name = name
        this.donations = mutableListOf()
        this.moneyFactor = moneyFactor
        this.beginningDate = beginningDate
        this.finishDate = finishDate
        this.isFinished = false
        this.location = location
        this.minPercentageToFinish = minPercentage
        this.donations = donations
    }

    private fun verifyParameters(
            name: String,
            moneyFactor: Double,
            minPercentage: Int,
            beginningDate: LocalDate,
            finishDate: LocalDate)
    {
        verifyName(name)
        verifyMoneyFactor(moneyFactor)
        verifyMinPercentage(minPercentage)
        verifyDates(beginningDate, finishDate)
    }

    private fun verifyDates(beginningDate: LocalDate, finishDate: LocalDate) {
        if (beginningDate.isAfter(finishDate)) {
            throw AProjectCannotHaveABeginningDateAfterFinishDateException()
        }
    }

    private fun verifyMinPercentage(minPercentage: Int) {
        verifyMinPercentageBiggerThanFiftyPercent(minPercentage)
        verifyMinPercentageLesserThanOneHundredPercent(minPercentage)
    }

    private fun verifyMinPercentageLesserThanOneHundredPercent(minPercentage: Int) {
        if (minPercentage > 100) {
            throw AProjectCannotHaveAMinimumPercentageToFinishBiggerThanOneHundredPercent()
        }
    }

    private fun verifyMinPercentageBiggerThanFiftyPercent(minPercentage: Int) {
        if (minPercentage < 50) {
            throw AProjectCannotHaveAMinimumPercentageToFinishLesserThanFiftyPercent()
        }
    }

    private fun verifyName(name: String) {
        if (name.isBlank()) {
            throw AProjectCannotHaveEmptyNameException()
        }
    }

    private fun verifyMoneyFactor(moneyFactor: Double) {
        verifyMoneyFactorBiggerThanZero(moneyFactor)
        verifyMoneyFactorLesserThanOneHundredThousand(moneyFactor)
    }

    private fun verifyMoneyFactorLesserThanOneHundredThousand(moneyFactor: Double) {
        if (moneyFactor > 100000.0f) {
            throw AProjectCannotHaveMoneyFactorBiggerThanOneHundredThousand()
        }
    }

    private fun verifyMoneyFactorBiggerThanZero(moneyFactor: Double) {
        if (moneyFactor < 0.0f) {
            throw AProjectCannotHaveMoneyFactorLesserThanZero()
        }
    }


    fun receiveDonationFrom(user: User, donation: Donation) {
        this.donations.add(donation)
        user.earnPoints(this.pointsEarnedWithDonation(donation))
        user.addDonation(donation)
    }

    private fun pointsEarnedWithDonation(donation: Donation): Double {
        //Needs to be implemented
        return donation.money
    }

    fun neededBudget(population: Int) : Double {
        throw RuntimeException("Not implemented yet!")
    }

    fun minimumBudget(population: Int): Double {
        return population * this.moneyFactor
    }

}
