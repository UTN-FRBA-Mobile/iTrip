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

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Defines methods for using the SleepNight class with Room.
 */
@Dao
interface QuestionDatabaseDao {

    @Insert
    fun insertQuestion(question: Question)


    @Update
    fun updateQuestion(question: Question)

    @Delete
    fun deleteQuestion(question: Question)

 /*   @Insert
    fun insertAnswer(answer: Answer)

    @Update
    fun UpdateAnswer(answer: Answer)

    @Delete
    fun DeleteAnswer(answer: Answer)
*/
    @Query("SELECT * from questions_table WHERE questionId = :key")
    fun get(key: Long): Question?

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM questions_table")
    fun clear()

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by name in descending order.
     */
   /* @Query("SELECT * FROM questions_table ORDER BY questionId DESC")
    fun getAllQuestions(): LiveData<List<Question>>

    @Query("SELECT * FROM Answer where qId = :questionId ORDER BY id DESC")
    fun getAllPossibleAnswers(questionId:Long): List<Answer>

    @Query("SELECT * FROM Answer where qId = :questionId AND chosenByUser=1 ORDER BY id DESC")
    fun getAllAnswersChosenByUser(questionId:Long): List<Answer>
*/


}

