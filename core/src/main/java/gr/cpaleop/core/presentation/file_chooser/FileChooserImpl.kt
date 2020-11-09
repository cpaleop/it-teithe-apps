package gr.cpaleop.core.presentation.file_chooser

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent

class FileChooserImpl : FileChooser {

    @Throws(ActivityNotFoundException::class)
    override fun invoke(activity: Activity, requestCode: Int, type: String) {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            this.type = type
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        activity.startActivityForResult(
            Intent.createChooser(intent, "Select a File to Upload"),
            requestCode
        )
    }
}