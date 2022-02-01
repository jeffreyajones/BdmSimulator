package com.eclecticengineering.bdm

fun Double.format(width: Int, digits: Int) = "%${width}.${digits}f".format(this)
fun Int.format(width: Int) = "%${width}d".format(this)
