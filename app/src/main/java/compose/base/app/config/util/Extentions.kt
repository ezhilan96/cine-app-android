package compose.base.app.config.util

import android.content.Context
import android.content.pm.ApplicationInfo

fun getAppName(context: Context): String {
    val applicationInfo: ApplicationInfo = context.applicationInfo
    val stringId = applicationInfo.labelRes
    return if (stringId == 0) applicationInfo.nonLocalizedLabel.toString() else context.getString(
        stringId
    )
}