package models.customers

import models.Rating
import models.QualificationType
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
                QualificationType.MAIN_COURSE, Utilities.randomNumber(0, QUANTITY_OF_MAIN_PLATE - 1),  //				QualificationType.FUERTE, 0,
                Utilities.randomNumber(MINIUM_QUALIFICATION, MAXIUM_QUALIFICATION))
        )
        ratingList.add(
            Rating(
                QualificationType.ENTRY, Utilities.randomNumber(-1, QUANTITY_OF_ENTREE_PLATE - 1),
                Utilities.randomNumber(MINIUM_QUALIFICATION, MAXIUM_QUALIFICATION))
        )
        ratingList.add(
            Rating(
                QualificationType.DESSERT, Utilities.randomNumber(-1, QUANTITY_OF_DESSERT_PLATE - 1),
                Utilities.randomNumber(MINIUM_QUALIFICATION, MAXIUM_QUALIFICATION))
        )
        ratingList.add(
            Rating(
                QualificationType.DESSERT, Utilities.randomNumber(-1, QUANTITY_OF_DESSERT_PLATE - 1),
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