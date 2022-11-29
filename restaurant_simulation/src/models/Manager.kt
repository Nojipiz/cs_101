package models

import persistence.FileOperations
import structures.QueuList

//import persistence.FileManager;
class Manager {
    private val class1List: ArrayList<ClaseLeturaEscritura>? = null
    private val dessertPlateList: ArrayList<DessertPlate>
    private val mainPlateList: ArrayList<MainPlate>
    private val entreePlateList: ArrayList<EntreePlate>

    private val timeArrivalsClientList: ArrayList<Time>?

    //	private ArrayList<CostumerGroup> CostumerGroupList;
    var groupQueue: QueuList<CostumerGroup?>
    var orderQueue: QueuList<Order?>
    var paymentQueue: QueuList<CostumerGroup>
    var paymentPriorityQueue: QueuList<CostumerGroup>
    val invoiceList: ArrayList<Invoice>
    val waiterList: ArrayList<Waiter>
    val cookList: ArrayList<Cook>

    init {
        dessertPlateList = ArrayList()
        entreePlateList = ArrayList()
        mainPlateList = ArrayList()
        groupQueue = QueuList(null)
        orderQueue = QueuList(null)
        paymentQueue = QueuList(null)
        paymentPriorityQueue = QueuList(null)
        invoiceList = ArrayList()
        waiterList = ArrayList()
        cookList = ArrayList()
        //		costumerEatingList = new ArrayList<>();
        timeArrivalsClientList = FileOperations.readFile()
        startRestaurantMenu()
        createWaiterList()
        createCookList()
        cretateGroupQueue()
    }

    private fun createWaiterList() {
        for (i in 0 until WAITER_NUMBER) {
            waiterList.add(Waiter())
        }
    }

    private fun createCookList() {
        cookList.add(Cook(SpecialtyType.POSTRE))
        cookList.add(Cook(SpecialtyType.ENTRADA))
    }

    private fun cretateGroupQueue() {
        for (i in timeArrivalsClientList!!.indices) {
            groupQueue.push(CostumerGroup(timeArrivalsClientList[i]))
        }
    }

    private fun startRestaurantMenu() {
        entreePlateList.add(EntreePlate("Causa de atún", Time(0, 7, 10), Time(0, 5, 0), 13.000))
        entreePlateList.add(EntreePlate("Chicharrón de calamar", Time(0, 4, 0), Time(0, 6, 0), 11.000))
        mainPlateList.add(MainPlate("Aguadito norteño", Time(0, 27, 12), Time(0, 11, 23), 33.800))
        mainPlateList.add(MainPlate("Chupe de langostinos", Time(0, 32, 0), Time(0, 15, 19), 35.200))
        dessertPlateList.add(DessertPlate("Chocotejas", Time(0, 8, 7), Time(0, 11, 34), 5.000))
        dessertPlateList.add(DessertPlate("Arroz con leche", Time(0, 7, 12), Time(0, 13, 19), 3.600))
    }

    fun poolToGroupQueue() {
        groupQueue.pool()
    }

    fun addOrderQueue(costumerGroup: CostumerGroup) {
        val idGroup = costumerGroup.id
        val clientList = costumerGroup.clientList
        val order = Order()
        var item: OrderItem? = null
        var code: Int
        var quialificationList: ArrayList<Quialification>
        for (i in clientList!!.indices) {
            if (clientList!![i] != null) {
                quialificationList = clientList[i]?.qualificationList ?: continue
                for (j in quialificationList!!.indices) {
                    code = quialificationList[j]!!.getcode()
                    if (code != -1) {
                        item = returnItem(item, code, quialificationList, j, idGroup)
                        order.addItem(item)
                    }
                }
            }
        }
        orderQueue.push(order)
    }

    private fun returnItem(item: OrderItem?, code: Int, quialificationList: ArrayList<Quialification>, index: Int, idGroup: Long): OrderItem? {
        var item = item
        var plate: Plate
        //		System.out.println("code..."+code);
        if (quialificationList!![index].type == QualificationType.ENTRADA) {
            plate = entreePlateList[code]
            item = OrderItem(plate, SpecialtyType.ENTRADA, idGroup)
        }
        if (quialificationList[index].type == QualificationType.FUERTE) {
            plate = mainPlateList[code]
            item = OrderItem(plate, SpecialtyType.FUERTE, idGroup)
        }
        if (quialificationList[index].type == QualificationType.POSTRE) {
            plate = dessertPlateList[code]
            item = OrderItem(plate, SpecialtyType.POSTRE, idGroup)
        }
        return item
    }

