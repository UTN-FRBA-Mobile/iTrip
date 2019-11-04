package com.android.itrip.fragments


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.android.itrip.MainActivity
import com.android.itrip.QuizActivity
import com.android.itrip.R
import com.android.itrip.databinding.FragmentQuizEndBinding
import com.google.firebase.auth.FirebaseAuth

class QuizEndFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setBarTitle()
        val binding: FragmentQuizEndBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_quiz_end, container, false
        )
        binding.materialbuttonQuizEnd.setOnClickListener { finishQuiz() }
        return binding.root
    }

    private fun setBarTitle() {
        (activity as QuizActivity).setActionBarTitle(getString(R.string.quiz_end_title))
    }

    private fun finishQuiz() {
        val intent = Intent(activity!!, MainActivity::class.java).apply {
            putExtra("CurrentUser", FirebaseAuth.getInstance().currentUser)
        }
        startActivity(intent)
        activity!!.finish()
    }

}
