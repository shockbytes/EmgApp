package at.fhooe.mc.emg.app.ui.fragment

import at.fhooe.mc.emg.app.R

/**
 * @author Martin Macheiner
 * Date: 08.01.2018.
 */

abstract class AndroidToolViewFragment : BaseFragment() {

    fun showToolView(tag: String) {
        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.main_container, this, tag)
                .addToBackStack(null)
                .commit()
    }

}
