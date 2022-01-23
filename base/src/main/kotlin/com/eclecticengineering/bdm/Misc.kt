package com.eclecticengineering.bdm

import com.eclecticengineering.bdm.Resource.*
import java.util.*

interface Chance {
    fun getProbability(stage: Int): Double
}

class Counter(private val resourceConfig: ResourceConfig?) {
    private var count: Int = 0

    fun increment() {
        count += resourceConfig?.resource?.cost ?: 1
        if (resourceConfig != null && count > resourceConfig.limit) {
            count = resourceConfig.limit
        }
    }

    fun reset(): Int {
        val toReturn = count
        count = 0
        return toReturn
    }

    fun getCount(): Int {
        return count
    }

    fun exhausted(): Boolean {
        return count >= (resourceConfig?.limit ?: 0)
    }

    fun mustStop(): Boolean {
        return exhausted() && !(resourceConfig?.continueAfterExhausted ?: false)
    }
}

class SimpleChance(private val probabilities: List<Double>, private val counter: Counter?) : Chance {
    override fun getProbability(stage: Int): Double {
        counter?.increment()
        return probabilities[stage]
    }
}

class Valks(
    private val resourceConfig: ResourceConfig?,
    private val bonus: Double,
    private val counter: Counter?,
    private val baseChance: Chance
) : Chance {
    override fun getProbability(stage: Int): Double {
        val base = baseChance.getProbability(stage)
        return if (resourceConfig != null
            && stage in resourceConfig.startStage..resourceConfig.endStage
            && !(counter?.exhausted() ?: false)
        ) {
            counter?.increment()
            base * (1.0 + bonus)
        } else
            base
    }

}

class CounterSet(config: Config) {
    val counters: Map<Resource, Counter>

    init {
        this.counters = mutableMapOf()
        Resource.values().forEach { counters.put(it, Counter(config.resources[it])) }
    }

    fun reset() {
        counters.values.forEach { it.reset() }
    }

    fun mustStop(): Boolean {
        return counters.values.any { it.mustStop() }
    }

}

class Enhancer(
    private val chance: Chance,
    private val counters: CounterSet,
    private val config: Config
) {
    private val target: Int = config.target
    private val restoreScrollsStartStage: Int = config.resources[RESTORE_SCROLLS]?.startStage ?: 10
    private var random: Random = Random()

    fun execute(): Boolean {
        var stage = config.startStage
        while (stage < target && !counters.mustStop()) {
            val probability = chance.getProbability(stage)
            if (random.nextInt(1001) <= probability * 10) {
                ++stage
            } else if (stage >= restoreScrollsStartStage) {
                counters.counters[RESTORE_SCROLLS]?.increment()
                if (random.nextInt(100) > 50) {
                    --stage
                }
            } else {
                --stage
            }
            if (stage < 0)
                stage = 0
        }
        return stage == target
    }
}

fun Double.format(width: Int, digits: Int) = "%${width}.${digits}f".format(this)
fun Int.format(width: Int) = "%${width}d".format(this)
