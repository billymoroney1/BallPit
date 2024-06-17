package com.ball.pit.screens;

import com.ball.pit.BallPit;
import com.badlogic.gdx.Screen;

public abstract class BallPitScreen implements Screen {

    protected BallPit ballPit;

    public BallPitScreen(BallPit ballPit) { this.ballPit = ballPit; }

    public abstract void update (float delta);

    public abstract void draw (float delta);

    public abstract boolean isDone ();

    @Override
    public void render (float delta) {
        update(delta);
        draw(delta);
    }

    @Override
    public void resize (int width, int height) {}

    @Override
    public void show () {}

    @Override
    public void hide () {}

    @Override
    public void pause () {}

    @Override
    public void resume () {}

    @Override
    public void dispose() {}

}
