package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.DrawerLocker
import com.android.itrip.QuizActivity
import com.android.itrip.R
import com.android.itrip.databinding.FragmentQuizInfoBinding
import com.android.itrip.models.Answer
import com.android.itrip.models.Quiz
import com.android.itrip.viewModels.QuizViewModel
import java.util.logging.Logger

private const val DIALOG_TITLE_GEN = R.string.quiz_info_dialog_title_genero
private const val DIALOG_TITLE_EC = R.string.quiz_info_dialog_title_estadocivil
private const val DIALOG_TITLE_EST = R.string.quiz_info_dialog_title_estudios
private const val DIALOG_TITLE_OCU = R.string.quiz_info_dialog_title_ocupacion

class QuizInfoFragment : Fragment() {

    private val logger = Logger.getLogger(this::class.java.name)
    private val quizViewModel: QuizViewModel = QuizViewModel()
    private lateinit var binding: FragmentQuizInfoBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz_info, container, false
        )
        disableHomeButton()
        bindings()
        return binding.root
    }

    private fun setBarTitle() {
        (activity as QuizActivity).setActionBarTitle(getString(R.string.quiz_info_title))
    }

    private fun disableHomeButton() {
        (activity as DrawerLocker).setDrawerEnabled(false)
        (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar!!.setHomeButtonEnabled(false)
    }

    private fun bindings() {
        binding.textinputlayoutQuizInfoGenero.editText.apply {
            setOnClickListener { view ->
                setText(" ") // hack (do not remove)
                setAlertDialog(getString(DIALOG_TITLE_GEN), this, quizViewModel.genero)
            }
        }
        binding.textinputlayoutQuizInfoEstadocivil.editText.apply {
            setOnClickListener { view ->
                setAlertDialog(getString(DIALOG_TITLE_EC), this, quizViewModel.estado_civil)
            }
        }
        binding.textinputlayoutQuizInfoEstudios.editText.apply {
            setOnClickListener { view ->
                setAlertDialog(getString(DIALOG_TITLE_EST), this, quizViewModel.nivel_de_estudios)
            }
        }
        binding.textinputlayoutQuizInfoOcupacion.editText.apply {
            setOnClickListener { view ->
                setAlertDialog(getString(DIALOG_TITLE_OCU), this, quizViewModel.ocupacion)
            }
        }
        binding.floatingactionbuttonQuizInfo.setOnClickListener { view -> createQuiz(view) }
    }

    private fun setAlertDialog(title: String, input: EditText, elements: List<Answer>) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(title)
        val items = elements.map { it.value }.toTypedArray()
        builder.setItems(items) { dialog, option ->
            input.setText(items[option])
            if (title == getString(DIALOG_TITLE_GEN)) handleOtherGender(option)
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun handleOtherGender(option: Int) {
        if (option == 2) {
            binding.textinputlayoutQuizInfoOtrogenero.visibility = View.VISIBLE
            binding.textinputedittextQuizInfoEdad.layoutParams
                .let { it as RelativeLayout.LayoutParams }
                .addRule(RelativeLayout.BELOW, R.id.textinputlayout_quiz_info_otrogenero)
        } else {
            binding.textinputlayoutQuizInfoOtrogenero.visibility = View.GONE
            binding.textinputedittextQuizInfoEdad.layoutParams
                .let { it as RelativeLayout.LayoutParams }
                .addRule(RelativeLayout.BELOW, R.id.textinputlayout_quiz_info_otrogenero)
            binding.textinputlayoutQuizInfoOtrogenero.editText.setText(" ") // hack (do not remove)
        }
    }

    private fun createQuiz(view: View) {
        binding.form.validate()
        if (binding.form.isValid) {
            val generoValue = binding.textinputlayoutQuizInfoGenero.editText.text.toString()
            val otroGeneroValue = binding.textinputlayoutQuizInfoOtrogenero.editText.text.toString()
            val edadValue = binding.textinputedittextQuizInfoEdad.editText.text.toString()
            val ecValue = binding.textinputlayoutQuizInfoEstadocivil.editText.text.toString()
            val estudiosValue = binding.textinputlayoutQuizInfoEstudios.editText.text.toString()
            val ocupacionValue = binding.textinputlayoutQuizInfoOcupacion.editText.text.toString()

            val genero = quizViewModel.genero.find { it.value == generoValue }!!.key
            val otroGenero = if (genero == "0") otroGeneroValue else null
            val edad = edadValue.toInt()
            val estadoCivil = quizViewModel.estado_civil.find { it.value == ecValue }!!.key
            val estudios = quizViewModel.nivel_de_estudios.find { it.value == estudiosValue }!!.key
            val ocupacion = quizViewModel.ocupacion.find { it.value == ocupacionValue }!!.key

            val quiz = Quiz(genero, otroGenero, edad, estadoCivil, estudios, ocupacion)
            view.findNavController()
                .navigate(
                    QuizInfoFragmentDirections.actionQuizInfoFragmentToQuizHobbiesFragment().actionId,
                    bundleOf("quiz" to quiz)
                )
        } else {
            logger.severe("All fields are mandatory")
        }
    }

}
