package de.eso.weather

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

/**
 * This ContentProvider is started as the very first ContentProvider in order to initialize Koin and DomainModelBoot.
 * See the description of [WeatherApplication] for more detailed information about the App's startup.
 */
class InitProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        WeatherApplication.setupDmbAndKoin(requireNotNull(context))
        return true
    }

    override fun query(uri: Uri, prj: Array<out String>?, sel: String?, selArgs: Array<out String>?, sort: String?): Cursor? = null
    override fun getType(uri: Uri): String? = null
    override fun insert(uri: Uri, values: ContentValues?): Uri? = null
    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int = 0
}
