package com.eclecticengineering.bdm

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

class CounterSet(resourceConfigMap: Map<Resource, ResourceConfig>) {
    val counters: Map<Resource, Counter>

    init {
        this.counters = mutableMapOf()
        Resource.values().forEach { counters.put(it, Counter(resourceConfigMap[it])) }
    }

    fun reset() {
        counters.values.forEach { it.reset() }
    }

    fun mustStop(): Boolean {
        return counters.values.any { it.mustStop() }
    }

}