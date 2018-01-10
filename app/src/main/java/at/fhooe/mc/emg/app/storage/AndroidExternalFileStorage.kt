package at.fhooe.mc.emg.app.storage

import at.fhooe.mc.emg.core.storage.FileStorage
import io.reactivex.Completable
import io.reactivex.Single
import java.io.Serializable

/**
 * @author Martin Macheiner
 * Date: 10.01.2018.
 */

class AndroidExternalFileStorage(private val base: String) : FileStorage {

    override fun <T> loadFromFileAsObject(filename: String): Single<T?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadFromFileAsString(fileName: String): Single<String?> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun storeFile(fileName: String, content: String): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T : Serializable> storeFileAsObject(obj: T, fileName: String): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}