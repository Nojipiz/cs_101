package models

import utilities.Utilities

class Client {
    private val id: Long
    val qualificationList: ArrayList<Quialification>

    init {
        id = idCounter++
        qualificationList = ArrayList()
        creatQualificationList()
    }

    private fun creatQualificationList() {
        qualificationList.add(Quialification(
                QualificationType.FUERTE, Utilities.randomNumber(0, QUANTITY_OF_MAIN_PLATE - 1),  //				QualificationType.FUERTE, 0,
                Utilities.randomNumber(MINIUM_QUALIFICATION, MAXIUM_QUALIFICATION)))
        qualificationList.add(Quialification(
                QualificationType.ENTRADA, Utilities.randomNumber(-1, QUANTITY_OF_ENTREE_PLATE - 1),
                Utilities.randomNumber(MINIUM_QUALIFICATION, MAXIUM_QUALIFICATION)))
        qualificationList.add(Quialification(
                QualificationType.POSTRE, Utilities.randomNumber(-1, QUANTITY_OF_DESSERT_PLATE - 1),
                Utilities.randomNumber(MINIUM_QUALIFICATION, MAXIUM_QUALIFICATION)))
        qualificationList.add(Quialification(
                QualificationType.POSTRE, Utilities.randomNumber(-1, QUANTITY_OF_DESSERT_PLATE - 1),
                Utilities.randomNumber(MINIUM_QUALIFICATION, MAXIUM_QUALIFICATION)))
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