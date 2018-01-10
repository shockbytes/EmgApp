package at.fhooe.mc.emg.app.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.core.tools.conconi.ConconiRoundData
import at.shockbytes.util.adapter.BaseAdapter

/**
 * @author Martin Macheiner
 * Date: 09.01.2018.
 */

class ConconiRoundDataAdapter(c: Context,
                              d: MutableList<ConconiRoundData>) : BaseAdapter<ConconiRoundData>(c, d) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_conconi_test, parent, false))
    }

    inner class ViewHolder(itemView: View) : BaseAdapter<ConconiRoundData>.ViewHolder(itemView) {

        override fun bind(t: ConconiRoundData) {
            // TODO
        }

    }


}