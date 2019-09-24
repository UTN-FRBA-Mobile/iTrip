package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.R
import com.android.itrip.databinding.FragmentPasswordRecoveryBinding


/**
 * A simple [Fragment] subclass.
 */
class PasswordRecoveryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val myValue = this.arguments!!.getString("email")
        val binding: FragmentPasswordRecoveryBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_password_recovery, container, false
        )
        binding.emailInput.editText!!.setText(myValue)
        binding.loginButton.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(PasswordRecoveryFragmentDirections.actionPasswordRecoveryFragmentToEmailSentFragment())
        }
        return binding.root
    }

}
