package gr.cpaleop.core.presentation.file_viewer

import android.content.Context
import java.io.File

interface FileViewer {

    operator fun invoke(context: Context, uriValue: String)

    operator fun invoke(context: Context, file: File)
}