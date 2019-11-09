package com.android.itrip.fragments


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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
        setQuizDescription()
        return binding.root
    }

    private fun setBarTitle() {
        // set the fragment title depending on source
        val quiz = activity as QuizActivity
        val title = if (quiz.source == "preferences") {
            R.string.quiz_info_title_update_preferences
        } else {
            R.string.quiz_info_title
        }
        quiz.setActionBarTitle(getString(title))
    }

    private fun setQuizDescription() {
        // hide the view description if source is 'preferences'
        val quiz = activity as QuizActivity
        if (quiz.source == "preferences") {
            binding.textviewQuizInfoDescription.visibility = View.GONE
        }
    }

    private fun disableHomeButton() {
        // disable only if it comes from preferences menu
        val quiz = activity as QuizActivity
        if (quiz.source == "preferences") {
            (activity as DrawerLocker).setDrawerEnabled(false)
            (activity as AppCompatActivity).supportActionBar!!.setDisplayHomeAsUpEnabled(false)
            (activity as AppCompatActivity).supportActionBar!!.setHomeButtonEnabled(false)
        }
    }

    private fun bindings() {
        binding.textinputlayoutQuizInfoGenero.editText.apply {
            setFocusableBehavior()
            setOnClickListener {
                closeKeyboard(hasFocus = false)
                setAlertDialog(getString(DIALOG_TITLE_GEN), this, quizViewModel.genero)
            }
        }
        // when on focus changes it closes the keyboard because the other inputs cant be written
        binding.textinputlayoutQuizInfoEdad.editText.setOnFocusChangeListener { _, hasFocus ->
            closeKeyboard(hasFocus)
        }
        binding.textinputlayoutQuizInfoEstadocivil.editText.apply {
            setFocusableBehavior()
            setOnClickListener {
                closeKeyboard(false)
                setAlertDialog(getString(DIALOG_TITLE_EC), this, quizViewModel.estado_civil)
            }
        }
        binding.textinputlayoutQuizInfoEstudios.editText.apply {
            setFocusableBehavior()
            setOnClickListener {
                closeKeyboard(false)
                setAlertDialog(getString(DIALOG_TITLE_EST), this, quizViewModel.nivel_de_estudios)
            }
        }
        binding.textinputlayoutQuizInfoOcupacion.editText.apply {
            setFocusableBehavior()
            setOnClickListener {
                closeKeyboard(false)
                setAlertDialog(getString(DIALOG_TITLE_OCU), this, quizViewModel.ocupacion)
            }
        }
        binding.floatingactionbuttonQuizInfo.setOnClickListener { view ->
            // hack (do not remove)
            if (binding.textinputlayoutQuizInfoOtrogenero.editText.text.toString() == " ") {
                binding.textinputlayoutQuizInfoOtrogenero.editText.setText("")
            }
            createQuiz(view)
        }
    }

    private fun setAlertDialog(title: String, input: EditText, elements: List<Answer>) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(title)
        val items = elements.map { it.value }.toTypedArray()
        builder.setItems(items) { _, option ->
            input.setText(items[option])
            if (title == getString(DIALOG_TITLE_GEN)) handleOtherGender(option)
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun handleOtherGender(option: Int) {
        if (option == 2) {
            with(binding.textinputlayoutQuizInfoOtrogenero) {
                visibility = View.VISIBLE
                editText.setText(" ") // hack (do not remove)
                requestFocus()
            }
            binding.textinputlayoutQuizInfoEdad.layoutParams
                .let { it as RelativeLayout.LayoutParams }
                .addRule(RelativeLayout.BELOW, R.id.textinputlayout_quiz_info_otrogenero)
        } else {
            binding.textinputlayoutQuizInfoOtrogenero.visibility = View.GONE
            binding.textinputlayoutQuizInfoEdad.layoutParams
                .let { it as RelativeLayout.LayoutParams }
                .addRule(RelativeLayout.BELOW, R.id.textinputlayout_quiz_info_otrogenero)
            binding.textinputlayoutQuizInfoOtrogenero.editText.setText("<hack>") // hack (do not remove)
        }
    }

    private fun closeKeyboard(hasFocus: Boolean) {
        if (!hasFocus) {
            binding.root.let { v ->
                val imm =
                    context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(v.windowToken, 0)
            }
            binding.textinputlayoutQuizInfoEdad.editText.clearFocus()
            binding.textinputlayoutQuizInfoOtrogenero.editText.clearFocus()
        }
    }

    private fun EditText.setFocusableBehavior() {
        // avoid double touching when interacting with the input
        keyListener = null
        isFocusableInTouchMode = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) focusable = View.NOT_FOCUSABLE
    }

    private fun createQuiz(view: View) {
        binding.form.validate()
        if (binding.form.isValid) {
            val generoValue = binding.textinputlayoutQuizInfoGenero.editText.text.toString()
            val otroGeneroValue = binding.textinputlayoutQuizInfoOtrogenero.editText.text.toString()
            val edadValue = binding.textinputlayoutQuizInfoEdad.editText.text.toString()
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
