package at.fhooe.mc.emg.app.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import at.fhooe.mc.emg.app.R
import at.fhooe.mc.emg.core.tool.conconi.ConconiRoundData
import at.shockbytes.util.adapter.BaseAdapter
import kotterknife.bindView

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

        private val txtSpeed: TextView by bindView(R.id.item_conconi_txt_speed)
        private val txtAvg: TextView by bindView(R.id.item_conconi_txt_avg)
        private val txtPeaks: TextView by bindView(R.id.item_conconi_txt_peaks)

        override fun bind(t: ConconiRoundData) {
            content = t

            txtSpeed.text = context.getString(R.string.tool_conconi_round_data_speed,
                    String.format("%.01f", t.speed))
            txtAvg.text = context.getString(R.string.tool_conconi_round_data_rms,
                    String.format("%.02f", t.rms))
            txtPeaks.text = context.getString(R.string.tool_conconi_round_data_peaks, t.peaks)
        }

    }


}