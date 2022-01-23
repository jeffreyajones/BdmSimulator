package com.eclecticengineering.bdm

import kotlinx.serialization.Serializable

@Serializable
data class ExternalConfig(
    val itemType: ItemType,
    val startStage: Int,
    val target: Int,
    val resources: Map<ExternalResource, ExternalResourceConfig>
)

@Serializable
data class ExternalResourceConfig(
    val limit: Int,
    val startStage: Int = 0,
    val continueAfterExhausted: Boolean = false
)