@file:Suppress("EXPERIMENTAL_IS_NOT_ENABLED")

package com.eclecticengineering.bdm

import com.eclecticengineering.bdm.Resource.*
import com.typesafe.config.ConfigFactory
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.hocon.Hocon
import kotlinx.serialization.hocon.decodeFromConfig
import kotlinx.serialization.hocon.encodeToConfig
import java.io.InputStreamReader

@OptIn(ExperimentalSerializationApi::class)
fun main() {
    val trials = 100_000
    val externalConfig = Hocon.decodeFromConfig<ExternalConfig>(
        ConfigFactory.parseReader(
            InputStreamReader(System.`in`)
        )
    )
    val config = Config(externalConfig)
    val counters = CounterSet(config)
    val chance = config.itemType.makeChance(config, counters)
    var successes = 0L
    val trackers: MutableMap<Resource, Tracker> = mutableMapOf()
    Resource.values().forEach {
        trackers[it] = Tracker()
    }
    for (i in 1..trials) {
        val enhancer = Enhancer(chance, counters, config)
        if (enhancer.execute()) {
            ++successes
        } else {
            Resource.values().forEach {
                if (counters.counters.getValue(it).mustStop()) {
                    trackers[it]!!.incrementExceeded()
                }
            }
        }
        Resource.values().forEach {
            trackers.getValue(it).record(counters.counters.getValue(it).getCount())
        }
        counters.reset()
    }
    println(Hocon.encodeToConfig(externalConfig))
    println(Hocon.encodeToConfig(config))
    println("probability of success = ${successes.toDouble() / trials}")
    printResult("crystal", trials, trackers[CRYSTAL])
    printResult("valks10", trials, trackers[VALKS10])
    printResult("valks50", trials, trackers[VALKS50])
    printResult("valks100", trials, trackers[VALKS100])
    printResult("restoreScrolls", trials, trackers[RESTORE_SCROLLS])
}

private fun printResult(
    name: String,
    @Suppress("SameParameterValue") trials: Int,
    tracker: Tracker?
) {
    if (tracker == null) {
        return
    }
    val average: Double = tracker.used.toDouble() / trials
    print(name)
    for (i in name.length..15) {
        print(" ")
    }
    println(
        "exceeded= ${(tracker.exceeded.toDouble() / trials).format(7, 5)}"
                + " average used= ${average.format(8, 2)}"
                + " fraction below average= "
                + (tracker.distribution.getCountBelow(average.toInt()).toDouble() / trials).format(5, 3)
                + " median=${tracker.distribution.getMedian().format(6)} "
                + " 75%=${tracker.distribution.getPercentile(75).format(6)} "
                + " 90%=${tracker.distribution.getPercentile(90).format(6)} "
                + " 99%=${tracker.distribution.getPercentile(99).format(6)} "
    )
}

class Distribution {
    private var values: MutableList<Int> = mutableListOf()
    private var analyzed = false

    fun record(value: Int) {
        values.add(value)
        analyzed = false
    }

    fun getMedian(): Int {
        analyze()
        return values[values.size / 2]
    }

    fun getPercentile(percentage: Int): Int {
        analyze()
        return values[((values.size * percentage).toDouble() / 100).toInt()]
    }

    fun getCountBelow(value: Int): Int {
        analyze()
        return values.indexOfFirst { it >= value }
    }

    private fun analyze() {
        if (!analyzed) {
            values.sort()
            analyzed = true
        }
    }
}

@Suppress("unused")
enum class ItemType {
    GEAR {
        override fun makeChance(config: Config, counters: CounterSet): Chance =
            Valks(
                config.resources[VALKS100], 1.0, counters.counters[VALKS100],
                Valks(
                    config.resources[VALKS50], 0.50, counters.counters[VALKS50],
                    Valks(
                        config.resources[VALKS10], 0.10, counters.counters[VALKS10],
                        SimpleChance(
                            listOf(70.0, 60.0, 40.0, 20.0, 10.0, 7.0, 5.0, 3.0, 1.0, 0.5),
                            counters.counters[CRYSTAL]
                        )
                    )
                )
            )
    },
    ACCESSORY {
        override fun makeChance(config: Config, counters: CounterSet): Chance =
            SimpleChance(listOf(100.0, 100.0, 100.0, 60.0, 41.0, 28.0, 9.0, 4.0, 1.0, 0.3), counters.counters[CRYSTAL])
    },
    RELIC {
        override fun makeChance(config: Config, counters: CounterSet): Chance =
            SimpleChance(listOf(50.0, 30.0, 10.0, 10.0, 10.0, 8.0, 5.0, 3.0, 1.0, 0.5), counters.counters[CRYSTAL])
    },
    TOTEM {
        override fun makeChance(config: Config, counters: CounterSet): Chance =
            SimpleChance(listOf(70.0, 60.0, 50.0, 30.0, 15.0, 10.0, 5.0, 2.0, 1.0, 0.5), counters.counters[CRYSTAL])
    };

    open fun makeChance(config: Config, counters: CounterSet): Chance {
        return SimpleChance(listOf(0.0), counters.counters[CRYSTAL])
    }
}

class Tracker {
    var exceeded: Long = 0
    var used: Long = 0
    val distribution: Distribution = Distribution()

    fun incrementExceeded() = ++exceeded
    fun record(count: Int) {
        used += count
        distribution.record(count)
    }
}
