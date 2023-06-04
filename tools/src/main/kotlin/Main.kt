import dev.ebnbin.gdx.utils.toJson
import dev.ebnbin.insaniquarium.Config
import dev.ebnbin.insaniquarium.InsaniquariumGame
import java.io.File

fun main() {
    InsaniquariumGame

    ImageHelper.aquarium(
        srcFile = File("../files/Insaniquarium Deluxe/images/aquarium1.jpg"),
        dstFile = File("../assets/texture/aquarium_a.png"),
    )
    ImageHelper.aquarium(
        srcFile = File("../files/Insaniquarium Deluxe/images/aquarium4.jpg"),
        dstFile = File("../assets/texture/aquarium_b.png"),
    )
    ImageHelper.aquarium(
        srcFile = File("../files/Insaniquarium Deluxe/images/aquarium3.jpg"),
        dstFile = File("../assets/texture/aquarium_c.png"),
    )
    ImageHelper.aquarium(
        srcFile = File("../files/Insaniquarium Deluxe/images/aquarium2.jpg"),
        dstFile = File("../assets/texture/aquarium_d.png"),
    )
    ImageHelper.aquarium(
        srcFile = File("../files/Insaniquarium Deluxe/images/aquarium6.jpg"),
        dstFile = File("../assets/texture/aquarium_e.png"),
    )
    ImageHelper.aquarium(
        srcFile = File("../files/Insaniquarium Deluxe/images/Aquarium5.jpg"),
        dstFile = File("../assets/texture/aquarium_f.png"),
    )

    val clydeBodyConfig = ImageHelper.clyde(
        srcFile = File("../files/Insaniquarium Deluxe/images/clyde.gif"),
        srcMaskFile = File("../files/Insaniquarium Deluxe/images/_clyde.gif"),
        dstFile = File("../assets/texture/clyde.png"),
    )
    val config = Config(
        body = mapOf(
            "clyde" to clydeBodyConfig,
        ),
    )
    File("../assets/config.json").writeText(config.toJson())
}
