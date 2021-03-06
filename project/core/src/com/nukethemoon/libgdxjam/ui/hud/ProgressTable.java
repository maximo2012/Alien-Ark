package com.nukethemoon.libgdxjam.ui.hud;

import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.nukethemoon.libgdxjam.App;

public class ProgressTable extends Table {

	private Image[] images;
	private ShipProgressBar.ProgressType progressType;

	public ProgressTable(ShipProgressBar.ProgressType progressType) {
		this.progressType = progressType;
		String texturePrefix;
		if (progressType == ShipProgressBar.ProgressType.SHIELD) {
			texturePrefix = "ShieldProgressBar0";
		} else {
			texturePrefix = "progressBar0";
		}
		left().bottom();

		images = new Image[8];
		for (int i = 0; i < images.length; i++) {
			int imageIndex = progressType == ShipProgressBar.ProgressType.SHIELD ? images.length - i - 1: i;
			Image tmpImage = new Image(App.TEXTURES.findRegion(texturePrefix + ((imageIndex / 2) + 1)));
			Cell<Image> imageCell = add(tmpImage).padLeft(5).padBottom(5);
			images[i] = imageCell.getActor();
		}
		pack();
	}

	public int setValue(int value, int max) {
		float progress = (float) value / (float) max;
		int barCount = (int) Math.floor(progress * images.length);

		if (progressType == ShipProgressBar.ProgressType.FUEL) {
			for (int i = 0; i < images.length; i++) {
				if (i > barCount || value == 0) {
					images[i].setVisible(false);
				} else {
					images[i].setVisible(true);
				}
			}
		} else {
			int usedCount = images.length - (barCount + 1);
			for (int i = 0; i < images.length; i++) {
				if (i < usedCount || value == 0) {
					images[i].setVisible(false);
				} else {
					images[i].setVisible(true);
				}
			}
		}

		return barCount;
	}
}
