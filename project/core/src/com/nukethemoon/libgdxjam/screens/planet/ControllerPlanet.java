package com.nukethemoon.libgdxjam.screens.planet;

import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.nukethemoon.libgdxjam.Log;
import com.nukethemoon.libgdxjam.screens.planet.gameobjects.PlanetPart;
import com.nukethemoon.tools.opusproto.generator.ChunkListener;
import com.nukethemoon.tools.opusproto.generator.Opus;
import com.nukethemoon.tools.opusproto.loader.json.OpusLoaderJson;
import com.nukethemoon.tools.opusproto.region.Chunk;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ControllerPlanet implements ChunkListener, Disposable {

	private static final String WORLD_NAME = "entities/planets/planet01/opusConfig.json";


	private float tileGraphicSize = 2f;

	private int requestRadiusInTiles = 200;
	private int lastRequestCenterTileX = 0;
	private int lastRequestCenterTileY = 0;
	private long requestCount = 0;

	private Opus opus;

	private List<Point> tmpRequestList = new ArrayList<Point>();
	private List<Point> tmpRemoveList = new ArrayList<Point>();

	private List<Point> currentVisibleChunkPositions = new ArrayList<Point>();

	private Map<Point, PlanetPart> chunkGraphicBuffer = new HashMap<Point, PlanetPart>();


	private Vector2 tmpVector1 = new Vector2();
	private Vector2 tmpVector2 = new Vector2();

	private int chunkBufferSize;
	private PlanetConfig planetConfig;
	private ControllerPhysic controllerPhysic;

	public ControllerPlanet(int worldIndex, PlanetConfig pPlanetConfig, ControllerPhysic controllerPhysic) {
		this.planetConfig = pPlanetConfig;
		this.controllerPhysic = controllerPhysic;
		String worldName = String.format(WORLD_NAME, worldIndex);

		OpusLoaderJson loader = new OpusLoaderJson();
		try {
			// load opus by a json file
			opus = loader.load(worldName);
			com.nukethemoon.tools.opusproto.log.Log.logLevel = com.nukethemoon.tools.opusproto.log.Log.LogType.Error;

			chunkBufferSize = (requestRadiusInTiles / opus.getConfig().chunkSize) * 2;

			// add a callback to receive chunks
			opus.addChunkListener(this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void requestChunks(List<Point> chunkCoordinates) {

		List<Point> requestList = new ArrayList<Point>();
		for (Point coordinate : chunkCoordinates) {
			if (chunkGraphicBuffer.get(coordinate) == null) {
				requestList.add(coordinate);
			}
		}

		if (requestList.size() > 0) {
			int[] xCoordinates = new int[requestList.size()];
			int[] yCoordinates = new int[requestList.size()];
			for (int i = 0; i < requestList.size(); i++) {
				xCoordinates[i] = requestList.get(i).x;
				yCoordinates[i] = requestList.get(i).y;
			}

			try {
				opus.requestChunks(xCoordinates, yCoordinates);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

		}
	}



	public void updateRequestCenter(float graphicX, float graphicY) {
		int requestCenterTileX = (int) (Math.floor(graphicX) / tileGraphicSize);
		int requestCenterTileY = (int) (Math.floor(graphicY) / tileGraphicSize);

		if (lastRequestCenterTileX == requestCenterTileX && lastRequestCenterTileY == requestCenterTileY && requestCount < 0) {
			// return if requested the same tile again
			return;
		}

		requestCount++;
		// buffer the last request position
		lastRequestCenterTileX = requestCenterTileX;
		lastRequestCenterTileY = requestCenterTileY;

		tmpVector2.set(requestCenterTileX, requestCenterTileY);

		int chunkSize = opus.getConfig().chunkSize - 1;
		int chunkBufferCenterX = (int) Math.floor(requestCenterTileX / chunkSize);
		int chunkBufferCenterY = (int) Math.floor(requestCenterTileY / chunkSize);


		currentVisibleChunkPositions.clear();

		// find chunks that are in the view radius
		for (int chunkIndexX = 0; chunkIndexX < chunkBufferSize; chunkIndexX++) {
			for (int chunkIndexY = 0; chunkIndexY < chunkBufferSize; chunkIndexY++) {
				boolean isInRadius = false;
				int currentChunkX = chunkBufferCenterX + chunkIndexX - (chunkBufferSize / 2);
				int currentChunkY = chunkBufferCenterY + chunkIndexY - (chunkBufferSize / 2);
				// chunk tile corner 1
				tmpVector1.set(currentChunkX * chunkSize, currentChunkY * chunkSize);
				if (Math.abs(tmpVector1.dst(tmpVector2)) < requestRadiusInTiles) {
					isInRadius = true;
				}
				// chunk tile corner 2
				tmpVector1.set(currentChunkX * chunkSize, currentChunkY * chunkSize + chunkSize);
				if (Math.abs(tmpVector1.dst(tmpVector2)) < requestRadiusInTiles) {
					isInRadius = true;
				}
				// chunk tile corner 3
				tmpVector1.set(currentChunkX * chunkSize + chunkSize, currentChunkY * chunkSize + chunkSize);
				if (Math.abs(tmpVector1.dst(tmpVector2)) < requestRadiusInTiles) {
					isInRadius = true;
				}
				// chunk tile corner 4
				tmpVector1.set(currentChunkX * chunkSize + chunkSize, currentChunkY * chunkSize);
				if (Math.abs(tmpVector1.dst(tmpVector2)) < requestRadiusInTiles) {
					isInRadius = true;
				}
				if (isInRadius) {
					currentVisibleChunkPositions.add(new Point(currentChunkX, currentChunkY));
				}
			}
		}

		tmpRequestList.clear();
		tmpRemoveList.clear();


		// remove non visible chunks
		for (Map.Entry<Point, PlanetPart> entry : chunkGraphicBuffer.entrySet()) {
			if (!currentVisibleChunkPositions.contains(entry.getKey())) {
				PlanetPart c = entry.getValue();
				controllerPhysic.removeRigidBody(c.getRigidBody());
				c.dispose();
				tmpRemoveList.add(entry.getKey());
			}
		}
		for (Point p : tmpRemoveList) {
			chunkGraphicBuffer.remove(p);
		}

		// add chunks that are not already loaded
		for (Point p : currentVisibleChunkPositions) {
			if (chunkGraphicBuffer.get(p) == null) {
				tmpRequestList.add(p);
			}
		}

		// request visible chunks
		if (tmpRequestList.size() > 0) {
			requestChunks(tmpRequestList);
		}
	}

	@Override
	public void onChunkCreated(int x, int y, Chunk chunk) {
		Point point = new Point(x, y);
		if (chunkGraphicBuffer.get(point) == null) {
			PlanetPart chunkMesh = new PlanetPart(chunk, tileGraphicSize, planetConfig);

			controllerPhysic.addRigidBody(chunkMesh.getRigidBody(),
					ControllerPhysic.CollideType.GROUND, ControllerPhysic.CollideType.ROCKET);
			chunkGraphicBuffer.put(point, chunkMesh);
		} else {
			Log.d(getClass(), "Created a chunk that already exists. x " + x + " y " + y);
		}
	}


	public void render(ModelBatch batch, Environment environment) {
		for (Map.Entry<Point, PlanetPart> entry : chunkGraphicBuffer.entrySet()) {
			PlanetPart mesh = entry.getValue();
			batch.render(mesh.getModelInstance(), environment);
		}
	}


	public void dispose() {
		for (PlanetPart g : chunkGraphicBuffer.values()) {
			g.dispose();
		}
	}

}