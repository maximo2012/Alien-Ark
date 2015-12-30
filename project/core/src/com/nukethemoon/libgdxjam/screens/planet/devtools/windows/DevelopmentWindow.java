package com.nukethemoon.libgdxjam.screens.planet.devtools.windows;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.nukethemoon.libgdxjam.screens.planet.PlanetConfig;
import com.nukethemoon.libgdxjam.screens.planet.devtools.ReloadSceneListener;

public class DevelopmentWindow extends ClosableWindow {

	public static NinePatchDrawable INNER_BACKGROUND;

	public DevelopmentWindow(final Skin skin, Stage stage, final PlanetConfig planetConfig, ReloadSceneListener reloadSceneListener) {
		super("Development", skin);
		NinePatch patch = new NinePatch(new Texture(Gdx.files.internal("skin/background.png")),
				1, 1, 1, 1);

		INNER_BACKGROUND = new NinePatchDrawable(patch);


		final DirectionalLightsWindow directionalLightsWindow = new DirectionalLightsWindow(skin, reloadSceneListener);
		directionalLightsWindow.setVisible(false);
		stage.addActor(directionalLightsWindow);

		final MaterialsWindow materialsWindow = new MaterialsWindow(skin, reloadSceneListener);
		materialsWindow.setVisible(false);
		stage.addActor(materialsWindow);


		ColorAttribute colorAttribute = planetConfig.environmentColorAttributes.get(0);
		com.nukethemoon.libgdxjam.screens.planet.devtools.forms.ColorForm cf = new com.nukethemoon.libgdxjam.screens.planet.devtools.forms.ColorForm(skin, colorAttribute.color, new com.nukethemoon.libgdxjam.screens.planet.devtools.forms.ColorForm.ColorChangeListener() {
			@Override
			public void onColorChange(float v, float v1, float v2) {
				pack();
			}
		});
		add(cf);
		row();


		TextButton lightsButton = new TextButton("Directional Lights", skin);
		lightsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				directionalLightsWindow.load(planetConfig);
				directionalLightsWindow.setVisible(true);
				directionalLightsWindow.toFront();
			}
		});
		add(lightsButton);
		row();

		TextButton materialsButton = new TextButton("Materials", skin);
		materialsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				materialsWindow.load(planetConfig);
				materialsWindow.setVisible(true);
				materialsWindow.toFront();
			}
		});
		add(materialsButton);

		row();
		add(new Label("'Tab' toggle camera", skin)).left().fill();
		row();
		add(new Label("'p' toggle pause", skin)).left().fill();

		pack();
	}
}
