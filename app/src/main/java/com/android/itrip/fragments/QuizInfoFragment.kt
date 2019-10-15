package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Spinner
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.R
import com.android.itrip.adapters.AnswerAdapter
import com.android.itrip.databinding.FragmentQuizInfoBinding
import com.android.itrip.models.Answer
import com.android.itrip.models.Quiz
import com.android.itrip.viewModels.QuizViewModel
import java.util.logging.Logger

/**
 * A simple [Fragment] subclass.
 */
class QuizInfoFragment : Fragment() {

    private lateinit var binding: FragmentQuizInfoBinding
    private val logger = Logger.getLogger(this::class.java.name)
    lateinit var quizViewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz_info, container, false
        )

        val application = requireNotNull(this.activity).application

        quizViewModel = QuizViewModel(application)

        binding.quizViewModel = quizViewModel

        binding.submitFloatingActionButton.setOnClickListener { view: View ->
            val genero = binding.generoSpinner.selectedItem as Answer
            val estadoCivil = binding.estadocivilSpinner.selectedItem as Answer
            val estudios = binding.estudiosSpinner.selectedItem as Answer
            val ocupacion = binding.ocupacionSpinner.selectedItem as Answer
            val quiz = Quiz(
                genero.key,
                binding.otrogeneroTextinputedittext.text.toString(),
                binding.edadTextinputedittext.text.toString().toInt(),
                estadoCivil.key,
                estudios.key,
                ocupacion.key
            )

            val bundle = bundleOf(
                "quiz" to quiz
            )
            view.findNavController()
                .navigate(
                    QuizInfoFragmentDirections.actionQuizInfoFragmentToQuizHobbiesFragment().actionId
                    , bundle
                )
        }

        setSpinner(binding.generoSpinner, quizViewModel.genero)
        setSpinner(binding.estadocivilSpinner, quizViewModel.estado_civil)
        setSpinner(binding.estudiosSpinner, quizViewModel.nivel_de_estudios)
        setSpinner(binding.ocupacionSpinner, quizViewModel.ocupacion)


        return binding.root
    }

    private fun setSpinner(spinner: Spinner, answerList: List<Answer>) {
        val adapter = AnswerAdapter(
            activity!!,
            R.layout.answer_item,
            R.id.answer_textview,
            answerList
        )
        spinner.adapter = adapter
        spinner.setSelection(Adapter.NO_SELECTION, false)
        spinner.onItemSelectedListener = adapter

    }

}
