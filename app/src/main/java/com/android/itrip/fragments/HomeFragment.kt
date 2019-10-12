package com.android.itrip.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.android.itrip.R
import com.android.itrip.databinding.FragmentHomeBinding
import com.android.itrip.util.VolleyController
import com.android.volley.Response
import com.android.volley.toolbox.ImageRequest
import java.util.logging.Logger

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment() {

    private val logger = Logger.getLogger(this::class.java.name)

    private lateinit var queue: VolleyController


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentHomeBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_home, container, false
        )

        queue = VolleyController.getInstance(context)

        val url = "https://proyecto.brazilsouth.cloudapp.azure.com/media/45.jpg"
        val request = ImageRequest(url,
            Response.Listener { bitmap ->
                logger.info("Image downloaded")
                binding.mainLogo.setImageBitmap(bitmap)
            }, 0, 0, null,
            Response.ErrorListener { error ->
                logger.info("Image failed: " + error.toString())
            })
        queue.addToRequestQueue(request)


        binding.createTravel.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToCreateTravelFragment())
        }
        binding.quiz.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToQuizHomeFragment())
        }
        return binding.root
    }


}
