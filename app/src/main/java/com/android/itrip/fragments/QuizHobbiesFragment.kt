package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.itrip.MainActivity
import com.android.itrip.R
import com.android.itrip.adapters.QuizAdapter
import com.android.itrip.databinding.FragmentQuizHobbiesBinding
import com.android.itrip.models.Quiz
import com.android.itrip.viewModels.QuizViewModel

class QuizHobbiesFragment : Fragment() {

    private lateinit var viewAdapter: QuizAdapter
    private lateinit var binding: FragmentQuizHobbiesBinding
    private lateinit var quiz: Quiz
    private lateinit var quizViewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
        arguments!!.get("quiz")?.let { quiz = it as Quiz }
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz_hobbies, container, false
        )
        quizViewModel = QuizViewModel()
        binding.apply {
            quizViewModel = this@QuizHobbiesFragment.quizViewModel
            myRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireNotNull(activity).application)
                viewAdapter = QuizAdapter(quizViewModel!!.hobbies)
                adapter = viewAdapter
            }
            lifecycleOwner = this@QuizHobbiesFragment
            submitFloatingActionButton.setOnClickListener {
                var textMessage = ""
                viewAdapter.checkedHobbies.forEach {
                    textMessage = textMessage + it.value + ", "
                }
                quizViewModel!!.sendQuiz(quiz, viewAdapter.checkedHobbies) { finishQuiz() }
            }
        }
        return binding.root
    }

    private fun setBarTitle() {
        (activity as MainActivity).setActionBarTitle(getString(R.string.quiz_hobbies_title))
    }

    private fun finishQuiz() {
        view?.findNavController()
            ?.navigate(QuizHobbiesFragmentDirections.actionQuizHobbiesFragmentToQuizEndFragment())
    }

}
