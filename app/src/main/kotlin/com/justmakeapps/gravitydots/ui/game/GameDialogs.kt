package com.justmakeapps.gravitydots.ui.game

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.justmakeapps.gravitydots.R

// ── Level Complete ────────────────────────────────────────────────────────────

class LevelCompleteDialog : DialogFragment() {

    var onWatchAd: (() -> Unit)? = null
    var onNextLevel: (() -> Unit)? = null
    var onRetry: (() -> Unit)? = null

    companion object {
        fun newInstance(level: Int, stars: Int, hasNextLevel: Boolean) =
            LevelCompleteDialog().apply {
                arguments = bundleOf("level" to level, "stars" to stars, "hasNext" to hasNextLevel)
            }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.dialog_level_complete, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val stars = arguments?.getInt("stars", 1) ?: 1
        val hasNext = arguments?.getBoolean("hasNext", false) ?: false

        view.findViewById<TextView>(R.id.tv_complete_title).text =
            "Level ${arguments?.getInt("level")} done!"

        listOf(R.id.star1, R.id.star2, R.id.star3).forEachIndexed { index, id ->
            view.findViewById<ImageView>(id).alpha = if (index < stars) 1f else 0.2f
        }

        view.findViewById<View>(R.id.btn_watch_ad).setOnClickListener {
            dismiss()
            onWatchAd?.invoke()
        }

        view.findViewById<View>(R.id.btn_next).apply {
            isEnabled = hasNext
            alpha = if (hasNext) 1f else 0.4f
            setOnClickListener { dismiss(); onNextLevel?.invoke() }
        }

        view.findViewById<View>(R.id.btn_retry_complete).setOnClickListener {
            dismiss(); onRetry?.invoke()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.BOTTOM)
        }
    }
}

// ── Level Failed ──────────────────────────────────────────────────────────────

class LevelFailedDialog : DialogFragment() {

    var onWatchAd: (() -> Unit)? = null
    var onRetry: (() -> Unit)? = null

    companion object {
        fun newInstance(level: Int) = LevelFailedDialog().apply {
            arguments = bundleOf("level" to level)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.dialog_level_failed, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<View>(R.id.btn_watch_ad_continue).setOnClickListener {
            dismiss(); onWatchAd?.invoke()
        }
        view.findViewById<View>(R.id.btn_retry).setOnClickListener {
            dismiss(); onRetry?.invoke()
        }
        view.findViewById<View>(R.id.btn_menu).setOnClickListener {
            dismiss(); parentFragmentManager.popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.BOTTOM)
        }
    }
}

// ── Out of Walls ──────────────────────────────────────────────────────────────

class OutOfWallsDialog : DialogFragment() {

    var onWatchAd: (() -> Unit)? = null
    var onRestart: (() -> Unit)? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.dialog_out_of_walls, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<View>(R.id.btn_watch_ad_walls).setOnClickListener {
            dismiss(); onWatchAd?.invoke()
        }
        view.findViewById<View>(R.id.btn_restart_walls).setOnClickListener {
            dismiss(); onRestart?.invoke()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setGravity(Gravity.BOTTOM)
        }
    }
}
