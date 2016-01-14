package com.nukethemoon.libgdxjam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.Bullet;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;

import java.util.HashMap;
import java.util.Map;

import static com.nukethemoon.libgdxjam.ArtifactDefinitions.ConcreteArtifactType.ATTRIBUTE_FUEL_CAPACITY;
import static com.nukethemoon.libgdxjam.ArtifactDefinitions.ConcreteArtifactType.ATTRIBUTE_INERTIA;
import static com.nukethemoon.libgdxjam.ArtifactDefinitions.ConcreteArtifactType.ATTRIBUTE_LANDSLIDE;
import static com.nukethemoon.libgdxjam.ArtifactDefinitions.ConcreteArtifactType.ATTRIBUTE_LUCK;
import static com.nukethemoon.libgdxjam.ArtifactDefinitions.ConcreteArtifactType.ATTRIBUTE_SCAN_RADIUS;
import static com.nukethemoon.libgdxjam.ArtifactDefinitions.ConcreteArtifactType.ATTRIBUTE_SHIELD_CAPACITY;
import static com.nukethemoon.libgdxjam.ArtifactDefinitions.ConcreteArtifactType.ATTRIBUTE_SPEED;
import static com.nukethemoon.libgdxjam.ArtifactDefinitions.ConcreteArtifactType.OPERATOR_DIVIDE;
import static com.nukethemoon.libgdxjam.ArtifactDefinitions.ConcreteArtifactType.OPERATOR_MINUS;
import static com.nukethemoon.libgdxjam.ArtifactDefinitions.ConcreteArtifactType.OPERATOR_MULTIPLY;
import static com.nukethemoon.libgdxjam.ArtifactDefinitions.ConcreteArtifactType.OPERATOR_PLUS;
import static com.nukethemoon.libgdxjam.ArtifactDefinitions.ConcreteArtifactType.VALUE_100;
import static com.nukethemoon.libgdxjam.ArtifactDefinitions.ConcreteArtifactType.VALUE_1000;
import static com.nukethemoon.libgdxjam.ArtifactDefinitions.ConcreteArtifactType.VALUE_200;
import static com.nukethemoon.libgdxjam.ArtifactDefinitions.ConcreteArtifactType.VALUE_2000;
import static com.nukethemoon.libgdxjam.ArtifactDefinitions.ConcreteArtifactType.VALUE_500;

public class Models {

	public static Model FUEL;
	public static btCollisionShape FUEL_SHAPE;

	public static Model SHIELD;
	public static btCollisionShape SHIELD_SHAPE;

	public static final Map<ArtifactDefinitions.ConcreteArtifactType, Model> ARTIFACT_MODELS
			= new HashMap<ArtifactDefinitions.ConcreteArtifactType, Model>();

	public static Model PLANET_PORTAL;
	public static Model PLANET_PORTAL_TORUS;

	public static btCollisionShape PORTAL_TUBE_COLLISION;
	public static btCollisionShape PORTAL_STAND_COLLISION;
	public static btCollisionShape PORTAL_TRIGGER_COLLISION;

	private static ObjLoader loader;
	private static ObjLoader.ObjLoaderParameters param;

	/**
	 * Needs an initialized com.badlogic.gdx.physics.bullet.Bullet.
	 */

	public static void init() {
		loader = new ObjLoader();
		param = new ObjLoader.ObjLoaderParameters(true);

		BoundingBox boundingBox = new BoundingBox();


		FUEL = loader.loadModel(Gdx.files.internal("models/fuel.obj"), param);
		FUEL.calculateBoundingBox(boundingBox);
		FUEL_SHAPE = new btBoxShape(boundingBox.getDimensions(new Vector3()).scl(0.8f));

		SHIELD = loader.loadModel(Gdx.files.internal("models/shield.obj"), param);
		SHIELD.calculateBoundingBox(boundingBox);
		SHIELD_SHAPE = new btBoxShape(boundingBox.getDimensions(new Vector3()).scl(0.8f));


		ARTIFACT_MODELS.put(ATTRIBUTE_SPEED, 			load("models/artifact_speed.obj"));
		ARTIFACT_MODELS.put(ATTRIBUTE_INERTIA, 			load("models/artifact_inertia.obj"));
		ARTIFACT_MODELS.put(ATTRIBUTE_LANDSLIDE, 		load("models/artifact_landslide.obj"));
		ARTIFACT_MODELS.put(ATTRIBUTE_FUEL_CAPACITY, 	load("models/artifact_fuel.obj"));
		ARTIFACT_MODELS.put(ATTRIBUTE_SHIELD_CAPACITY, 	load("models/artifact_shield.obj"));
		ARTIFACT_MODELS.put(ATTRIBUTE_SCAN_RADIUS, 		load("models/artifact_radius.obj"));
		ARTIFACT_MODELS.put(ATTRIBUTE_LUCK, 			load("models/artifact_luck.obj"));
		ARTIFACT_MODELS.put(OPERATOR_PLUS, 				load("models/artifact_plus.obj"));
		ARTIFACT_MODELS.put(OPERATOR_MINUS, 			load("models/artifact_minus.obj"));
		ARTIFACT_MODELS.put(OPERATOR_MULTIPLY, 			load("models/artifact_multiply.obj"));
		ARTIFACT_MODELS.put(OPERATOR_DIVIDE, 			load("models/artifact_shield.obj"));
		ARTIFACT_MODELS.put(VALUE_100,		 			load("models/artifact_shield.obj"));
		ARTIFACT_MODELS.put(VALUE_200,		 			load("models/artifact_shield.obj"));
		ARTIFACT_MODELS.put(VALUE_500,		 			load("models/artifact_shield.obj"));
		ARTIFACT_MODELS.put(VALUE_1000,		 			load("models/artifact_shield.obj"));
		ARTIFACT_MODELS.put(VALUE_2000,		 			load("models/artifact_shield.obj"));

		PLANET_PORTAL = 		loader.loadModel(Gdx.files.internal("models/planetPortal.obj"), param);
		PLANET_PORTAL_TORUS = 	loader.loadModel(Gdx.files.internal("models/portalTorus.obj"), param);

		PORTAL_STAND_COLLISION = Bullet.obtainStaticNodeShape(PLANET_PORTAL.nodes);

		Model portalTorusCollisionModel = loader.loadModel(Gdx.files.internal("models/planetPortalCollisionTube.obj"), param);
		PORTAL_TUBE_COLLISION = Bullet.obtainStaticNodeShape(portalTorusCollisionModel.nodes);

		Model portalTriggerModel = loader.loadModel(Gdx.files.internal("models/portalTrigger.obj"), param);
		PORTAL_TRIGGER_COLLISION = Bullet.obtainStaticNodeShape(portalTriggerModel.nodes);
	}

	private static Model load(String path) {
		return loader.loadModel(Gdx.files.internal(path), param);
	}

	public static void dispose() {
		FUEL.dispose();
		FUEL_SHAPE.dispose();
		SHIELD.dispose();
		SHIELD_SHAPE.dispose();

		for (Model m : ARTIFACT_MODELS.values()) {
			m.dispose();
		}
	}
}
