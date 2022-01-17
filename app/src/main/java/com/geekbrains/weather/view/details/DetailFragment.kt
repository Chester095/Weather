package com.geekbrains.weather.view.details

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.geekbrains.weather.R
import com.geekbrains.weather.databinding.DetailFragmentBinding
import com.geekbrains.weather.model.*

class DetailFragment : Fragment() {
    // фабричный статический метод
    companion object {
        fun newInstance(bundle: Bundle): DetailFragment {
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
    ): View? {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getParcelable<Weather>(getString(R.string.weather_extra))?.let { weather: Weather ->

            binding.cityName.text = weather.city.name
            binding.cityCoordinates.text = "${weather.city.lat} ${weather.city.lon}"

            //запускаем сервисы
            requireActivity().startService(Intent(requireContext(),MainIntentService::class.java).apply {
                // кладём туда данные
                putExtra(getString(R.string.weather_extra), weather)

            })


/*            WeatherLoader.load(weather.city, object : WeatherLoader.OnWeatherLoadListener {
                override fun onLoaded(weatherDTO: WeatherDTO) {
                    weatherDTO.fact?.let { fact ->
                        binding.weatherCondition.text = fact.condition
                        binding.temperatureValue.text = fact.temp?.toString()
                        binding.feelsLikeValue.text = fact.feelsLike?.toString()
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    Log.e("DEBUGLOG", throwable.message.toString())
                    binding.root.showSnackBar("Сбой загрузки данных", "Попробовать снова", {
                        //запросили новые данные
                        WeatherLoader.load(weather.city, this)
                    })
//                    Toast.makeText(requireContext(), throwable.message, Toast.LENGTH_LONG).show()
                }
            })*/
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}