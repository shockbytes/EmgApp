package at.fhooe.mc.emg.app.ui.fragment.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.design.widget.TextInputLayout
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import at.fhooe.mc.emg.app.R

/**
 * @author Martin Macheiner
 * Date: 02.12.2017.
 */
class TextEnterDialogFragment : DialogFragment() {

    interface OnTextEnteredListener {

        fun onTextEntered(text: String)
    }

    var listener: OnTextEnteredListener? = null

    private var txt: TextInputEditText? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val title = arguments.getString(ARG_TITLE)
        val icon = arguments.getInt(ARG_ICON)
        val numOnly = arguments.getBoolean(ARG_NUM_ONLY)
        val hint = arguments.getString(ARG_HINT)

        return AlertDialog.Builder(context)
                .setPositiveButton(R.string.enter) { _, _ -> listener?.onTextEntered(txt?.text.toString()) }
                .setNegativeButton(android.R.string.cancel) { _, _ -> dismiss() }
                .setIcon(icon)
                .setView(buildView(numOnly, hint))
                .setTitle(title)
                .create()

    }

    @SuppressLint("InflateParams")
    private fun buildView(numOnly: Boolean, hint: String): View {
        val v = LayoutInflater.from(context)
                .inflate(R.layout.dialogfragment_textenter, null, false)
        txt = v.findViewById(R.id.dialogfragment_textenter_txt)
        (v as TextInputLayout).hint = hint
        txt?.inputType = if (numOnly) InputType.TYPE_CLASS_NUMBER else InputType.TYPE_CLASS_TEXT
        return v
    }

    companion object {

        private val ARG_TITLE = "arg_title"
        private val ARG_ICON = "arg_icon"
        private val ARG_NUM_ONLY = "arg_num_only"
        private val ARG_HINT = "arg_hint"

        fun newInstance(title: String, icon: Int = R.mipmap.ic_launcher,
                        numbersOnly: Boolean = false, hint: String): TextEnterDialogFragment {
            val fragment = TextEnterDialogFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putInt(ARG_ICON, icon)
            args.putBoolean(ARG_NUM_ONLY, numbersOnly)
            args.putString(ARG_HINT, hint)
            fragment.arguments = args
            return fragment
        }

    }
}