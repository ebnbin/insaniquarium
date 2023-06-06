import dev.ebnbin.insaniquarium.InsaniquariumGame

fun main() {
    InsaniquariumGame

    TextureInfo.aquariumList.forEach {
        ImageHelper.aquarium(it)
    }
    TextureInfo.petList.forEach {
        ImageHelper.pet(it)
    }
}
