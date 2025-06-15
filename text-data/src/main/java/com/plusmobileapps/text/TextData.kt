package com.plusmobileapps.text

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import java.util.Locale

/**
 * Represents a generic text to be evaluated with a [Context] in the UI layer.
 */
sealed class TextData {
    abstract fun evaluate(context: Context): String
}


/**
 * A text data model that represents a fixed string.
 */
data class FixedString(val value: String) : TextData() {
    override fun evaluate(context: Context): String = value
}


/**
 * A text data model that represents a string resource that does not require any
 * formatting or placeholders.
 */
data class ResourceString(@StringRes val resource: Int) : TextData() {
    override fun evaluate(context: Context): String = context.getString(resource)
}


/**
 * A text data model that represents a phrase with placeholders.
 *
 * Usage example:
 *
 * <string name="greeting">Hello {name}, welcome to {place}!</string>
 *
 * ```kotlin
 * PhraseModel(
 *     resource = R.string.greeting,
 *     "name" to FixedString("Alice"),
 *     "place" to ResourceString(R.string.wonderland)
 * )
 * ```
 */
data class PhraseModel(
    @StringRes val resource: Int,
    val args: Map<String, TextData> = emptyMap()
) : TextData() {

    constructor(
        @StringRes resource: Int,
        vararg args: Pair<String, TextData>
    ) : this(
        resource = resource,
        args = args.toMap()
    )

    override fun evaluate(context: Context): String {
        val string = context.getString(resource)
        if (args.isEmpty()) {
            return string
        }

        var resultString = string
        args.forEach { (key, textData) ->
            val placeholder = "{$key}"
            val value = textData.evaluate(context)
            resultString = resultString.replace(placeholder, value)
        }

        return resultString
    }
}

/**
 * A text data model that represents a localized string resource.
 *
 * This is useful for cases where you want to evaluate a string resource in a specific locale that
 * is different from the device's current locale.
 */
data class LocaleTextData(
    val local: Locale,
    val textData: TextData,
) : TextData() {
    override fun evaluate(context: Context): String {
        // Create a configuration with the provided locale
        val config = Configuration(context.resources.configuration)
        config.setLocale(local)

        // Create a new context with the updated configuration
        val localizedContext = context.createConfigurationContext(config)

        // Evaluate the textData with the localized context
        return textData.evaluate(localizedContext)
    }
}