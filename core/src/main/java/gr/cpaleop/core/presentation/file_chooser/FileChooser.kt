package gr.cpaleop.core.presentation.file_chooser

import android.app.Activity

interface FileChooser {

    operator fun invoke(activity: Activity, requestCode: Int, type: String = "*/*")
}