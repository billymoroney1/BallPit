package com.ball.pit.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.ball.pit.BallPit;

public class GameOver extends BallPitScreen {

    private final SpriteBatch spriteBatch;
    private final BitmapFont font;
    private boolean isDone = false;
    private final Matrix4 viewMatrix = new Matrix4();
    private final Matrix4 transformMatrix = new Matrix4();

    private final GlyphLayout glyphLayout = new GlyphLayout();

    public GameOver (BallPit ballPit) {
        super(ballPit);
        spriteBatch = new SpriteBatch();
        // set background
        font = new BitmapFont();

        if (ballPit.getController() != null) {
            ballPit.getController().addListener(new ControllerAdapter() {
                @Override
                public boolean buttonUp(Controller controller, int buttonindex) {
                    controller.removeListener(this);
                    isDone = true;
                    return false;
                }
            });
        }
    }

    @Override
    public void dispose () {
        spriteBatch.dispose();
        font.dispose();
    }

    @Override
    public boolean isDone () { return isDone; }

    @Override
    public void draw (float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewMatrix.setToOrtho2D(0, 0, 480, 320);
        spriteBatch.setProjectionMatrix(viewMatrix);
        spriteBatch.setTransformMatrix(transformMatrix);
        spriteBatch.begin();
        spriteBatch.disableBlending();
        spriteBatch.setColor(Color.WHITE);
        // background here
        spriteBatch.enableBlending();
        glyphLayout.setText(font, "Game over, press any button to try again");
        spriteBatch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        font.draw(spriteBatch, glyphLayout, 0, 160 + glyphLayout.height / 2);
        spriteBatch.end();
    }

    @Override
    public void update (float delta) {
        if (Gdx.input.justTouched()) {
            isDone = true;
        }
    }
}
