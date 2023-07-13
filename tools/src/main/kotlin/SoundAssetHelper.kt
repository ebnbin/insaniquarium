import dev.ebnbin.gdx.asset.SoundAsset
import java.io.File

object SoundAssetHelper {
    private val srcDir: File = File("files/sound")
    val dstDir: File = File("../assets/sound")

    fun sound(soundInfo: SoundInfo): SoundAsset {
        val srcFile = File(srcDir, soundInfo.srcFileName)
        val dstFile = File(dstDir, "${soundInfo.name}.mp3")
        exec("ffmpeg -i ${srcFile.path} -ab 128k -aq 0 -ar 44100 -ac 2 -acodec libmp3lame ${dstFile.path}")
        return SoundAsset(
            name = soundInfo.name,
        )
    }
}
