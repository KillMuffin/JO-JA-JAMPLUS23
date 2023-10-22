package com.gdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import java.util.ArrayList;
import java.util.List;

public class GdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, tPC, ProjDefault, tKnight;
	private float posX, posY, vel, rposX, rposY, fireRate, fireAux;
	private float enemyVelM, enemySpawnRateM;
	private float knightVel;
	private int bulletVel;
	private List<float[]> PProjectiles = new ArrayList<float[]>();
	private List<float[]> Knights = new ArrayList<float[]>();
	private long lastSpawnTime;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("img/PHBG.png");
		tPC = new Texture("img/entities/PC/PHKingPoring.png");
		ProjDefault = new Texture("img/entities/projectiles/PHdefault.png");
		tKnight = new Texture("img/entities/knight/PHgoblin.png");
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
	}

	@Override
	public void render () {

		this.movePC();
		this.handlePProjectiles();
		this.spawnKnight();
		this.handleKnights();

		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, posX, posY);
		batch.draw(tPC, rposX - tPC.getWidth() / 2, rposY - tPC.getHeight() / 2);
		
		for (int i=0; i < PProjectiles.size(); i++) {
			float[] projectile = PProjectiles.get(i);
			batch.draw(ProjDefault, projectile[3], projectile[4]);
		} 

		for (int i=0; i < Knights.size(); i++) {
			float[] knight = Knights.get(i);
			batch.draw(tKnight, knight[1], knight[2]);
		}

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		tPC.dispose();
		ProjDefault.dispose();
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

	private void spawnKnight() {
		float[] knight = new float[7];
		

		knight[0] = 2;
		knight[1] = MathUtils.random(0, 1280);
		knight[2] = MathUtils.random(0, 720);
		knight[3] = posX;
		knight[4] = posY;

		Knights.add(knight);
		
		lastSpawnTime = TimeUtils.nanoTime();
	}

	private void handleKnights() {
		
		// if (TimeUtils.nanoTime() - lastSpawnTime > 1000000000) {
		// 	this.spawnEnemies();
		// }

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
}
