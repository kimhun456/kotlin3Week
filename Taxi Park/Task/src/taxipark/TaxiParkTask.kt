package taxipark

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> =
        this.allDrivers.filter { driver ->
            !this.trips.map { it.driver }.contains(driver)
        }.toSet()

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> =
        this.allPassengers.filter { passenger ->
            this.trips.flatMap { it.passengers }.filter { passenger == it }.size >= minTrips
        }.toSet()


/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> = this.trips
        .filter { trip -> trip.driver == driver }
        .flatMap { it.passengers }
        .groupBy { it }
        .filter { (_, passengerList) -> passengerList.size > 1 }
        .map { it.key }
        .toSet()

/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> {
    // todo:: is this possible with partition??

    val discountPassengers = this.trips
            .filter { it.discount ?: 0.0 > 0.0 }
            .flatMap { it.passengers }
    val unDiscountPassengers = this.trips
            .filter { it.discount ?: 0.0 == 0.0 }
            .flatMap { it.passengers }

    return this.allPassengers
            .filter { passenger ->
                discountPassengers.count { it == passenger } > unDiscountPassengers.count { it == passenger }
            }.toSet()
}


/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? = this.trips
        .map { IntRange(it.duration / 10 * 10, it.duration / 10 * 10 + 9) }
        .groupBy { it }
        .maxBy { it.value.size }
        ?.key

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    if (trips.isEmpty()) return false
    val topDriverCount = this.allDrivers.size / 5
    val sortedList = this.allDrivers
            .map { driver ->
                driver to this.trips.filter { trip -> trip.driver == driver }.sumByDouble { it.cost }
            }
            .sortedByDescending { it.second }
    val totalCost = trips.sumByDouble { it.cost }
    val topDriversTotalCost = sortedList.subList(0, topDriverCount).sumByDouble { it.second }
    return topDriversTotalCost >= totalCost * 0.8
}