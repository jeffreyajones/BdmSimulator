package com.eclecticengineering.bdm

/** Interface for getting the probability of success for a given stage */
interface Chance {
    fun getProbability(stage: Int): Double
}

/** Chance object that has probabilities stored in a list.  Counts each call to getProbability in a Counter */
class SimpleChance(private val probabilities: List<Double>, private val counter: Counter?) : Chance {
    override fun getProbability(stage: Int): Double {
        counter?.increment()
        return probabilities[stage]
    }
}

/**
 * Chance object that takes care of the special behavior of Advice of Valks or Akhram's Prophecy.
 * Uses an underlying Chance object for the base chance, then applies the probability modifier for the Advice/Prophecy.
 * Counts the number of advice/prophecy used, and if they're all used up, does not modify the underlying probability.
 */
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
            && counter?.exhausted() != true
        ) {
            counter?.increment()
            base * (1.0 + bonus)
        } else
            base
    }
}