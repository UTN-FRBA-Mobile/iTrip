/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.itrip.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "questions_table")
data class Question(


    @PrimaryKey(autoGenerate = true)
    var questionId: Long = 0L,

    @ColumnInfo(name = "question")
    val question: String = ""



)

@Entity(foreignKeys = arrayOf(ForeignKey(entity = Question::class,
    parentColumns = arrayOf("questionId"),
    childColumns = arrayOf("qId"),
    onDelete = ForeignKey.CASCADE)))
data class Answer(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val qId: Long,
    @ColumnInfo(name = "answer")
    val answer: String,
    @ColumnInfo(name = "chosenByUser")
    var chosenByUser: Boolean =false

    )