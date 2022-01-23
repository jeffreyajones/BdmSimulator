package com.eclecticengineering.bdm

import com.eclecticengineering.bdm.Resource.*
import kotlinx.serialization.Serializable

@Serializable
class Config {
    constructor(external: ExternalConfig) {
        itemType = external.itemType
        startStage = external.startStage
        target = external.target
        ExternalResource.values().forEach { externalResource ->
            val internalResource = externalResource.internalResource
            val externalResourceConfig = external.resources[externalResource]
            if (externalResourceConfig == null) {
                val startStage = if (internalResource == CRYSTAL) 0 else Int.MAX_VALUE
                resources[internalResource] = ResourceConfig(internalResource, Int.MAX_VALUE, startStage, Int.MAX_VALUE)
            } else {
                resources[internalResource] = ResourceConfig(
                    internalResource, externalResourceConfig.limit, externalResourceConfig.startStage,
                    Int.MAX_VALUE, externalResourceConfig.continueAfterExhausted
                )
            }
        }
        resources[VALKS10]?.endStage = resources[VALKS50]!!.startStage - 1
        resources[VALKS50]?.endStage = resources[VALKS100]!!.startStage - 1
    }

    val itemType: ItemType
    val startStage: Int
    val target: Int
    val resources: MutableMap<Resource, ResourceConfig> = mutableMapOf()
}

@Serializable
data class ResourceConfig(
    val resource: Resource,
    var limit: Int,
    var startStage: Int,
    var endStage: Int,
    var continueAfterExhausted: Boolean = false
)