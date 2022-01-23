package com.eclecticengineering.bdm

enum class Resource(val cost: Int = 1) {
    CRYSTAL, VALKS10, VALKS50, VALKS100, RESTORE_SCROLLS(200);
}