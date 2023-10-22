package com.gdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gdx.game.MainGame;
 

public class MenuScreen implements Screen{
    private static final int Main = 0;
	public Stage menuStage;
    private Button button;
    private Skin skin;

    Texture background;

    public MenuScreen(final Main main) {
    	
    	System.out.println("Foi");
    	
    	background = new Texture("template.jpg");
    	
    	skin = new Skin(Gdx.files.internal("arcade/skin/arcade-ui.json"));
    	button = new Button(skin);
    	
    	menuStage = new Stage(new FitViewport(1280, 720));
    	Gdx.input.setInputProcessor(menuStage);
    	
    	button.setPosition(440, 260); // Defina a posi��o do bot�o na tela
    	
    	menuStage.addActor(button);

    	button.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
                System.out.println("SEXO");
                main.ChangeScreen(new MainGame(main));
            }
        });

}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);

        menuStage.act(Gdx.graphics.getDeltaTime());
        
        menuStage.getBatch().begin();
        menuStage.getBatch().draw(background, 0, 0);
        menuStage.getBatch().end();
        
        menuStage.draw();
		
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}
	
    }