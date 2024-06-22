package com.ball.pit.screens;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.ball.pit.BallPit;

public class MainMenu extends BallPitScreen {

    private final SpriteBatch spriteBatch;
//    private final Texture background;
    private final BitmapFont font;

    private boolean isDone = false;

    private final Matrix4 viewMatrix = new Matrix4();
    private final Matrix4 transformMatrix = new Matrix4();

    private final GlyphLayout glyphLayout = new GlyphLayout();

    public MainMenu (BallPit ballPit) {
        super(ballPit);

        spriteBatch = new SpriteBatch();
        // set background for screen

        font = new BitmapFont();

        if (ballPit.getController() != null) {
            ballPit.getController().addListener(new ControllerAdapter() {
                @Override
                public boolean buttonUp(Controller controller, int buttonIndex) {
                    controller.removeListener(this);
                    isDone = true;
                    return false;
                }
            });
        }
    }

    @Override
    public boolean isDone () { return isDone; }

    @Override
    public void update (float delta) {
        if (Gdx.input.justTouched()) {
            isDone = true;
        }
    }

    @Override
    public void draw (float delta) {
        Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

        viewMatrix.setToOrtho2D(0, 0, 480, 320);
        spriteBatch.setProjectionMatrix(viewMatrix);
        spriteBatch.setTransformMatrix(transformMatrix);
        spriteBatch.begin();
        spriteBatch.disableBlending();
        spriteBatch.setColor(Color.WHITE);
//        spriteBatch.draw(background, 0f, 0f, 480f, 320f, 0f, 0f, 512f, 512f);
        spriteBatch.enableBlending();
        spriteBatch.setBlendFunction(GL30.GL_ONE, GL30.GL_ONE_MINUS_SRC_ALPHA);
        glyphLayout.setText(font, "Touch screen to start!");
        font.draw(spriteBatch, glyphLayout, 240 - glyphLayout.width / 2, 128 - font.getLineHeight());
        if (Gdx.app.getType() == Application.ApplicationType.WebGL) {
            glyphLayout.setText(font, "Press Enter for Fullscreen Mode");
            font.draw(spriteBatch, glyphLayout, 240 - glyphLayout.width / 2, 128 - font.getLineHeight());
        }
        spriteBatch.end();
    }

    @Override
    public void dispose () {
        spriteBatch.dispose();
        font.dispose();
    }
}//        background.dispose();

