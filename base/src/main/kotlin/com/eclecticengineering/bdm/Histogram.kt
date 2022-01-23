package com.eclecticengineering.bdm

import kotlin.math.sqrt

/** Keeps counts of values in buckets of specified size. */
@Suppress("unused")
class Histogram(private val bucketSize: Int = 0, private val multiplier: Int = 1) {
    private var maxBucket = 10
    private val counts = mutableListOf(0, 0, 0)
    private var totalCount: Long = 0
    private var sum: Long = 0
    private var sumSquared: Long = 0

    init {
        for (i in 1..maxBucket) {
            counts.add(0)
        }
    }

    fun add(number: Int) {
        ++totalCount
        sum += number
        sumSquared += number * number
        val bucket: Int = number / bucketSize
        if (bucket > maxBucket) {
            for (i in maxBucket..bucket) {
                counts.add(0)
            }
            maxBucket = bucket
        }
        ++counts[bucket]
    }

    fun print() {
        val average = sum.toDouble() / totalCount
        val standardDeviation = sqrt(sumSquared.toDouble() / totalCount - average * average)
        println("avg=${average * multiplier} stddev=${standardDeviation * multiplier}")
        //print("sum=${sum} sumSquared=${sumSquared} ")
        //println("totalCount=${totalCount} avgSquared=${average * average}")
        for (i in 1..maxBucket) {
            println("${i * bucketSize * multiplier}: ${counts[i]}")
        }
    }
}