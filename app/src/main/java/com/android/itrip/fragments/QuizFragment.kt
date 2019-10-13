package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.android.itrip.R
import com.android.itrip.database.QuestionDatabase
import com.android.itrip.databinding.FragmentQuizBinding
import com.android.itrip.viewModels.DestinationViewModel
import com.android.itrip.viewModels.QuizViewModel
import com.android.itrip.viewModels.QuizViewModelFactory

/**
 * A simple [Fragment] subclass.
 */
class QuizFragment : Fragment() {


    lateinit var currentQuestion: QuizViewModel.Question
    lateinit var answers: MutableList<String>
    lateinit var questionsViewModel: QuizViewModel
    var questionIndex = 0
    private lateinit var questions: MutableList<QuizViewModel.Question>
    //numquestions will is initialized in Oncreate method
    private val numQuestions : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentQuizBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz, container, false
        )

        val application = requireNotNull(this.activity).application
        val dataSource = QuestionDatabase.getInstance(application).questionDao
        val viewModelFactory = QuizViewModelFactory(dataSource, application)

        // Bind this fragment class to the layout
        questionsViewModel=ViewModelProviders.of(this, viewModelFactory
            ).get(QuizViewModel::class.java)

        binding.quizViewModel = questionsViewModel

        questions = questionsViewModel.questions.toMutableList()
        val numQuestions = questionsViewModel.questions.size
        // Set the onClickListener for the submitButton

        binding.nextQuiz.setOnClickListener { view: View ->
            val checkedId = binding.answers.checkedRadioButtonId
            // Do nothing if nothing is checked (id == -1)
            if (-1 != checkedId) {
                var answerIndex = 0
                when (checkedId) {
                    R.id.second -> answerIndex = 1
                    R.id.third -> answerIndex = 2
                }
                // The first answer in the original question is always the correct one, so if our
                // answer matches, we have the correct answer.

                questionIndex++
                // Advance to the next question
                if (questionIndex < numQuestions) {
                    currentQuestion = questions[questionIndex]
                    setQuestion()
                    binding.invalidateAll()
                } else {
                    // We've won!  Navigate to the gameWonFragment.
                    view.findNavController()
                        .navigate(QuizFragmentDirections.actionQuizFragmentToQuizEndFragment())
                }

            }

        }

//        binding.nextQuiz.setOnClickListener { view: View ->
//            view.findNavController()
//                .navigate(QuizFragmentDirections.actionQuizFragmentToQuizEndFragment())
//        }
        return binding.root
    }



    // Sets the question and randomizes the answers.  This only changes the data, not the UI.
    // Calling invalidateAll on the FragmentGameBinding updates the data.
    private fun setQuestion() {
        currentQuestion = questions[questionIndex]
        // randomize the answers into a copy of the array
        answers = currentQuestion.answers.toMutableList()
        (activity as AppCompatActivity).supportActionBar?.title =
            "Q: " + (questionIndex + 1) + " of " + numQuestions
    }

}
