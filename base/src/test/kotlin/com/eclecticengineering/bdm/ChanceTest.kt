package com.eclecticengineering.bdm

import com.eclecticengineering.bdm.Resource.CRYSTAL
import org.junit.jupiter.api.Test

internal class ChanceTest {

    @Test
    fun simpleChance() {
        val counter = Counter(ResourceConfig(CRYSTAL, 5, 1, 3))
        val chance = SimpleChance(listOf(10.0, 20.0), counter)
        assert(counter.getCount() == 0)
        assert(chance.getProbability(0) == 10.0)
        assert(counter.getCount() == 1)
        assert(chance.getProbability(1) == 20.0)
        assert(counter.getCount() == 2)
    }

    @Test
    fun simpleChanceWithNullCounter() {
        val chance = SimpleChance(listOf(10.0, 20.0), null)
        assert(chance.getProbability(0) == 10.0)
        assert(chance.getProbability(1) == 20.0)
    }

    @Test
    fun valksBasic() {
        val counter = Counter(ResourceConfig(CRYSTAL, 5, 1, 3))
        val valksCounter = Counter(ResourceConfig(CRYSTAL, 5, 1, 3))
        val chance = Valks(
            ResourceConfig(CRYSTAL, 5, 1, 3), 7.0, valksCounter,
            SimpleChance(listOf(10.0, 20.0, 30.0, 40.0, 50.0), counter)
        )
    }
}