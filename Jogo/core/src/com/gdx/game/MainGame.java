package com.gdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;

public class MainGame implements Screen {
	public Stage gameStage;

	Texture background, tPC, ProjDefault, tKnight;
	SpriteBatch batch;
	private float posX, posY, vel, rposX, rposY, fireRate, fireAux, spawnX, spawnY, roundCount, pDamage, HP;
	private float enemyVelM;
	private float bulletVel, knightVel;
	private Button buttonLife, buttonMove, buttonFire, buttonBack, buttonQuit;
	private Skin skin;
	private double enemySpawnRateM;
	private List<float[]> PProjectiles = new ArrayList<float[]>();
	private List<float[]> Knights = new ArrayList<float[]>();
	private long lastSpawnTime, rTime, iFrames;
	private boolean round, isShop, tangible;

	public Main main;

	public MainGame(Main main) {
		
		this.main = main;
		
		batch = new SpriteBatch();
		
		posX = 0; 
		posY = 0; 
		rposX = Gdx.graphics.getWidth() / 2; 
		rposY = Gdx.graphics.getHeight() / 2; 
		vel = 4;
		enemyVelM = 1;
		knightVel = 1;
		bulletVel = 3;
		fireRate = 1;
		fireAux = 60;
		lastSpawnTime = 0;
		enemySpawnRateM = 1;
		spawnX = 0;
		spawnY = 0;
		rTime = TimeUtils.millis();
		round = true;
		tangible = true;
		roundCount = 1;
		pDamage = 1;
		HP = 4;

		isShop = false;
		skin = new Skin(Gdx.files.internal("arcade/skin/arcade-ui.json"));
		
		buttonLife = new Button(skin);
		buttonMove = new Button(skin);
		buttonFire = new Button(skin);
		
		buttonBack = new Button(skin);
		buttonQuit = new Button(skin);
		
		buttonLife.setPosition(318, 163);
		buttonMove.setPosition(418, 163);
		buttonFire.setPosition(518, 163);
		
		buttonBack.setPosition(418, 163);
		buttonQuit.setPosition(518, 163);

		background = new Texture("background.png");
		tPC = new Texture("slime.png");
		ProjDefault = new Texture("tiro.png");
		tKnight = new Texture("knight.png");
		
		gameStage = new Stage(new FitViewport(1280, 720));
		Gdx.input.setInputProcessor(gameStage);

		gameStage.addActor(buttonLife);
		gameStage.addActor(buttonMove);
		gameStage.addActor(buttonFire);

		buttonLife.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
                round = false;
            }
        });
		
		buttonMove.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
                round = false;
            }
        });
		
		buttonFire.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
                round = false;
            }
        });
		
		if(isShop == false) {
			round = true;
		}
		
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub

		this.movePC();
		this.handlePProjectiles();
		this.maelstrom();
		this.handleKnights();
		this.handlePProjectilesColision();
		this.handlePCColision();

		Gdx.gl.glClearColor(0, 0, 0, 1);
		
		gameStage.act(Gdx.graphics.getDeltaTime());
        
        gameStage.getBatch().begin();

        if(isShop == false) {
        	gameStage.getBatch().draw(background, posX - background.getWidth() / 2 + 100, posY - background.getHeight() / 2);
			
        	if (tangible == true) {
				gameStage.getBatch().draw(tPC, rposX - tPC.getWidth() / 2, rposY - tPC.getHeight() / 2);
			}

			else {
				if (TimeUtils.millis() % 2 == 1) {
					gameStage.getBatch().draw(tPC, rposX - tPC.getWidth() / 2, rposY - tPC.getHeight() / 2);
				}
			}
        	this.movePC();
    		
    		for (int i=0; i < PProjectiles.size(); i++) {
    			float[] projectile = PProjectiles.get(i);
    			gameStage.getBatch().draw(ProjDefault, projectile[3], projectile[4]);
    		} 

    		for (int i=0; i < Knights.size(); i++) {
    			float[] knight = Knights.get(i);
    			gameStage.getBatch().draw(tKnight, knight[1], knight[2]);
    		}
    		buttonLife.setVisible(false);
    		buttonMove.setVisible(false);
    		buttonFire.setVisible(false);
        }

        if(isShop == true) {
        	gameStage.getBatch().draw(background, posX - background.getWidth() / 2 + 100, posY - background.getHeight() / 2);
        	buttonLife.setVisible(true);
        	buttonMove.setVisible(true);
        	buttonFire.setVisible(true);
        	
        }

		gameStage.getBatch().end();
        
		gameStage.draw();
		
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
	
	private void movePC() {

		if (Gdx.input.isKeyPressed(Input.Keys.D) && posX > -Gdx.graphics.getWidth()) {
			posX -= vel;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.A) && posX < Gdx.graphics.getWidth()) {
			posX += vel;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.W) && posY > -Gdx.graphics.getHeight()) {
			posY -= vel;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S) && posY < Gdx.graphics.getHeight()) {
			posY += vel;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			isShop = true;
		}
	}

	private void handlePProjectiles() {
		if (fireAux > 0) {
			fireAux -= fireRate;
		}
		if (fireAux <= 0) {
			if (Gdx.input.isTouched()) {

				fireAux = 60;

				float[] projetil = new float[7];
				
				float mX = Gdx.input.getX() - 16;
				float mY = 720 - Gdx.input.getY() - 16;
				float velX = mX - rposX;
				float velY = mY - rposY;

				float length = (float) Math.sqrt(velX * velX + velY * velY);
				velX *= bulletVel / length;
				velY *= bulletVel / length;

				projetil[0] = 300;
				projetil[1] = velX;
				projetil[2] = velY;
				projetil[3] = rposX;
				projetil[4] = rposY;
				projetil[5] = posX;
				projetil[6] = posY;

				PProjectiles.add(projetil);
				
			}
		}
		for (int i=0; i < PProjectiles.size(); i++) {
			float[] i2 = PProjectiles.get(i);
			i2[0] -= 1;
			i2[3] += i2[1]; 
			i2[4] += i2[2];

			i2[3] -= i2[5] - posX;
			i2[4] -= i2[6] - posY;
			i2[5] = posX;
			i2[6] = posY;

			if (i2[0] > 0) {
				PProjectiles.set(i, i2);
			}
			else {
				PProjectiles.remove(i);
			}
		}
	}

	private void maelstrom() {

		if (round == false) {
			if (TimeUtils.millis() - rTime > 20000) {
					round = true;
					rTime = TimeUtils.millis();
					roundCount += 1;
					enemySpawnRateM -= 0.3;
					enemyVelM += 0.2;
			}
		}

		if (round == true) {

			if (TimeUtils.nanoTime() - lastSpawnTime > 2000000000 * enemySpawnRateM) {
				
				if (TimeUtils.millis() - rTime > 90000) {
					round = false;
					rTime = TimeUtils.millis();
				}

				float c = MathUtils.random(1, 4);

				if (c == 1) {
					spawnX = MathUtils.random(-100, -50);
					spawnY = MathUtils.random(0, 720);
				}

				if (c == 2) {
					spawnX = MathUtils.random(1330, 1380);
					spawnY = MathUtils.random(0, 720);
				}

				if (c == 3) {
					spawnX = MathUtils.random(0, 1280);
					spawnY = -1 * MathUtils.random(80, 180);
				}

				if (c == 4) {
					spawnX = MathUtils.random(0, 1280);
					spawnY = MathUtils.random(800, 850);
				}

				spawnKnight();

				lastSpawnTime = TimeUtils.nanoTime();
			}
		}
		

	}

	private void spawnKnight() {
		float[] knight = new float[7];
		

		knight[0] = 2;
		knight[1] = spawnX;
		knight[2] = spawnY;
		knight[3] = posX;
		knight[4] = posY;

		Knights.add(knight);
		
		lastSpawnTime = TimeUtils.nanoTime();
	}

	private void handleKnights() {

		for (int i=0; i < Knights.size(); i++) {
			float[] knight = Knights.get(i);

			float velX = rposX - knight[1];
			float velY = rposY - knight[2];

			float length = (float) Math.sqrt(velX * velX + velY * velY);
			velX *= (knightVel * enemyVelM) / length;
			velY *= (knightVel * enemyVelM) / length;
			
			knight[1] += velX; 
			knight[2] += velY;

			knight[1] -= knight[3] - posX;
			knight[2] -= knight[4] - posY;
			knight[3] = posX;
			knight[4] = posY;

			if (knight[0] <= 0) {
				Knights.remove(knight);
			}
		}
	}

	private void handlePProjectilesColision() {
		if (Knights.size() > 0) {
			for (int i=0; i < PProjectiles.size(); i++) {
				float[] projectile = PProjectiles.get(i);
				for (int i2=0; i2 < Knights.size(); i2++) {
					float[] knight = Knights.get(i2);
					//projectile[3] X = projectile[4] = Y // knight[1] = X knight[2] = Y

					float distX2 = projectile[3] - knight[1];
					float DistY2 = projectile[4] - knight[2];

					float distance = (float) Math.sqrt(distX2 * distX2 + DistY2 * DistY2);

					if (distance <= 30) {
						knight[0] -= pDamage;
						PProjectiles.remove(projectile);
					}
				}
			}
		}
	}

	private void handlePCColision() {
		if (Knights.size() > 0) {
		
			for (int i=0; i < Knights.size(); i++) {
				float[] knight = Knights.get(i);
				// knight[1] = X knight[2] = Y

				float distX2 = rposX - knight[1];
				float DistY2 = rposY - knight[2];

				float distance = (float) Math.sqrt(distX2 * distX2 + DistY2 * DistY2);

				if (TimeUtils.millis() - iFrames  > 900) {
					tangible = true;
					if (distance <= 70) {
						HP -= 1;
						tangible = false;
						iFrames = TimeUtils.millis();
					}
				}
			}
		
		}
	}
}	