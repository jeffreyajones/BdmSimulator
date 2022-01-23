package com.eclecticengineering.bdm

import com.eclecticengineering.bdm.Resource.*

enum class ExternalResource(val internalResource: Resource) {
    crystal(CRYSTAL), valks10(VALKS10), valks50(VALKS50), valks100(VALKS100), restoreScrolls(RESTORE_SCROLLS);
}