    fun cookPlate(cook: Cook, orderItem: OrderItem?, currentTime: Time?) {
        cookList[cook.id.toInt()].cookPlate(orderItem, currentTime)
    }

    fun setDepartureTimeGroup(currentTime: Time, orderItem: OrderItem) {
        currentTime.addTime(orderItem.plate.consumptionTime)
        for (i in invoiceList.indices) {
            if (invoiceList[i].costumerGroup?.id == orderItem.idGroup) {
                invoiceList[i].costumerGroup?.departureTime = currentTime
            }
        }
    }

    fun addInvoiceList(invoice: Invoice) {
        invoiceList.add(invoice)
    }

    fun pushPaymentQueue(costumerGroup: CostumerGroup?) {
        paymentQueue.push(costumerGroup!!)
    }

    fun pushPaymentPriorityQueue(costumerGroup: CostumerGroup?) {
        paymentPriorityQueue.push(costumerGroup!!)
    }

    fun poolOrderItem() {
        orderQueue.peek()?.orderItemQueue?.pool()
        if (orderQueue.peek()?.orderItemQueue?.isEmpty == true) {
            orderQueue.pool()
        }
    }

    fun deleteorder() {
        if (!orderQueue.isEmpty) {
            if (orderQueue.peek()?.orderItemQueue?.isEmpty == true) {
                orderQueue.pool()
            }
        }
    }

    // ================== Reportes ===============
    val paytmentType: HashMap<PaymentType, Int>
        get() {
            var countPaymentTypeCard = 0
            var countPaymentTypeEfecty = 0
            for (actualInvoice in invoiceList) {
                val paymentType = actualInvoice.paymentType
                if (paymentType == PaymentType.TARJETA) {
                    countPaymentTypeCard++
                } else {
                    countPaymentTypeEfecty++
                }
            }
            val paymentsList = HashMap<PaymentType, Int>()
            paymentsList[PaymentType.EFECTIVO] = countPaymentTypeEfecty
            paymentsList[PaymentType.TARJETA] = countPaymentTypeCard
            return paymentsList
        }

    /**
     * Ingresos en bruto del restaurante en cada una de las muestras
     * @return
     */
    val grossIncome: Int
        get() {
            var countEntreePlate = 0
            var countMainPlate = 0
            var countDessertPlate = 0
            for (actualInvoice in invoiceList) {
                val costumerGroup = actualInvoice.costumerGroup
                val clientsList = costumerGroup?.clientList
                for (actualClient in clientsList!!) {
                    val qualificationList = actualClient?.qualificationList
                    for (actualQuialification in qualificationList!!) {
                        val code = actualQuialification!!.getcode()
                        val qualificationType = actualQuialification.type
                        if (code != -1) {
                            if (qualificationType == QualificationType.ENTRADA) {
                                val entreePlate = entreePlateList[code]
                                countEntreePlate += entreePlate.cost.toInt()
                            } else if (qualificationType == QualificationType.FUERTE) {
                                val mainPlate = mainPlateList[code]
                                countMainPlate += mainPlate.cost.toInt()
                            } else if (qualificationType == QualificationType.POSTRE) {
                                val dessertPlate = dessertPlateList[code]
                                countDessertPlate += dessertPlate.cost.toInt()
                            }
                        }
                    }
                }
            }
            return countEntreePlate + countMainPlate + countDessertPlate
        }

    /**
     * Mesero con mayor calificaci�n diaria durante cada muestra
     * @return
     */
    val waiterBestCalificated: Waiter
        get() {
            val countScoreWaiterList = IntArray(waiterList.size)
            val countWaiterList = IntArray(waiterList.size)
            for (i in invoiceList.indices) {
                val actualInvoice = invoiceList[i]
                val quialification = actualInvoice.waiterQuialification
                if (quialification != null) {
                    val score = quialification.score
                    val code = quialification.getcode()
                    if (code != -1) {
                        countScoreWaiterList[code] += score
                        countWaiterList[code]++
                    }
                }
            }
            val averageWaiter = getAverageScore(countScoreWaiterList, countWaiterList)
            val coordenateWaiter = getBestCoordenate(averageWaiter)
            return waiterList[coordenateWaiter]
        }

