package models.customers

import models.rating.Rating
import models.rating.RatingType
import utilities.Generator

class Customer {
    private val customerId: Long
    val ratingList: ArrayList<Rating>

    init {
        customerId = idCounter++
        ratingList = ArrayList()
        creatQualificationList()
    }

    private fun creatQualificationList() {
        ratingList.add(
            Rating(
                RatingType.MAIN_COURSE, Generator.generateRandomNumber(0, QUANTITY_OF_MAIN_PLATE - 1),  //				RatingType.FUERTE, 0,
                Generator.generateRandomNumber(MINIUM_QUALIFICATION, MAXIUM_QUALIFICATION))
        )
        ratingList.add(
            Rating(
                RatingType.ENTRY, Generator.generateRandomNumber(-1, QUANTITY_OF_ENTREE_PLATE - 1),
                Generator.generateRandomNumber(MINIUM_QUALIFICATION, MAXIUM_QUALIFICATION))
        )
        ratingList.add(
            Rating(
                RatingType.DESSERT, Generator.generateRandomNumber(-1, QUANTITY_OF_DESSERT_PLATE - 1),
                Generator.generateRandomNumber(MINIUM_QUALIFICATION, MAXIUM_QUALIFICATION))
        )
        ratingList.add(
            Rating(
                RatingType.DESSERT, Generator.generateRandomNumber(-1, QUANTITY_OF_DESSERT_PLATE - 1),
                Generator.generateRandomNumber(MINIUM_QUALIFICATION, MAXIUM_QUALIFICATION))
        )
    }

    companion object {
        private var idCounter: Long = 1
        private const val QUANTITY_OF_MAIN_PLATE = 2
        private const val QUANTITY_OF_ENTREE_PLATE = 2
        private const val QUANTITY_OF_DESSERT_PLATE = 2
        private const val MAXIUM_QUALIFICATION = 2
        private const val MINIUM_QUALIFICATION = 2
    }
}