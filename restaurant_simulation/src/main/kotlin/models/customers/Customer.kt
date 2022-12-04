package models.customers

import models.rating.Rating
import models.rating.RatingType
import utilities.Utilities

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
                RatingType.MAIN_COURSE, Utilities.randomNumber(0, QUANTITY_OF_MAIN_PLATE - 1),  //				RatingType.FUERTE, 0,
                Utilities.randomNumber(MINIUM_QUALIFICATION, MAXIUM_QUALIFICATION))
        )
        ratingList.add(
            Rating(
                RatingType.ENTRY, Utilities.randomNumber(-1, QUANTITY_OF_ENTREE_PLATE - 1),
                Utilities.randomNumber(MINIUM_QUALIFICATION, MAXIUM_QUALIFICATION))
        )
        ratingList.add(
            Rating(
                RatingType.DESSERT, Utilities.randomNumber(-1, QUANTITY_OF_DESSERT_PLATE - 1),
                Utilities.randomNumber(MINIUM_QUALIFICATION, MAXIUM_QUALIFICATION))
        )
        ratingList.add(
            Rating(
                RatingType.DESSERT, Utilities.randomNumber(-1, QUANTITY_OF_DESSERT_PLATE - 1),
                Utilities.randomNumber(MINIUM_QUALIFICATION, MAXIUM_QUALIFICATION))
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