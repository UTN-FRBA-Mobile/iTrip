package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.itrip.R
import com.android.itrip.adapters.QuizAdapter
import com.android.itrip.databinding.FragmentQuizHobbiesBinding
import com.android.itrip.viewModels.QuizViewModel
import com.android.itrip.viewModels.QuizViewModelFactory
import java.util.logging.Logger

/**
 * A simple [Fragment] subclass.
 */
class QuizFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: QuizAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var binding: FragmentQuizHobbiesBinding

    private val logger = Logger.getLogger(this::class.java.name)

    lateinit var quizViewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz_hobbies, container, false
        )
        val application = requireNotNull(this.activity).application

        val quizViewModelFactory = QuizViewModelFactory(application)

        quizViewModel =
            ViewModelProviders.of(
                this, quizViewModelFactory
            ).get(QuizViewModel::class.java)

        binding.quizViewModel = quizViewModel

        //        //RECYCLERVIEW logic
        recyclerView = binding.myRecyclerView
        viewManager = LinearLayoutManager(application)
        recyclerView.layoutManager = viewManager

        viewAdapter = QuizAdapter(quizViewModel.hobbies)
        recyclerView.adapter = viewAdapter


        binding.lifecycleOwner = this
//        subscribeUi(viewAdapter)


        binding.submitFloatingActionButton.setOnClickListener {

            var textMessage = ""
            viewAdapter.checkedHobbies.forEach {
                textMessage = textMessage + it.value + ", "
            }
            textMessage += "agregadas."
            Toast.makeText(
                context,
                textMessage,
                Toast.LENGTH_SHORT
            ).show()
            quizViewModel.sendQuiz(viewAdapter.checkedHobbies) { finishQuiz() }
        }



        return binding.root
    }

    private fun finishQuiz() {
        view?.findNavController()
            ?.navigate(QuizFragmentDirections.actionQuizFragmentToQuizEndFragment())
    }

}
