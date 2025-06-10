package com.example.simpleweather.ui.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.simpleweather.R
import com.example.simpleweather.databinding.FragmentWelcomeBinding
import com.example.simpleweather.ui.weatherlist.WeatherListFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WelcomeFragment : Fragment() {
    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!
    private var animationJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Setup Lottie animation
        binding.sunAnimation.apply {
            setAnimation(R.raw.sun_animation)
            playAnimation()
        }

        // Start a coroutine to handle the delay
        animationJob = CoroutineScope(Dispatchers.Main).launch {
            delay(3000) // 3 seconds delay
            binding.sunAnimation.cancelAnimation()
            // Navigate to WeatherListFragment using fragment transaction
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, WeatherListFragment())
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        animationJob?.cancel()
        binding.sunAnimation.cancelAnimation()
        _binding = null
    }
}