package gr.cpaleop.common.extensions

import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.PrecomputedTextCompat
import androidx.core.widget.TextViewCompat

var AppCompatTextView.futureText: CharSequence
    get() {
        return this.text
    }
    set(value) {
        this.setTextFuture(
            PrecomputedTextCompat.getTextFuture(
                value,
                TextViewCompat.getTextMetricsParams(this),
                null
            )
        )
    }