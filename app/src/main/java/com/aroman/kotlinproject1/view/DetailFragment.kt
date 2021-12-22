package com.aroman.kotlinproject1.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aroman.kotlinproject1.databinding.DetailFragmentBinding
import com.aroman.kotlinproject1.model.Weather
import com.aroman.kotlinproject1.model.WeatherDTO
import com.aroman.kotlinproject1.model.WeatherLoader
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
        loadingContainer.show()
        arguments?.getParcelable<Weather>("WEATHER_EXTRA")?.let {
            with(binding) {
                cityName.text = it.city.name
                cityCoordinates.text = "lat: ${it.city.lat} lon: ${it.city.lon}"
            }

            WeatherLoader.load(it.city, object : WeatherLoader.OnWeatherLoadListener {

                override fun onLoaded(weatherDTO: WeatherDTO) {
                    view.simpleSnack("Showing ${cityName.text}")
                    with(binding) {
                        weatherCondition.text = weatherDTO.fact?.condition.toString()
                        temperature.text = weatherDTO.fact?.temp.toString()
                        feelsLike.text = weatherDTO.fact?.feels_like.toString()
                    }
                    loadingContainer.hide()
                }

                override fun onFailed(throwable: Throwable) {
                    view.simpleSnack("Failed: ${throwable.message}")
                    Log.d("WeatherLoader", throwable.message.toString())
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}