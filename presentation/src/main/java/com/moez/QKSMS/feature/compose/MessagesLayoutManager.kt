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
package dev.octoshrimpy.quik.feature.compose

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Layout for the message list that keeps D-pad focus on a message taller than the list from
 * jumping to the message's top.
 *
 * By default, focusing such a message scrolls its top into view. Arriving from below, that skips
 * everything under the top, so the end the focus came from is kept on screen instead; once the
 * message fills the list it stays put. MessagesAdapter then scrolls through it with up/down.
 */
class MessagesLayoutManager(context: Context) : LinearLayoutManager(context) {

    init {
        stackFromEnd = true
    }

    override fun requestChildRectangleOnScreen(
        parent: RecyclerView,
        child: View,
        rect: Rect,
        immediate: Boolean,
        focusedChildVisible: Boolean
    ): Boolean {
        val listTop = paddingTop
        val listBottom = height - paddingBottom
        val visibleHeight = listBottom - listTop
        if (rect.height() <= visibleHeight) {
            return super.requestChildRectangleOnScreen(parent, child, rect, immediate, focusedChildVisible)
        }

        // rect is in the child's coordinates
        val rectTop = child.top + rect.top
        val rectBottom = child.top + rect.bottom
        return when {
            // Already fills the list, so stay wherever the user has scrolled to
            rectTop <= listTop && rectBottom >= listBottom -> false

            // Bottom is on screen, so focus arrived from below: bring in only the bottom
            rectBottom <= listBottom -> {
                val bottomSlice = Rect(rect.left, rect.bottom - visibleHeight, rect.right, rect.bottom)
                super.requestChildRectangleOnScreen(parent, child, bottomSlice, immediate, focusedChildVisible)
            }

            else -> super.requestChildRectangleOnScreen(parent, child, rect, immediate, focusedChildVisible)
        }
    }

}
