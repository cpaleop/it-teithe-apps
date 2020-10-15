package gr.cpaleop.common.extensions

import android.animation.Animator
import android.view.ViewPropertyAnimator

fun ViewPropertyAnimator.setEndListener(listener: (Animator?) -> Unit): ViewPropertyAnimator {

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

fun ViewPropertyAnimator.setStartListener(listener: (Animator?) -> Unit): ViewPropertyAnimator {

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