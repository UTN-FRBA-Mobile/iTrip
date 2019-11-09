package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.QuizActivity
import com.android.itrip.R
import com.android.itrip.adapters.HobbiesAdapter
import com.android.itrip.databinding.FragmentQuizHobbiesBinding
import com.android.itrip.models.Quiz
import com.android.itrip.services.QuizService
import com.android.itrip.viewModels.QuizViewModel
import java.util.logging.Logger

class QuizHobbiesFragment : Fragment() {

    private val logger = Logger.getLogger(this::class.java.name)
    private lateinit var recyclerView: RecyclerView
    private lateinit var hobbiesAdapter: HobbiesAdapter
    private lateinit var binding: FragmentQuizHobbiesBinding
    private lateinit var quiz: Quiz

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
        quiz = arguments?.get("quiz") as Quiz
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz_hobbies, container, false
        )
        bindings()
        return binding.root
    }

    private fun setBarTitle() {
        (activity as QuizActivity).setActionBarTitle(getString(R.string.quiz_hobbies_title))
    }

    private fun bindings() {
        val application = requireNotNull(activity).application
        // recyclerview
        recyclerView = binding.recyclerviewQuizHobbies
        recyclerView.layoutManager = LinearLayoutManager(application)
        recyclerView.itemAnimator = DefaultItemAnimator()
        hobbiesAdapter = HobbiesAdapter(QuizViewModel().hobbies)
        recyclerView.adapter = hobbiesAdapter
        binding.lifecycleOwner = this
        // button
        binding.floatingactionbuttonQuizHobbies.setOnClickListener { resolveQuiz() }
    }

    private fun resolveQuiz() {
        val quizCompleted = quiz.addHobbies(hobbiesAdapter.checkedHobbies.map { it.key })
        QuizService.postAnswers(quizCompleted, {
            // if source is 'preferences' just closes the activity and shows a toast message,
            // otherwise it continues to congrats view
            val activity = activity as QuizActivity
            if (activity.source == "preferences") {
                Toast
                    .makeText(context, "Preferencias actualizadas", Toast.LENGTH_LONG)
                    .show()
                activity.finish()
            } else {
                view?.findNavController()
                    ?.navigate(QuizHobbiesFragmentDirections.actionQuizHobbiesFragmentToQuizEndFragment())
            }
        }, { error ->
            logger.severe("Failed to post quiz answers - status: ${error.statusCode} - message: ${error.message}")
            Toast
                .makeText(context, "Hubo un problema, intente de nuevo", Toast.LENGTH_SHORT)
                .show()
        })
    }

}
