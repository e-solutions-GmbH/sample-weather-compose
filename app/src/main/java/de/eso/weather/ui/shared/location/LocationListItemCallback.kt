package de.eso.weather.ui.shared.location

import androidx.recyclerview.widget.DiffUtil
import de.eso.weather.domain.shared.api.Location

object LocationListItemCallback : DiffUtil.ItemCallback<Location>() {
    override fun areItemsTheSame(oldItem: Location, newItem: Location) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Location, newItem: Location) =
        oldItem == newItem
}
