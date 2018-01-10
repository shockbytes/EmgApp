package at.fhooe.mc.emg.app.ui.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import at.fhooe.mc.emg.app.R

/**
 * @author Martin Macheiner
 * Date: 10.01.2018.
 */

class PickFilesDialogFragment : DialogFragment() {

    private var enterListener: ((String) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val files = arguments.getStringArrayList(ARG_FILES)

        return AlertDialog.Builder(context)
                .setNegativeButton(android.R.string.cancel) { _, _ -> dismiss() }
                .setIcon(R.drawable.ic_file)
                .setItems(files.toTypedArray()) { _, idx ->
                    enterListener?.invoke(files[idx])
                    dismiss()
                }
                .setTitle(R.string.dialog_pick_file)
                .create()
    }

    fun setOnFilePickedListener(listener: (String) -> Unit): PickFilesDialogFragment {
        this.enterListener = listener
        return this
    }

    companion object {

        const val ARG_FILES = "arg_files"

        fun newInstance(files: List<String>): PickFilesDialogFragment {
            val fragment = PickFilesDialogFragment()
            val args = Bundle()
            args.putStringArrayList(ARG_FILES, ArrayList(files))
            fragment.arguments = args
            return fragment
        }

    }


}