package de.eso.weather.ui.location.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import de.eso.weather.databinding.LocationListItemBinding
import de.eso.weather.domain.shared.api.Location
import de.eso.weather.ui.shared.location.LocationListItemCallback

class LocationSearchAdapter(
    private val clickListener: (location: Location) -> Unit
) : ListAdapter<Location, LocationSearchAdapter.LocationSearchViewHolder>(LocationListItemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LocationSearchViewHolder(
            LocationListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: LocationSearchViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class LocationSearchViewHolder(private val viewBinding: LocationListItemBinding) : RecyclerView.ViewHolder(viewBinding.root) {
        fun bind(location: Location, clickListener: (location: Location) -> Unit) {
            viewBinding.locationListItemName.text = location.name
            viewBinding.root.setOnClickListener { clickListener(location) }
        }
    }
}
