package presenters

import models.*
import models.cashBox.Invoice
import models.cashBox.PaymentType
import models.customers.CostumerGroup
import models.kitchen.Cook
import models.kitchen.SpecialtyType
import models.timers.Time
import views.MainWindow
import javax.swing.Timer

class ViewReports {
    private var mainWindow: MainWindow? = null
    private lateinit var modelDao: Manager
    private var simulationClock: Time = Time(0, 0, 0)
    private lateinit var simulationLimit: Time

    var animationTimer: Timer? = null
    var speed = 0

    init {
        mainWindow = MainWindow({
            modelDao = Manager()
            simulationClock = Time(0, 0, 0)
            simulationLimit = Time(168, 0, 0)
            runSimultaion()
        }, { cookIndex ->
            val cook = modelDao.cookList.getOrNull(cookIndex)
            return@MainWindow cook?.efficency ?: mutableListOf()
        })
        mainWindow!!.isVisible = true
    }

    /**
     * This is our simulation core clock, this function makes that the politic of
     * "Mesas disponibles en el menor tiempo posible"
     */
    private fun runSimultaion() {
        val delay = 0
        speed = 15
        animationTimer = Timer(delay) {
            timeConditions(speed)
            serveCustomer()
            prepareAndEatFood()
            exitToRestaurant()
            simulationLimit()
        }
        animationTimer!!.start()
    }

    private fun simulationLimit() {
        if (!simulationClock.beforeThan(simulationLimit)) {
            animationTimer!!.stop()
            showReports()
        }
    }

    private fun showReports() {
        // Entrada, Plato Fuerte y Postre mejor vendidos
        showPlatesBestSeller()
        // Entrada, Plato Fuerte y Postre mejor calificados
        showPlatesBestCalificated()
        // Mesero mejor calificado
        showWaiterBestCalificated()
        // Ingresos en bruto del restaurante en cada una de las muestras
        showGrossIncome()
        // Las modalidades de pago presentadas en cada una de las muestras
        showPaytmentType()
        mainWindow?.showResults("-------END------")
    }

    private fun timeConditions(speed: Int) {
        simulationClock.second = simulationClock.second + speed
        if (simulationClock.second >= 60) {
            simulationClock.second = 0
            simulationClock.increaseMinute()
        }
        if (simulationClock.minute >= 60) {
            simulationClock.minute = 0
            simulationClock.increaseHour()
        }
    }

    private fun exitToRestaurant() {
        verifyServiceCompletion()
        payService()
    }

    private fun payService() {
        val paymentPriorityQueue = modelDao.paymentPriorityQueue
        val paymentQueue = modelDao.paymentQueue
        var costumerGroup: CostumerGroup? = null
        if (!paymentPriorityQueue.isEmpty) {
            costumerGroup = paymentPriorityQueue.peek()
            println(
                "Un grupo ha pagado y ha salido de retaurante " + (costumerGroup?.departureTime
                    ?: "")
            )
            paymentPriorityQueue.pool()
        } else if (!paymentQueue.isEmpty) {
            costumerGroup = paymentQueue.peek()
            println(
                "  un grupo ha pagado y ha salido de retaurante " + (costumerGroup?.departureTime
                    ?: "")
            )
            paymentQueue.pool()
        }
    }

    private fun verifyServiceCompletion() {
        val invoList = modelDao.invoiceList
        var costumerGroup: CostumerGroup?
        for (i in invoList.indices) {
            costumerGroup = invoList[i].costumerGroup
            if (costumerGroup?.departureTime != null) {
                if (!simulationClock.beforeThan(costumerGroup.departureTime)) {
                    println("  un grupo esta en pasarela de pagos " + costumerGroup.departureTime)
                    if (invoList[i].paymentType == PaymentType.CASH) {
                        modelDao.pushPaymentPriorityQueue(costumerGroup)
                    }
                    if (invoList[i].paymentType == PaymentType.CREDIT_CARD) {
                        modelDao.pushPaymentQueue(costumerGroup)
                    }
                }
            }
        }
    }

