package de.eso.weather.ui.shared.location

import android.os.Parcel
import android.os.Parcelable

/**
 * Use to make sure that parcelables work when shrinking with R8 is enabled
 */
data class LocationResult(val id: String, val name: String) : Parcelable {
    constructor(parcel: Parcel) : this(
        requireNotNull(parcel.readString()),
        requireNotNull(parcel.readString())
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<LocationResult> {
        override fun createFromParcel(parcel: Parcel) = LocationResult(parcel)

        override fun newArray(size: Int): Array<LocationResult?> = arrayOfNulls(size)
    }
}
