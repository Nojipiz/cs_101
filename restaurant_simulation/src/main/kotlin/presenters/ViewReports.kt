package presenters
import models.*
import models.cashBox.Invoice
import models.cashBox.PaymentType
import models.customers.CostumerGroup
import models.kitchen.Cook
import models.kitchen.SpecialtyType
import models.timers.Time
import views.MainWindow
import java.io.IOException
import javax.swing.Timer

class ViewReports{
    private var mainWindow: MainWindow? = null
    private lateinit var modelDao: Manager
    private var simulationClock: Time = Time(0,0,0)
    private var simulationLimit: Time = Time(0,0,0)
    var animationTimer: Timer? = null
    var speed = 0

    init {
        try {
            mainWindow = MainWindow(){
                simulationClock = Time(1, 1, 0, 0, 0)
                simulationLimit = Time(1, 2, 1, 1, 0)
                runSimultaion()
            }
            modelDao = Manager()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mainWindow!!.isVisible = true

    }

    private fun runSimultaion() {
        val delay = 0
        speed = 22
        animationTimer = Timer(delay) {
            timeConditions(speed)
            serveCustomer()
            prepareAndEatFood()
            exitToRestaurant()
            lookSimulationLImit()
        }
        animationTimer!!.start()
    }

    private fun lookSimulationLImit() {
        if (!simulationClock.beforeThan(simulationLimit)) {
            animationTimer!!.stop()
            showReports()
            println("La simulacion ha terminado---------------------")
        }
    }

    private fun showReports() {
        println("===================================== REPORTES ==============================")
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
        if (simulationClock.hour == 24) {
            simulationClock.hour = 0
            simulationClock.increaseDay()
        }
        if (simulationClock.day == 7) {
            simulationClock.day = 1
            simulationClock.increaseWeek()
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
            costumerGroup = paymentPriorityQueue!!.peek()
            println("  un grupo ha pagado y ha salido de retaurante " + (costumerGroup?.departureTime ?: ""))
            paymentPriorityQueue!!.pool()
        } else if (!paymentQueue.isEmpty) {
            costumerGroup = paymentQueue!!.peek()
            println("  un grupo ha pagado y ha salido de retaurante " + (costumerGroup?.departureTime ?: ""))
            paymentQueue!!.pool()
        }
    }

    private fun verifyServiceCompletion() {
        val invoList = modelDao?.invoiceList
        var costumerGroup: CostumerGroup?
        for (i in invoList!!.indices) {
            costumerGroup = invoList!![i].costumerGroup
            if (costumerGroup?.departureTime != null) {
                if (!simulationClock.beforeThan(costumerGroup.departureTime)) {
                    println("  un grupo esta en pasarela de pagos " + costumerGroup.departureTime)
                    if (invoList!![i].paymentType == PaymentType.CASH) {
                        modelDao!!.pushPaymentPriorityQueue(costumerGroup)
                    }
                    if (invoList!![i].paymentType == PaymentType.CREDIT_CARD) {
                        modelDao!!.pushPaymentQueue(costumerGroup)
                    }
                }
            }
        }
    }

    private fun serveCustomer() {
        val nextCostumerGroup = modelDao?.groupQueue?.peek()
        val waiter = findAvailableWaiter()
        if (!simulationClock.beforeThan(nextCostumerGroup?.arrivalTime) && waiter != null) {
            modelDao!!.addInvoiceList(Invoice(simulationClock, nextCostumerGroup, null))
            if (nextCostumerGroup != null) {
                modelDao!!.addOrderQueue(nextCostumerGroup)
            }
            println("un grupo ha ingresado en " + nextCostumerGroup?.arrivalTime.toString())
            modelDao!!.poolToGroupQueue()
        }
    }

    private fun prepareAndEatFood() {
        modelDao!!.deleteorder()
        if (!modelDao.orderQueue.isEmpty) {
            val order = modelDao.orderQueue.peek()
            val orderItem = order?.orderItemQueue?.peek()
            val cookList = modelDao.cookList
            println("reloj :$simulationClock")
            val cook = findAvailableCook(cookList, orderItem?.plateType)
            if (cook == null) println("ningun cocinero disponible")
            if (cook != null) {
                println("cocinero disponible " + cook.cookId)
                println("(antes) cocinero 0 disponible hasta " + cookList!![0].nextFreeTime)
                println("(antes) cocinero 1 disponible hasta " + cookList!![1].nextFreeTime)
                modelDao!!.cookPlate(cook, orderItem, simulationClock)
                modelDao!!.poolOrderItem()
                println("(despues) cocinero 0 disponible hasta " + cookList!![0].nextFreeTime)
                println("(despues) cocinero 1 disponible hasta " + cookList!![1].nextFreeTime)
            }
            cookFinishedPlate(cookList)
        }
    }

    private fun cookFinishedPlate(cookList: ArrayList<Cook>?) {
        for (i in cookList!!.indices) {
            if (!simulationClock.beforeThan(cookList[i]?.nextFreeTime)) {
                sendPlateToGroup(cookList[i])
            }
        }
    }

    private fun sendPlateToGroup(cook: Cook?) {
        val orderItemList = cook?.orderItemList
        for (i in orderItemList!!.indices) {
            orderItemList[i]?.let {
                modelDao!!.setDepartureTimeGroup(simulationClock, it)
                modelDao!!.setDepartureTimeGroup(simulationClock, it)
                println("  El cocinero " + cook.cookId + " ha finalizado la preparacion de " + it.plate.name +
                        " para el grupo " + it.idGroup + " " + simulationClock.toString())
            }
        }
    }

    private fun findAvailableCook(cookList: ArrayList<Cook>?, plateType: SpecialtyType?): Cook? {
        var cook: Cook? = null
        for (i in cookList!!.indices) {
            if (cookList[i]!!.isAvailable(plateType, simulationClock)) {
                cook = cookList[i]
                println("especilidades: plato-" + plateType + "  mesero-" + cook.specialy)
                println("encontramos disponible a cocinero " + cook.cookId)
                return cook
            }
        }
        return cook
    }

    private fun findAvailableWaiter(): Waiter? {
        val waiterList = modelDao.waiterList
        var waiter: Waiter? = null
        for (i in waiterList.filterNotNull().indices) {
            if (waiterList[i].isAvaliable) {
                waiter = waiterList[i]
            }
        }
        return waiter
    }

    // ======================== MUestreo de reportes ========================00
    private fun showPaytmentType() {
        val paymentsList = modelDao.paytmentType
        println("=======================================")
        paymentsList!!.forEach { (paymentType, countPaymentType) ->
            println("Se realizaron "
                    + countPaymentType.toInt().toString() + " pagos en " + paymentType.toString())
        }
    }

    private fun showGrossIncome() {
        val grossIncome = modelDao.grossIncome
        println("""
    =======================================
    Los Ingresos en bruto son $grossIncome
    """.trimIndent())
    }

    private fun showWaiterBestCalificated() {
        val waiter = modelDao.waiterBestCalificated
        println("""
    =======================================
    El mesero mejor calificado es ${waiter.id}
    """.trimIndent())
    }

    private fun showPlatesBestCalificated() {
        val platesBestCalificated = modelDao.entreePlateMainPlateAndDessertBestCalificated
        println("""
    =======================================
    La entrada mejor calificada es ${platesBestCalificated!![0].name}
    El plato fuerte mejor calificado es ${platesBestCalificated!![1].name}
    El postre mejor calificado es ${platesBestCalificated!![2].name}
    """.trimIndent())
    }

    fun showPlatesBestSeller() {
        val platesBestCalificated = modelDao.entreePlateMainPlateAndDessertBestSeller
        println("""
    =======================================
    La entrada mejor vendida es ${platesBestCalificated!![0].name}
    El plato fuerte mejor vendido es ${platesBestCalificated!![1].name}
    El postre mejor vendido es ${platesBestCalificated!![2].name}
    """.trimIndent())
    }
}