package at.fhooe.mc.emg.app.storage

import at.fhooe.mc.emg.core.storage.FileStorage
import at.fhooe.mc.emg.core.util.CoreUtils
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.Serializable
import java.nio.charset.Charset

/**
 * @author Martin Macheiner
 * Date: 10.01.2018.
 */

class AndroidExternalFileStorage(override val baseDirectory: String) : FileStorage {

    override fun listFiles(directory: String, concatToBase: Boolean,
                           fileType: String?): Single<List<String>?> {
        return Single.fromCallable {
            val f = if (concatToBase) File("$baseDirectory/$directory") else File(directory)
            if (f.exists()) {
                f.list { _, name -> if(fileType != null) name.endsWith(fileType) else true }.toList()
            } else {
                arrayListOf()
            }
        }.subscribeOn(Schedulers.io())
    }

    override fun storeFile(fileName: String, content: String): Completable {
        return Completable.fromAction {
            CoreUtils.writeFile(File("$baseDirectory/$fileName"), content)
        }.subscribeOn(Schedulers.io())
    }

    override fun <T : Serializable> storeFileAsObject(obj: T, fileName: String): Completable {
        return Completable.fromAction {
            CoreUtils.serializeToFile(obj, "$baseDirectory/$fileName")
        }.subscribeOn(Schedulers.io())
    }

    override fun loadFromFileAsString(fileName: String): Single<String?> {
        return Single.fromCallable {
            FileUtils.readFileToString(File("$baseDirectory/$fileName"),
                    Charset.forName("UTF-8"))
        }.subscribeOn(Schedulers.io())
    }

    override fun <T> loadFromFileAsObject(filename: String): Single<T?> {
        return Single.fromCallable {
            CoreUtils.unsafeDeserializeFromFile<T>("$baseDirectory/$filename")
        }.subscribeOn(Schedulers.io())
    }

}