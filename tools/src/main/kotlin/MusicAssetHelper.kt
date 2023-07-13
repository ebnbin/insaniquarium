import dev.ebnbin.gdx.asset.MusicAsset
import java.io.File

object MusicAssetHelper {
    private val srcDir: File = File("files/music")
    val dstDir: File = File("../assets/music")

    fun music(musicInfo: MusicInfo): MusicAsset {
        val srcFile = File(srcDir, musicInfo.srcFileName)
        val dstFile = File(dstDir, "${musicInfo.name}.mp3")
        exec("ffmpeg -i ${srcFile.path} -ab 128k -aq 0 -ar 44100 -ac 2 -acodec libmp3lame ${dstFile.path}")
        return MusicAsset(
            name = musicInfo.name,
        )
    }
}
