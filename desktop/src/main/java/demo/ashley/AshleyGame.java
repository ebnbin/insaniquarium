package demo.ashley;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import demo.ashley.components.MovementComponent;
import demo.ashley.components.PositionComponent;
import demo.ashley.components.VisualComponent;
import demo.ashley.systems.MovementSystem;
import demo.ashley.systems.RenderSystem;
import dev.ebnbin.kgdx.util.FileUtilKt;
import dev.ebnbin.kgdx.util.PixmapUtilKt;
import dev.ebnbin.kgdx.util.ZipUtilKt;

public class AshleyGame extends ApplicationAdapter {
    PooledEngine engine;

    @Override
    public void create() {
        OrthographicCamera camera = new OrthographicCamera(640, 480);
        camera.position.set(320, 240, 0);
        camera.update();

        FileHandle zipFileHandle = FileUtilKt.internalAsset(Gdx.files, "Insaniquarium Deluxe.zip");
        Pixmap aPixmap =
                PixmapUtilKt.createPixmap(ZipUtilKt.readByteArrayFromZip(zipFileHandle, "images/helppetchoose.gif"));
        Pixmap bPixmap =
                PixmapUtilKt.createPixmap(ZipUtilKt.readByteArrayFromZip(zipFileHandle, "images/helpalien.gif"));
        Texture crateTexture = new Texture(aPixmap);
        Texture coinTexture = new Texture(bPixmap);

        engine = new PooledEngine();
        engine.addSystem(new RenderSystem(camera));
        engine.addSystem(new MovementSystem());

        Entity crate = engine.createEntity();
        crate.add(new PositionComponent(50, 50));
        crate.add(new VisualComponent(new TextureRegion(crateTexture)));

        engine.addEntity(crate);

        TextureRegion coinRegion = new TextureRegion(coinTexture);

        for (int i = 0; i < 100; i++) {
            Entity coin = engine.createEntity();
            coin.add(new PositionComponent(MathUtils.random(640), MathUtils.random(480)));
            coin.add(new MovementComponent(10.0f, 10.0f));
            coin.add(new VisualComponent(coinRegion));
            engine.addEntity(coin);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        engine.update(Gdx.graphics.getDeltaTime());
    }
}
