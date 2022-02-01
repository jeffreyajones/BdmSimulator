package com.eclecticengineering.bdm

import java.util.*

/** Given a configuration, runs through the specified enhancement, counting the amount of resources used. */
class Enhancer(
    private val chance: Chance,
    private val counters: CounterSet,
    private val config: Config
) {
    private val target: Int = config.target
    private val restoreScrollsStartStage: Int = config.resources[Resource.RESTORE_SCROLLS]?.startStage ?: 10
    private var random: Random = Random()

    fun execute(): Boolean {
        var stage = config.startStage
        while (stage < target && !counters.mustStop()) {
            val probability = chance.getProbability(stage)
            if (random.nextInt(1001) <= probability * 10) {
                ++stage
            } else if (stage >= restoreScrollsStartStage) {
                counters.counters[Resource.RESTORE_SCROLLS]?.increment()
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