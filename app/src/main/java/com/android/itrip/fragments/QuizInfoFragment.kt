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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.R
import com.android.itrip.activities.QuizActivity
import com.android.itrip.databinding.FragmentQuizInfoBinding
import com.android.itrip.models.Answer
import com.android.itrip.util.Constants
import com.android.itrip.util.DrawerLocker
import kotlinx.android.synthetic.main.activity_quiz.*
import kotlinx.android.synthetic.main.app_bar.view.*
import java.util.logging.Logger

class QuizInfoFragment : Fragment() {

    private val logger = Logger.getLogger(this::class.java.name)
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
        binding.lifecycleOwner = this
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
        // show toolbar shadow
        quiz.app_bar_quiz.view_toolbar_shadow.visibility = View.VISIBLE
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
            (activity as QuizActivity).quizViewModel.quiz.genero?.let {
                setText(it.value)
                handleOtherGender((activity as QuizActivity).quizViewModel.generos.indexOf(it))
            }
            setOnClickListener {
                closeKeyboard(false)
                setAlertDialog(
                    getString(Constants.DIALOG_TITLE_GEN),
                    this,
                    (activity as QuizActivity).quizViewModel.generos
                ) { (activity as QuizActivity).quizViewModel.quiz.genero = it }
            }
        }
        binding.textinputlayoutQuizInfoEdad.editText.apply {
            (activity as QuizActivity).quizViewModel.quiz.edad?.let { setText(it.toString()) }
            // when on focus changes it closes the keyboard because the other inputs cant be written
            setOnFocusChangeListener { _, hasFocus ->
                (activity as QuizActivity).quizViewModel.quiz.edad =
                    if (text.toString().isBlank()) null else text.toString().toInt()
                closeKeyboard(hasFocus)
            }
        }

        binding.textinputlayoutQuizInfoEstadocivil.editText.apply {
            setFocusableBehavior()
            (activity as QuizActivity).quizViewModel.quiz.estado_civil?.let { setText(it.value) }
            setOnClickListener {
                closeKeyboard(false)
                setAlertDialog(
                    getString(Constants.DIALOG_TITLE_EC),
                    this,
                    (activity as QuizActivity).quizViewModel.estados_civil
                ) { (activity as QuizActivity).quizViewModel.quiz.estado_civil = it }
            }
        }
        binding.textinputlayoutQuizInfoEstudios.editText.apply {
            setFocusableBehavior()
            (activity as QuizActivity).quizViewModel.quiz.nivel_de_estudios?.let { setText(it.value) }
            setOnClickListener {
                closeKeyboard(false)
                setAlertDialog(
                    getString(Constants.DIALOG_TITLE_EST),
                    this,
                    (activity as QuizActivity).quizViewModel.niveles_de_estudio
                ) { (activity as QuizActivity).quizViewModel.quiz.nivel_de_estudios = it }
            }
        }
        binding.textinputlayoutQuizInfoOcupacion.editText.apply {
            setFocusableBehavior()
            (activity as QuizActivity).quizViewModel.quiz.ocupacion?.let { setText(it.value) }
            setOnClickListener {
                closeKeyboard(false)
                setAlertDialog(
                    getString(Constants.DIALOG_TITLE_OCU),
                    this,
                    (activity as QuizActivity).quizViewModel.ocupaciones
                ) { (activity as QuizActivity).quizViewModel.quiz.ocupacion = it }
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

    private fun setAlertDialog(
        title: String,
        input: EditText,
        elements: List<Answer>,
        choosedAnswerCallback: (Answer) -> Unit
    ) {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(title)
        val items = elements.map { it.value }.toTypedArray()
        builder.setItems(items) { _, option ->
            input.setText(items[option])
            if (title == getString(Constants.DIALOG_TITLE_GEN)) handleOtherGender(option)
            choosedAnswerCallback(elements[option])
        }
        val dialog = builder.create()
        dialog.show()
    }

    private fun handleOtherGender(option: Int) {
        if (option == 2) {
            with(binding.textinputlayoutQuizInfoOtrogenero) {
                visibility = View.VISIBLE
                editText.setText(" ") // hack (do not remove)
                (activity as QuizActivity).quizViewModel.quiz.genero_otro?.let { editText.setText(it) }
                requestFocus()
            }
            binding.textinputlayoutQuizInfoEdad.layoutParams
                .let { it as RelativeLayout.LayoutParams }
                .addRule(RelativeLayout.BELOW, R.id.textinputlayout_quiz_info_otrogenero)
        } else {
            (activity as QuizActivity).quizViewModel.quiz.genero_otro = null
            binding.textinputlayoutQuizInfoOtrogenero.visibility = View.GONE
            binding.textinputlayoutQuizInfoEdad.layoutParams
                .let { it as RelativeLayout.LayoutParams }
                .addRule(RelativeLayout.BELOW, R.id.textinputlayout_quiz_info_otrogenero)
            binding.textinputlayoutQuizInfoOtrogenero.editText.setText("<hack>") // hack (do not remove)
        }
        binding.textinputlayoutQuizInfoOtrogenero.editText.setOnFocusChangeListener { _, _ ->
            (activity as QuizActivity).quizViewModel.quiz.genero_otro =
                if (binding.textinputlayoutQuizInfoOtrogenero.editText.text.toString().isBlank()) null
                else binding.textinputlayoutQuizInfoOtrogenero.editText.text.toString()
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
            view.findNavController()
                .navigate(
                    QuizInfoFragmentDirections.actionQuizInfoFragmentToQuizHobbiesFragment()
                )
        } else {
            logger.severe("All fields are mandatory")
        }
    }

}
