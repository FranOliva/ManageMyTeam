package es.us.managemyteam.util

import android.text.Html
import android.text.Spanned

class HtmlUtil {

    companion object {

        fun fromHtml(html: String?): Spanned? {
            return html?.let {
                Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
            }
        }
    }

}