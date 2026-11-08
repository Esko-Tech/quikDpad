/*
 * This file is part of QKSMS.
 *
 * QKSMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * QKSMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with QKSMS.  If not, see <http://www.gnu.org/licenses/>.
 */
package dev.octoshrimpy.quik.common.util

import android.content.Context
import android.content.res.Configuration
import android.preference.PreferenceManager
import dev.octoshrimpy.quik.util.Preferences
import kotlin.math.roundToInt

/**
 * Fits the UI onto screens narrower than the layouts were designed for, such as a 240x320 flip
 * phone at 160dpi, which is only 240dp wide.
 *
 * The layouts assume a smallest width of at least [DESIGN_WIDTH_DP]. On a narrower screen, each
 * activity gets a lower density so the screen behaves as if it were [DESIGN_WIDTH_DP] wide, which
 * shrinks every dp-sized view in proportion. Text shrinks only half as much so it stays readable,
 * and the in-app font size setting still applies on top.
 *
 * Screens that are at least [DESIGN_WIDTH_DP] wide are left untouched, as is everything when the
 * user turns the setting off.
 */
object SmallScreenScaler {

    /** Narrowest smallest-width, in dp, that the layouts are designed for */
    const val DESIGN_WIDTH_DP = 320

    /** True if this device's screen is narrow enough to be scaled, whether or not scaling is on */
    fun isSmallScreen(context: Context): Boolean {
        // The application's resources are never scaled, so they report the real screen width
        return isSmallScreen(context.applicationContext.resources.configuration)
    }

    /**
     * Returns [base] with a scaled-down configuration if the screen is small and scaling is
     * enabled, otherwise returns [base] unchanged. Call from Activity.attachBaseContext.
     */
    fun wrap(base: Context): Context {
        val config = base.resources.configuration
        if (!isSmallScreen(config) || !isEnabled(base)) return base

        val densityDpi = (config.densityDpi * config.smallestScreenWidthDp.toFloat() / DESIGN_WIDTH_DP).roundToInt()
        val scale = densityDpi.toFloat() / config.densityDpi
        val textScale = (1 + scale) / 2

        val scaled = Configuration(config).apply {
            this.densityDpi = densityDpi
            fontScale = config.fontScale * textScale / scale
            screenWidthDp = (config.screenWidthDp / scale).toInt()
            screenHeightDp = (config.screenHeightDp / scale).toInt()
            smallestScreenWidthDp = (config.smallestScreenWidthDp / scale).toInt()
        }
        return base.createConfigurationContext(scaled)
    }

    private fun isSmallScreen(config: Configuration): Boolean {
        val width = config.smallestScreenWidthDp
        return width != Configuration.SMALLEST_SCREEN_WIDTH_DP_UNDEFINED && width < DESIGN_WIDTH_DP
    }

    /**
     * Read straight from the shared preferences that [Preferences] is backed by, because an
     * activity's base context is attached before dependency injection runs
     */
    @Suppress("DEPRECATION")
    private fun isEnabled(context: Context): Boolean {
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(Preferences.SMALL_SCREEN_SCALING, true)
    }

}