    private fun serveCustomer() {
        val nextCostumerGroup = modelDao.groupQueue.peek() ?: return
        val waiter = getAvailableWaiter() ?: return
        if (!simulationClock.beforeThan(nextCostumerGroup.arrivalTime)) {
            modelDao.addInvoiceList(Invoice(simulationClock, nextCostumerGroup, null))
            modelDao.addOrderQueue(nextCostumerGroup)
            println("un grupo ha ingresado en " + nextCostumerGroup.arrivalTime.toString())
            modelDao.poolToGroupQueue()
        }
    }

    private fun prepareAndEatFood() {
        modelDao.deleteorder()
        if (!modelDao.orderQueue.isEmpty) {
            val order = modelDao.orderQueue.peek()
            val orderItem = order?.orderItemQueue?.peek()
            val cooksList = modelDao.cookList.shuffled()
            getAvailableCook(cooksList, orderItem?.plateType)?.let { avaliableCook ->
                println("cocinero disponible " + avaliableCook.cookId)
                println("(antes) cocinero 0 disponible hasta " + cooksList[0].nextFreeTime)
                println("(antes) cocinero 1 disponible hasta " + cooksList[1].nextFreeTime)
                modelDao.cookPlate(avaliableCook, orderItem, simulationClock)

                modelDao.poolOrderItem()
                println("(despues) cocinero 0 disponible hasta " + cooksList[0].nextFreeTime)
                println("(despues) cocinero 1 disponible hasta " + cooksList[1].nextFreeTime)
                cookFinishedPlate(cooksList)
            }
        }
    }

    private fun cookFinishedPlate(cookList: List<Cook>?) {
        cookList?.forEach { cook ->
            if (!simulationClock.beforeThan(cook.nextFreeTime))
                sendPlateToGroup(cook)
        }
    }

    private fun sendPlateToGroup(cook: Cook?) {
        val orderItemList = cook?.orderItemList
        for (i in orderItemList!!.indices) {
            orderItemList[i]?.let {
                modelDao.setDepartureTimeGroup(simulationClock, it)
                modelDao.setDepartureTimeGroup(simulationClock, it)
                println(
                    "El cocinero ${cook.cookId} ha finalizado la preparacion de ${it.plate.name} para el grupo ${it.idGroup} ${simulationClock}"
                )
            }
        }
    }

    private fun getAvailableCook(cookList: List<Cook>?, plateType: SpecialtyType?): Cook? {
        val availableCooker =
            cookList?.shuffled()?.find { it.isAvailable(plateType, simulationClock) }
        availableCooker?.let {
            println("Especilidades: plato- $plateType mesero ${availableCooker.specialy}")
            println("Encontramos disponible a cocinero ${availableCooker.cookId}")
        }
        return availableCooker
    }

    private fun getAvailableWaiter(): Waiter? {
        val waiterList = modelDao.waiterList.shuffled()
        return waiterList.find { it.isAvaliable }
    }

    // ======================== Muestreo de Reportes ========================00
    private fun showPaytmentType() {
        val paymentsList = modelDao.paytmentType
        paymentsList.forEach { (paymentType, countPaymentType) ->
            println("Se realizaron $countPaymentType pagos en $paymentType")
        }
    }

    private fun showGrossIncome() {
        val grossIncome = modelDao.grossIncome
        mainWindow?.showResults(
            """
            Los Ingresos en bruto son $grossIncome
            """.trimIndent()
        )
    }

    private fun showWaiterBestCalificated() {
        val waiter = modelDao.waiterBestCalificated
        mainWindow?.showResults(
            """
            El mesero mejor calificado es ${waiter.id}
            """.trimIndent()
        )
    }

    private fun showPlatesBestCalificated() {
        val platesBestCalificated = modelDao.entreePlateMainPlateAndDessertBestCalificated
        mainWindow?.showResults(
            """
            La entrada mejor calificada es ${platesBestCalificated[0].name}
            El plato fuerte mejor calificado es ${platesBestCalificated[1].name}
            El postre mejor calificado es ${platesBestCalificated[2].name}
            """.trimIndent()
        )
    }

    private fun showPlatesBestSeller() {
        val platesBestCalificated = modelDao.entreePlateMainPlateAndDessertBestSeller
        mainWindow?.showResults(
            """
            La entrada mejor vendida es ${platesBestCalificated[0].name}
            El plato fuerte mejor vendido es ${platesBestCalificated[1].name}
            El postre mejor vendido es ${platesBestCalificated[2].name}
            """.trimIndent()
        )
    }
}