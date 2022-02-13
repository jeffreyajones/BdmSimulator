package com.eclecticengineering.bdm

import org.junit.jupiter.api.Test

internal class CounterTest {
    @Test
    fun getCountZero() {
        val counter = Counter(ResourceConfig(Resource.CRYSTAL, 10, 1, 5))
        assert(counter.getCount() == 0)
    }

    @Test
    fun getCountOne() {
        val counter = Counter(ResourceConfig(Resource.CRYSTAL, 10, 1, 5))
        counter.increment()
        assert(counter.getCount() == 1)
    }

    @Test
    fun getCountMany() {
        val counter = Counter(ResourceConfig(Resource.CRYSTAL, 10, 1, 5))
        counter.increment()
        counter.increment()
        counter.increment()
        counter.increment()
        assert(counter.getCount() == 4)
    }

    @Test
    fun resetZero() {
        val counter = Counter(ResourceConfig(Resource.CRYSTAL, 10, 1, 5))
        val answer = counter.reset()
        assert(answer == 0)
        assert(counter.getCount() == 0)
    }

    @Test
    fun resetMany() {
        val counter = Counter(ResourceConfig(Resource.CRYSTAL, 10, 1, 5))
        counter.increment()
        counter.increment()
        counter.increment()
        val answer = counter.reset()
        assert(answer == 3)
        assert(counter.getCount() == 0)
    }

    @Test
    fun exhaustedZero() {
        val counter = Counter(ResourceConfig(Resource.CRYSTAL, 0, 1, 5))
        assert(counter.exhausted())
        assert(counter.mustStop())
    }

    @Test
    fun exhaustedOne() {
        val counter = Counter(ResourceConfig(Resource.CRYSTAL, 1, 1, 5))
        assert(!counter.exhausted())
        assert(!counter.mustStop())
    }

    @Test
    fun exhaustedOneTwo() {
        val counter = Counter(ResourceConfig(Resource.CRYSTAL, 1, 1, 5))
        counter.increment()
        assert(counter.exhausted())
        assert(counter.mustStop())
    }

    @Test
    fun mustStop() {
        val counter = Counter(ResourceConfig(Resource.CRYSTAL, 1, 1, 5, true))
        counter.increment()
        assert(!counter.mustStop())
    }
}