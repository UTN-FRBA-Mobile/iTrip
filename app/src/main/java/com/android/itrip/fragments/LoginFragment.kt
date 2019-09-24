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
import com.android.itrip.R
import com.android.itrip.databinding.FragmentLoginBinding
import com.android.itrip.viewModels.LoginViewModel

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel
            by lazy {
                ViewModelProviders.of(this).get(LoginViewModel::class.java)
            }

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        binding = DataBindingUtil.inflate(
//            inflater, R.layout.fragment_login, container, false
//        )
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.fragment_login,
            container,
            false
        )
        binding.loginViewModel = viewModel

        binding.setLifecycleOwner(this)

        binding.loginButton.setOnClickListener { view: View ->
            if (viewModel.loginCorrect()) {
                view.findNavController()
                    .navigate(LoginFragmentDirections.actionLoginToHomeFragment())
            } else {
                Toast.makeText(this.context, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT)
                    .show()
            }
        }


        binding.passwordRecovery.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(
                    LoginFragmentDirections.actionLoginToPasswordRecoveryFragment(
                        viewModel.email.get()!!
                    )
                )
        }

        return binding.root
    }


}
