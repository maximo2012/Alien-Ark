package com.nukethemoon.libgdxjam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.nukethemoon.libgdxjam.input.InputController;
import com.nukethemoon.libgdxjam.screens.MenuScreen;
import com.nukethemoon.libgdxjam.screens.SplashScreen;

import java.util.HashMap;
import java.util.Map;

public class App extends Game {

	private static Skin UI_SKIN;
	private static InputMultiplexer MULTIPLEXER;
	private static App app;

	private static Map<Class<? extends Screen>, ? extends Screen> SCREENS = new HashMap<Class<? extends Screen>, Screen>();


	@Override
	public void create () {
		app = this;
		MULTIPLEXER = new InputMultiplexer();
		MULTIPLEXER.addProcessor(new InputController());
		Gdx.input.setInputProcessor(MULTIPLEXER);
		UI_SKIN = new Skin(Gdx.files.internal(Config.UI_SKIN_PATH));

		openScreen(SplashScreen.class);
	}


	public static void openScreen(Class<? extends Screen> screenClass) {
		/*Screen screen = SCREENS.get(screenClass);
		if (screen == null) {

		}*/

		MenuScreen menuScreen = new MenuScreen(UI_SKIN, MULTIPLEXER);
		menuScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		app.setScreen(menuScreen);
	}


}
