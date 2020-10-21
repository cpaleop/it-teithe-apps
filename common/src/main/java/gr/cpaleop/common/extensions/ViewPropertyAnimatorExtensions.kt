package gr.cpaleop.common.extensions

import android.animation.Animator
import android.view.ViewPropertyAnimator

inline fun ViewPropertyAnimator.setEndListener(crossinline listener: (Animator?) -> Unit): ViewPropertyAnimator {

    this.setListener(object : Animator.AnimatorListener {

        override fun onAnimationRepeat(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {
            listener(animation)
        }

        override fun onAnimationCancel(animation: Animator?) {

        }

        override fun onAnimationStart(animation: Animator?) {

        }
    })
    return this
}

inline fun ViewPropertyAnimator.setStartListener(crossinline listener: (Animator?) -> Unit): ViewPropertyAnimator {

    this.setListener(object : Animator.AnimatorListener {

        override fun onAnimationRepeat(animation: Animator?) {

        }

        override fun onAnimationEnd(animation: Animator?) {

        }

        override fun onAnimationCancel(animation: Animator?) {

        }

        override fun onAnimationStart(animation: Animator?) {
            listener(animation)
        }
    })
    return this
}