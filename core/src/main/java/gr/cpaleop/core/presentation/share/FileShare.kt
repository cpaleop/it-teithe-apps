package gr.cpaleop.core.presentation.share

import android.content.Context
import java.io.File

interface FileShare {

    operator fun invoke(context: Context, uriValue: String)

    operator fun invoke(context: Context, file: File)
}