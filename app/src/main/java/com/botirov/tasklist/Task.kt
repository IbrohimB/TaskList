package com.botirov.tasklist

import java.io.Serializable

data class Task(
    val content: String,
    var isCompleted: Boolean = false,
    val description: String? = null,
    val dueTime: String? = null  // this represents the due time; it can be null if no due time is set
) : Serializable