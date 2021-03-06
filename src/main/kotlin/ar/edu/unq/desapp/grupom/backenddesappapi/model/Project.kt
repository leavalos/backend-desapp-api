package ar.edu.unq.desapp.grupom.backenddesappapi.model

import ar.edu.unq.desapp.grupom.backenddesappapi.model.exceptions.project.*
import ar.edu.unq.desapp.grupom.backenddesappapi.model.user.User
import com.fasterxml.jackson.annotation.JsonAutoDetect
import lombok.Generated
import org.springframework.data.domain.PageRequest
import java.time.LocalDate
import javax.persistence.*

@Entity
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class Project {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null


    @Generated
    @Column(unique = true)
    var name: String

    @OneToMany(fetch = FetchType.EAGER)
    @Generated
    var donations: MutableList<Donation>
    @Generated
    var moneyFactor: Double
    @Generated
    var beginningDate: LocalDate
    @Generated
    var finishDate: LocalDate
    @Generated
    var isFinished: Boolean
    @Generated
    private var population: Int
    @Generated
    var minPercentageToFinish: Int

    constructor(name: String, beginningDate: LocalDate = LocalDate.now(), finishDate: LocalDate, population: Int) {
        this.verifyName(name)
        this.name = name
        this.donations = mutableListOf()
        this.moneyFactor = 1000.0
        this.beginningDate = beginningDate
        this.finishDate = finishDate
        this.isFinished = false
        this.population = population
        this.minPercentageToFinish = 100
    }

    constructor(
            name: String,
            moneyFactor: Double =  1000.00,
            beginningDate: LocalDate = LocalDate.now(),
            finishDate: LocalDate,
            population: Int,
            minPercentage: Int = 100) {
        this.verifyParameters(name, moneyFactor, minPercentage, beginningDate, finishDate)
        this.name = name
        this.donations = mutableListOf()
        this.moneyFactor = moneyFactor
        this.beginningDate = beginningDate
        this.finishDate = finishDate
        this.isFinished = false
        this.population = population
        this.minPercentageToFinish = minPercentage
        this.donations = mutableListOf()
    }

    private fun verifyParameters(
            name: String,
            moneyFactor: Double,
            minPercentage: Int,
            beginningDate: LocalDate,
            finishDate: LocalDate) {
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
        verifyProjectIsNotFinished()
        this.donations.add(donation)
        user.earnPoints(this.pointsEarnedWithDonation(donation, user))
        user.addDonation(donation)
    }

    private fun verifyProjectIsNotFinished() {
        if (this.isFinished) {
            throw CannotMakeADonationToAFinishedProjectException()
        }
    }


    private fun pointsEarnedWithDonation(donation: Donation, user: User): Double {
        var points  = 00.00
        if (donation.money > 1000) {
            points += donation.money.toInt()
        }
        if (this.population < 2000) {
            points += (donation.money * 2)
        }
        if (user.madeOneDonationInThisMonth()) {
            points += 500
        }
        return points
    }

    fun totalBudgedRequired(): Double = this.population * this.moneyFactor

    fun minimumBudgetToFinish(): Double {
        return totalBudgedRequired() * this.percentage()
    }

    private fun percentage() = this.minPercentageToFinish / 100

    fun budgetCollected(): Double = donations.map { d -> d.money }.sum()

    fun neededBudget(): Double  = minimumBudgetToFinish() - budgetCollected()

    fun finishProject() {
        this.verifyNeededBudgetCompleted()
        this.verifyFinishDateHasPassed()
        this.isFinished = true
    }

    fun finishProjectRaisedMoney() {
        this.verifyNeededBudgetCompleted()
        this.isFinished = true
    }

    private fun verifyFinishDateHasPassed() {
        if (this.finishDate.isAfter(LocalDate.now())) {
            throw TheFinishDateOfTheProjectHasNotPassedYetException()
        }
    }

    private fun verifyNeededBudgetCompleted() {
        if (this.neededBudget() > 0.00) {
            throw TheNeededBudgetOfTheProjectIsNotCompletedYetException()
        }
    }

    fun population(): Int {
        return this.population
    }

    fun setPopulation(population: Int) {
        this.population = population
    }

    fun name() : String {
        return this.name
    }
}
