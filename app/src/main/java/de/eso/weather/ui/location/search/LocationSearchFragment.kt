package de.eso.weather.ui.location.search

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import de.eso.weather.R
import de.eso.weather.databinding.FragmentLocationSearchBinding
import de.eso.weather.domain.shared.api.Location
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class LocationSearchFragment : Fragment(R.layout.fragment_location_search),
    SearchView.OnQueryTextListener {

    private var viewBinding: FragmentLocationSearchBinding? = null

    private val editLocationsAdapter = LocationSearchAdapter(::onLocationSelected)
    private val viewModel: LocationSearchViewModel by sharedViewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? AppCompatActivity)?.supportActionBar?.apply {
            setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel)
            setDisplayHomeAsUpEnabled(true)
            setTitle(R.string.location_search_title)
        }

        viewBinding = FragmentLocationSearchBinding.bind(view)

        with(requireNotNull(viewBinding)) {
            locationsSearchView.setOnQueryTextListener(this@LocationSearchFragment)
            locationsSearchList.adapter = editLocationsAdapter
        }

        viewModel.locationsResult.observe(viewLifecycleOwner, ::onLocationsChanged)
    }

    private fun onLocationsChanged(locations: List<Location>) {
        editLocationsAdapter.submitList(locations)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        viewModel.onTextSubmit(query)
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        viewModel.onTextSubmit(newText)
        return true
    }

    private fun onLocationSelected(location: Location) {
        viewModel.onLocationSelected(location)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }
}
