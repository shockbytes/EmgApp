package at.fhooe.mc.emg.app.ui.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
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

    private var txt: EditText? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val title = arguments.getString(ARG_TITLE)
        val icon = arguments.getInt(ARG_ICON)
        val numOnly = arguments.getBoolean(ARG_NUM_ONLY)

        return AlertDialog.Builder(context)
                .setPositiveButton(R.string.enter) { _, _ -> listener?.onTextEntered(txt?.text.toString()) }
                .setNegativeButton(android.R.string.cancel) { _, _ -> dismiss() }
                .setIcon(icon)
                .setView(buildView(numOnly))
                .setTitle(title)
                .create()

    }

    private fun buildView(numOnly: Boolean): View {
        val v = LayoutInflater.from(context)
                .inflate(R.layout.dialogfragment_textenter, null, false)
        txt = v.findViewById(R.id.dialogfragment_textenter_txt)
        txt?.inputType = if (numOnly) InputType.TYPE_CLASS_NUMBER else InputType.TYPE_CLASS_TEXT
        return v
    }

    companion object {

        private val ARG_TITLE = "arg_title"
        private val ARG_ICON = "arg_icon"
        private val ARG_NUM_ONLY = "arg_num_only"

        fun newInstance(title: String, icon: Int = R.mipmap.ic_launcher,
                        numbersOnly: Boolean = false): TextEnterDialogFragment {
            val fragment = TextEnterDialogFragment()
            val args = Bundle()
            args.putString(ARG_TITLE, title)
            args.putInt(ARG_ICON, icon)
            args.putBoolean(ARG_NUM_ONLY, numbersOnly)
            fragment.arguments = args
            return fragment
        }

    }
}