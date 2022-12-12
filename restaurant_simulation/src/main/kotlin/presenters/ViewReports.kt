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
            makeAndServe()
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
        showPlatesBestSeller()
        showPlatesBestCalificated()
        showWaiterBestCalificated()
        showGrossIncome()
        showPaymentType()
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
            paymentPriorityQueue.pool()
        } else if (!paymentQueue.isEmpty) {
            costumerGroup = paymentQueue.peek()
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
            modelDao.poolToGroupQueue()
        }
    }

    private fun makeAndServe() {
        modelDao.deleteorder()
        if (!modelDao.orderQueue.isEmpty) {
            val order = modelDao.orderQueue.peek()
            val orderItem = order?.orderItemQueue?.peek()
            val cooksList = modelDao.cookList.shuffled()
            getAvailableCook(cooksList, orderItem?.plateType)?.let { avaliableCook ->
                modelDao.cookPlate(avaliableCook, orderItem, simulationClock)
                modelDao.poolOrderItem()
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
            }
        }
    }

    private fun getAvailableCook(cookList: List<Cook>?, plateType: SpecialtyType?): Cook? {
        val availableCooker =
            cookList?.shuffled()?.find { it.isAvailable(plateType, simulationClock) }
        availableCooker?.let {
        }
        return availableCooker
    }

    private fun getAvailableWaiter(): Waiter? {
        val waiterList = modelDao.waiterList.shuffled()
        return waiterList.find { it.isAvaliable }
    }

    private fun showPaymentType() {
        val paymentsList = modelDao.paytmentType
        paymentsList.forEach { (paymentType, countPaymentType) ->
            mainWindow?.showResults("Se realizaron $countPaymentType pagos en $paymentType")
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
            El plato de entrada mejor calificado fue ${platesBestCalificated[0].name}
            El plato fuerte mejor calificado fue  ${platesBestCalificated[1].name}
            El postre mejor calificado fue ${platesBestCalificated[2].name}
            """.trimIndent()
        )
    }

    private fun showPlatesBestSeller() {
        val platesBestCalificated = modelDao.entreePlateMainPlateAndDessertBestSeller
        mainWindow?.showResults(
            """
            El plato de entrada mas vendido fue ${platesBestCalificated[0].name}
            El plato fuerte mas vendido fue ${platesBestCalificated[1].name}
            El postre mas vendido fue ${platesBestCalificated[2].name}
            """.trimIndent()
        )
    }
}