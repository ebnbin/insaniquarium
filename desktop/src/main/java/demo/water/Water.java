package demo.water;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.math.GeometryUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Disposable;
import kotlin.Pair;

/**
 * Allows to create an object to simulate the behavior of water in interaction with other bodies
 */
public class Water implements Disposable {

    private Set<Pair<Fixture, Fixture>> fixturePairs; // contacts between this object and other dynamic bodies
    private Body body; // Box2d body

    private float density = 1000f;

    /**
     * Constructor that allows to specify if there is an effect of waves and splash particles.
     */
    public Water() {

        this.fixturePairs = new HashSet<Pair<Fixture, Fixture>>();
    }

    /**
     * Creates the body of the water. It will be a square sensor in a specific box2d world.
     * @param world Our box2d world
     * @param x Position of the x coordinate of the center of the body
     * @param y Position of the y coordinate of the center of the body
     * @param width Body width
     * @param height Body height
     */
    public void createBody(World world, float x, float y, float width, float height) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        bodyDef.position.set(x, y);

        // Create our body in the world using our body definition
        body = world.createBody(bodyDef);
        body.setUserData(this);

        PolygonShape square = new PolygonShape();
        square.setAsBox(width / 2, height / 2);

        // Create a fixture definition to apply our shape to
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = square;

        // Must be a sensor
        fixtureDef.isSensor = true;

        // Create our fixture and attach it to the body
        body.createFixture(fixtureDef);

        square.dispose();
    }

    /**
     * Updates the position of bodies in contact with water. To do this, it applies a force that counteracts
     * gravity by calculating the area in contact, centroid and force required.
     */
    public void update() {
        if (body != null && fixturePairs != null) {
            World world = body.getWorld();
            for (Pair<Fixture, Fixture> pair : fixturePairs) {

                Fixture fixtureA = pair.getFirst();
                Fixture fixtureB = pair.getSecond();

                List<Vector2> clippedPolygon = new ArrayList<Vector2>();
                if (IntersectionUtils.findIntersectionOfFixtures(fixtureA, fixtureB, clippedPolygon)) {

                    // find centroid and area
                    Polygon interPolygon = IntersectionUtils.getIntersectionPolygon(clippedPolygon);
                    Vector2 centroid = new Vector2();
                    GeometryUtils.polygonCentroid(interPolygon.getVertices(), 0, interPolygon.getVertices().length, centroid);
                    float area = interPolygon.area();

                    /* Get fixtures bodies */
                    Body fluidBody = fixtureA.getBody();
                    Body fixtureBody = fixtureB.getBody();

                    // apply buoyancy force (fixtureA is the fluid)
                    float displacedMass = this.density * area;
                    Vector2 buoyancyForce = new Vector2(displacedMass * -world.getGravity().x,
                            displacedMass * -world.getGravity().y);
                    fixtureB.getBody().applyForce(buoyancyForce, centroid, true);

                    float dragMod = 0.25f; // adjust as desired
                    float liftMod = 0.25f; // adjust as desired
                    float maxDrag = 2000; // adjust as desired
                    float maxLift = 500; // adjust as desired

                    /* Apply drag and lift forces */
                    int polygonVertices = clippedPolygon.size();
                    for (int i = 0; i < polygonVertices; i++) {

                        /* End points and mid point of the edge */
                        Vector2 firstPoint = clippedPolygon.get(i);
                        Vector2 secondPoint = clippedPolygon.get((i + 1) % polygonVertices);
                        Vector2 midPoint = firstPoint.cpy().add(secondPoint).scl(0.5f);

                        /*
                         * Find relative velocity between the object and the fluid at edge
                         * mid point.
                         */
                        Vector2 velocityDirection = new Vector2(fixtureBody
                                .getLinearVelocityFromWorldPoint(midPoint)
                                .sub(fluidBody.getLinearVelocityFromWorldPoint(midPoint)));
                        float velocity = velocityDirection.len();
                        velocityDirection.nor();

                        Vector2 edge = secondPoint.cpy().sub(firstPoint);
                        float edgeLength = edge.len();
                        edge.nor();

                        Vector2 normal = new Vector2(edge.y, -edge.x);
                        float dragDot = normal.dot(velocityDirection);

                        if (dragDot >= 0) {

                            /*
                             * Normal don't point backwards. This is a leading edge. Store
                             * the result of multiply edgeLength, density and velocity
                             * squared
                             */
                            float tempProduct = edgeLength * density * velocity * velocity;

                            float drag = dragDot * dragMod * tempProduct;
                            drag = Math.min(drag, maxDrag);
                            Vector2 dragForce = velocityDirection.cpy().scl(-drag);
                            fixtureBody.applyForce(dragForce, midPoint, true);

                            /* Apply lift force */
                            float liftDot = edge.dot(velocityDirection);
                            float lift = dragDot * liftDot * liftMod * tempProduct;
                            lift = Math.min(lift, maxLift);
                            Vector2 liftDirection = new Vector2(-velocityDirection.y,
                                    velocityDirection.x);
                            Vector2 liftForce = liftDirection.scl(lift);
                            fixtureBody.applyForce(liftForce, midPoint, true);


                            fixtureBody.applyTorque(-fixtureBody.getAngularVelocity()/100, true);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void dispose() {
        if(fixturePairs != null) fixturePairs.clear();
        if(body != null) body.getWorld().destroyBody(body);
    }

    public Set<Pair<Fixture, Fixture>> getFixturePairs() {
        return fixturePairs;
    }

}
