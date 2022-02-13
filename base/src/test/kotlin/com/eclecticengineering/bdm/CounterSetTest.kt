package com.eclecticengineering.bdm

import com.eclecticengineering.bdm.Resource.CRYSTAL
import com.eclecticengineering.bdm.Resource.VALKS10
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CounterSetTest {
    private var resourceConfigMap = emptyMap<Resource, ResourceConfig>()

    @BeforeEach
    fun beforeEach() {
        resourceConfigMap = mutableMapOf(
            CRYSTAL to ResourceConfig(CRYSTAL, 1, 1, 3),
            VALKS10 to ResourceConfig(VALKS10, 1, 1, 3, true)
        )
    }

    @Test
    fun getCounters() {
        val counterSet = CounterSet(resourceConfigMap)
        assert(counterSet.counters.size == 2)
        assert(counterSet.counters.contains(CRYSTAL))
        assert(counterSet.counters.contains(VALKS10))
        assert(!counterSet.counters.getValue(CRYSTAL).exhausted())
        assert(!counterSet.counters.getValue(VALKS10).exhausted())
    }

    @Test
    fun reset() {
        val counterSet = CounterSet(resourceConfigMap)
        counterSet.counters.getValue(CRYSTAL).increment()
        assert(counterSet.counters.getValue(CRYSTAL).exhausted())
        counterSet.reset()
        assert(!counterSet.counters.getValue(CRYSTAL).exhausted())
    }

    @Test
    fun mustStopOne() {
        val counterSet = CounterSet(resourceConfigMap)
        counterSet.counters.getValue(CRYSTAL).increment()
        assert(counterSet.mustStop())
    }

    @Test
    fun mustStopTwo() {
        val counterSet = CounterSet(resourceConfigMap)
        counterSet.counters.getValue(VALKS10).increment()
        assert(!counterSet.mustStop()) // VALKS10 is set up with continueAfterExhausted = true
    }
}