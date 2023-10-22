package com.gdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import java.util.ArrayList;
import java.util.List;

public class GdxGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, TPC, ProjDefault;
	private Sprite PC;
	private float posX, posY, vel, rposX, rposY;
	private int bulletVel;
	private List<float[]> PProjectiles = new ArrayList<float[]>();
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("img/PHBG.png");
		TPC = new Texture("img/entities/PC/PHKingPoring.png");
		ProjDefault = new Texture("img/entities/projectiles/PHdefault.png");
		PC = new Sprite(TPC);
		posX = 0; 
		posY = 0; 
		rposX = Gdx.graphics.getWidth() / 2 - PC.getWidth() / 2; 
		rposY = Gdx.graphics.getHeight() / 2 - PC.getHeight() / 2; 
		vel = 2;
		bulletVel = 1;
	}

	@Override
	public void render () {

		this.movePC();
		this.PProjectilesHandling();

		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, posX, posY);
		batch.draw(PC, rposX, rposY);
		
		for (int i=0; i < PProjectiles.size(); i++) {
			float[] i2 = PProjectiles.get(i);
			batch.draw(ProjDefault, i2[3], i2[4]);
		} 

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		TPC.dispose();
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

	private void PProjectilesHandling() {

		if (Gdx.input.isTouched()) {
			
			float[] projetil = new float[5];
			
			float mX = Gdx.input.getX() - 16;
			float mY = 720 - Gdx.input.getY() - 16;
			float velX = mX - rposX;
			float velY = mY - rposY;

			float length = (float) Math.sqrt(velX * velX + velY * velY);
			velX *= bulletVel / length;
			velY *= bulletVel / length;

			projetil[0] = 150;
			projetil[1] = velX;
			projetil[2] = velY;
			projetil[3] = rposX;
			projetil[4] = rposY;

			PProjectiles.add(projetil);
			
		}

		for (int i=0; i < PProjectiles.size(); i++) {
			float[] i2 = PProjectiles.get(i);
			i2[0] -= 1;
			i2[3] += i2[1];
			i2[4] += i2[2];

			if (i2[0] > 0) {
				PProjectiles.set(i, i2);
			}
			else {
				PProjectiles.remove(i);
			}
		}
	}
}
