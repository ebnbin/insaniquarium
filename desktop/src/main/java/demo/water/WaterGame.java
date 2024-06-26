package demo.water;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class WaterGame extends ApplicationAdapter {

    Stage stage;
    OrthographicCamera camera;

    World world;
    Water water;
    Box2DDebugRenderer debugRenderer;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth() / 240f, Gdx.graphics.getHeight() / 240f, camera));

        // Create box2d world
        world = new World(new Vector2(0, -10), true);
        world.setContactListener(new MyContactListener());
        debugRenderer = new Box2DDebugRenderer();

        water = new Water();
        water.createBody(world, 4.5f, 3f, 8f, 5f); //world, x, y, width, height
        //water.setDebugMode(true);
    }

    @Override
    public void render () {
        // Clean screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // creates a square at the clicked point
        if(Gdx.input.justTouched()){
            createBody();
        }

        world.step(1/60f, 6, 2);

        water.update();

        debugRenderer.render(world, camera.combined);
    }

    private void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;

        // Set our body's starting position in the world
        bodyDef.position.set(Gdx.input.getX() / 240f, camera.viewportHeight - Gdx.input.getY() / 240f);

        // Create our body in the world using our body definition
        Body body = world.createBody(bodyDef);

        // Create a circle shape and set its radius to 6
        PolygonShape square = new PolygonShape();
        square.setAsBox(0.25f, 0.25f);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = square;
        fixtureDef.density = 500f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.5f;

        // Create our fixture and attach it to the body
        body.createFixture(fixtureDef);

        square.dispose();
    }

    @Override
    public void dispose() {
        water.dispose();
        world.dispose();
        debugRenderer.dispose();
    }
}
