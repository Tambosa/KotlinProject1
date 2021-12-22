package com.aroman.kotlinproject1.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aroman.kotlinproject1.databinding.DetailFragmentBinding
import com.aroman.kotlinproject1.model.Weather
import kotlinx.android.synthetic.main.detail_fragment.*

class DetailFragment : Fragment() {

    companion object {
        fun newInstance(bundle: Bundle?): DetailFragment {
            val fragment = DetailFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Weather>("WEATHER_EXTRA")?.let {
            binding.cityName.text = it.city.name
            binding.temperature.text = it.temperature.toString()
            binding.latitude.text = it.city.lat.toString()
            binding.longitude.text = it.city.lon.toString()
        }
        view.simpleSnack("Showing ${cityName.text}")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}