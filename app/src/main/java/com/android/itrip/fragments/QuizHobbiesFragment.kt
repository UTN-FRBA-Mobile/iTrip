package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.MainActivity
import com.android.itrip.R
import com.android.itrip.adapters.QuizAdapter
import com.android.itrip.databinding.FragmentQuizHobbiesBinding
import com.android.itrip.models.Quiz
import com.android.itrip.services.QuizService
import com.android.itrip.viewModels.QuizViewModel

class QuizHobbiesFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: QuizAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var binding: FragmentQuizHobbiesBinding
    private lateinit var quiz: Quiz

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
        quiz = arguments!!.get("quiz") as Quiz
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz_hobbies, container, false
        )
        val application = requireNotNull(this.activity).application

        // RECYCLERVIEW logic
        recyclerView = binding.myRecyclerView
        viewManager = LinearLayoutManager(application)
        recyclerView.layoutManager = viewManager
        viewAdapter = QuizAdapter(QuizViewModel(application).hobbies)
        recyclerView.adapter = viewAdapter
        binding.lifecycleOwner = this

        binding.submitFloatingActionButton.setOnClickListener { resolveQuiz() }

        return binding.root
    }

    private fun setBarTitle() {
        (activity as MainActivity).setActionBarTitle(getString(R.string.quiz_hobbies_title))
    }

    private fun resolveQuiz() {
        var textMessage = ""
        viewAdapter.checkedHobbies.forEach {
            textMessage = textMessage + it.value + ", "
        }

        QuizService.postQuestions(
            quiz.addHobbies(viewAdapter.checkedHobbies.map { it.key }), {
                view?.findNavController()
                    ?.navigate(QuizHobbiesFragmentDirections.actionQuizHobbiesFragmentToQuizEndFragment())
            }, { error ->

            })
    }

}
