package es.us.managemyteam.util

import android.os.Build
import android.text.Html
import android.text.Spanned

class HtmlUtil {

    companion object {
        fun htmlToPlainText(html: String?): String? {
            return html?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY).toString()
                } else {
                    Html.fromHtml(it).toString()
                }
            }
        }

        fun fromHtml(html: String?): Spanned? {
            return html?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(it, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    Html.fromHtml(it)
                }
            }
        }
    }

}