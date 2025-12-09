package com.adrian.monuver.core.domain.model

data class Account(
    val id: Int = 0,
    val name: String,
    val type: Int,
    val balance: Long,
    val isActive: Boolean
)