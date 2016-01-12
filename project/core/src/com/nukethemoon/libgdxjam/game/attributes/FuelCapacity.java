package com.nukethemoon.libgdxjam.game.attributes;


public class FuelCapacity extends Attribute {

	public static float INTERNAL_MIN = 200;
	public static float INERNAL_MAX = 9999;

	public FuelCapacity(int maxFuel) {
		setCurrentValue(maxFuel);
	}

}
