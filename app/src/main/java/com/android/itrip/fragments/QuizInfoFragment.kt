package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.MainActivity
import com.android.itrip.R
import com.android.itrip.adapters.AnswerAdapter
import com.android.itrip.databinding.FragmentQuizInfoBinding
import com.android.itrip.models.Answer
import com.android.itrip.models.Quiz
import com.android.itrip.viewModels.QuizViewModel

class QuizInfoFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var binding: FragmentQuizInfoBinding
    lateinit var quizViewModel: QuizViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz_info, container, false
        )
        quizViewModel = QuizViewModel()
        binding.quizViewModel = quizViewModel
        binding.submitFloatingActionButton.setOnClickListener {
            val genero = binding.generoSpinner.selectedItem as Answer
            val otro_genero = binding.otrogeneroTextinputedittext.text.toString()
            val edadText = binding.edadTextinputedittext.text.toString()
            val edadInt = if (edadText.isBlank()) 0 else edadText.toInt()
            val estadoCivil = binding.estadocivilSpinner.selectedItem as Answer
            val estudios = binding.estudiosSpinner.selectedItem as Answer
            val ocupacion = binding.ocupacionSpinner.selectedItem as Answer
            val generoCondition: Boolean =
                genero.key.isBlank() || (genero.key == "O" && otro_genero.isBlank())
            val condition: Boolean =
                generoCondition || edadInt == 0 || estadoCivil.key.isBlank() || estudios.key.isBlank() || ocupacion.key.isBlank()
            if (!condition) {
                val quiz = Quiz(
                    genero.key,
                    otro_genero,
                    edadInt,
                    estadoCivil.key,
                    estudios.key,
                    ocupacion.key
                )
                val bundle = bundleOf(
                    "quiz" to quiz
                )
                it.findNavController()
                    .navigate(
                        QuizInfoFragmentDirections.actionQuizInfoFragmentToQuizHobbiesFragment().actionId,
                        bundle
                    )
            }
        }
        setSpinner(binding.generoSpinner, quizViewModel.genero).onItemSelectedListener = this
        setSpinner(binding.estadocivilSpinner, quizViewModel.estado_civil)
        setSpinner(binding.estudiosSpinner, quizViewModel.nivel_de_estudios)
        setSpinner(binding.ocupacionSpinner, quizViewModel.ocupacion)
        return binding.root
    }

    private fun setBarTitle() {
        (activity as MainActivity).setActionBarTitle(getString(R.string.quiz_info_title))
    }

    private fun setSpinner(spinner: Spinner, answerList: List<Answer>): Spinner {
        val adapter = AnswerAdapter(
            activity!!,
            R.layout.answer_item,
            R.id.answer_textview,
            answerList
        )
        spinner.adapter = adapter
        spinner.setSelection(Adapter.NO_SELECTION, false)
        return spinner
    }

    override fun onItemSelected(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ) {
        val answer: Answer = parent!!.getItemAtPosition(position) as Answer
        if (answer.key == "O") {
            binding.otrogeneroTextinputlayout.visibility = View.VISIBLE
            Toast.makeText(context, "Por favor ingrese género", Toast.LENGTH_SHORT).show()
        } else {
            binding.otrogeneroTextinputlayout.visibility = View.GONE
            binding.otrogeneroTextinputedittext.setText("")
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

}