    /**
     * Entrada, plato y Postre mejor vendidos
     * @return
     */
    val entreePlateMainPlateAndDessertBestCalificated: Array<Plate>
        get() {
            val countScoreEntreePlateList = IntArray(entreePlateList.size)
            val countEntreePlateList = IntArray(entreePlateList.size)
            val countScoreMainPlateList = IntArray(mainPlateList.size)
            val countMainPlateList = IntArray(mainPlateList.size)
            val countScoreDessertPlateList = IntArray(dessertPlateList.size)
            val countDessertPlateList = IntArray(dessertPlateList.size)
            for (actualInvoice in invoiceList) {
                val costumerGroup = actualInvoice.costumerGroup
                val clientsList = costumerGroup?.clientList
                for (actualClient in clientsList!!) {
                    val qualificationList = actualClient?.qualificationList
                    for (actualQuialification in qualificationList!!) {
                        val code = actualQuialification!!.getcode()
                        val score = actualQuialification.score
                        val qualificationType = actualQuialification.type
                        if (code != -1) {
                            if (qualificationType == QualificationType.ENTRADA) {
                                countScoreEntreePlateList[code] += score
                                countEntreePlateList[code]++
                            } else if (qualificationType == QualificationType.FUERTE) {
                                countScoreMainPlateList[code] += score
                                countMainPlateList[code]++
                            } else if (qualificationType == QualificationType.POSTRE) {
                                countScoreDessertPlateList[code] += score
                                countDessertPlateList[code]++
                            }
                        }
                    }
                }
            }
            val averageEntreePlate = getAverageScore(countScoreEntreePlateList, countEntreePlateList)
            val averageMainPlate = getAverageScore(countScoreMainPlateList, countMainPlateList)
            val averageDessertPlate = getAverageScore(countScoreDessertPlateList, countDessertPlateList)
            val coordenateEntreePlate = getBestCoordenate(averageEntreePlate)
            val coordenateMainPlate = getBestCoordenate(averageMainPlate)
            val coordenateDessertPlate = getBestCoordenate(averageDessertPlate)
            val entreePlate = entreePlateList[coordenateEntreePlate]
            val mainPlate = mainPlateList[coordenateMainPlate]
            val dessertPlate = dessertPlateList[coordenateDessertPlate]
            return arrayOf(entreePlate, mainPlate, dessertPlate)
        }

    val entreePlateMainPlateAndDessertBestSeller: Array<Plate>
        get() {
            val countEntreePlateList = IntArray(entreePlateList.size)
            val countMainPlateList = IntArray(mainPlateList.size)
            val countDessertPlateList = IntArray(dessertPlateList.size)
            for (actualInvoice in invoiceList) {
                val costumerGroup = actualInvoice.costumerGroup
                val clientsList = costumerGroup?.clientList
                for (actualClient in clientsList!!) {
                    val qualificationList = actualClient?.qualificationList
                    for (actualQuialification in qualificationList!!) {
                        val code = actualQuialification!!.getcode()
                        val qualificationType = actualQuialification.type
                        if (code != -1) {
                            if (qualificationType == QualificationType.ENTRADA) {
                                countEntreePlateList[code]++
                            } else if (qualificationType == QualificationType.FUERTE) {
                                countMainPlateList[code]++
                            } else if (qualificationType == QualificationType.POSTRE) {
                                countDessertPlateList[code]++
                            }
                        }
                    }
                }
            }
            val coordenateEntreePlate = getBestCoordenate(countEntreePlateList)
            val coordenateMainPlate = getBestCoordenate(countMainPlateList)
            val coordenateDessertPlate = getBestCoordenate(countDessertPlateList)
            val entreePlate = entreePlateList[coordenateEntreePlate]
            val mainPlate = mainPlateList[coordenateMainPlate]
            val dessertPlate = dessertPlateList[coordenateDessertPlate]
            return arrayOf(entreePlate, mainPlate, dessertPlate)
        }

    private fun getBestCoordenate(countEntreePlateList: IntArray): Int {
        var actual = 0
        var position = 0
        for (i in countEntreePlateList.indices) {
            if (countEntreePlateList[i] > actual) {
                actual = countEntreePlateList[i]
                position = i
            }
        }
        return position
    }

    private fun getAverageScore(scoreList: IntArray, countList: IntArray): IntArray {
        val averageList = IntArray(scoreList.size)
        for (i in scoreList.indices) {
            if (countList[i] != 0) {
                averageList[i] = scoreList[i] / countList[i]
            }
        }
        return averageList
    }

    companion object {
        private const val WAITER_NUMBER = 3

        //	private ArrayList<CostumerGroup> costumerEatingList;
        private const val WEEKS_TO_SIMULATE = 3
        private const val DAYS_PER_WEEKS = 7
    }
}