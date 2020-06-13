package com.mankomania.game.gamecore.fieldoverlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mankomania.game.core.data.GameData;
import com.mankomania.game.core.fields.types.Field;
import com.mankomania.game.core.player.Player;
import com.mankomania.game.gamecore.MankomaniaGame;
import com.mankomania.game.gamecore.fieldoverlay.fielddata.FieldOverlayFieldInfo;
import com.mankomania.game.gamecore.fieldoverlay.fielddata.FieldOverlayRowPosition;


import static com.mankomania.game.gamecore.fieldoverlay.FieldOverlayConfig.*;

/**
 * a class which stores among the base field (Field property) the textures to render in the overlay
 */
public class FieldOverlayField {
    private Field baseField;
    private FieldOverlayFieldInfo fieldInfo;
    private int fieldId;
    private Texture texture;
    private BitmapFont debugFont;

    private float currentPosX;
    private final float currentPosY;
    private static final int FIELD_PADDING_LEFT = 2 * COLUMN_WIDTH;

    private FieldOverlayFieldBorder fieldBorder;
    // reference to gamedata in order to not have to fetch this reference in each render call
    private GameData gameData;
    // holds references to the player dot textures
    private FieldOverlayTextures.PlayerDotTextures playerDotTextures;

    public FieldOverlayField(Field baseField, FieldOverlayFieldInfo fieldInfo, int id) {
        this.baseField = baseField;
        this.fieldInfo = fieldInfo;
        this.fieldId = id;

        this.fieldBorder = new FieldOverlayFieldBorder();

        // y position wont change, thats why we can calculate it at ctor already
        this.currentPosY = calculateYPosition();
    }

    public void create(FieldOverlayTextures fieldTextures) {
        switch (baseField.getColor()) {
            case GREY:
            case WHITE:
                this.texture = fieldTextures.getFieldWhite();
                break;
            case ORANGE:
                this.texture = fieldTextures.getFieldOrange();
                break;
            case YELLOW:
                this.texture = fieldTextures.getFieldYellow();
                break;
            case BLUE:
                this.texture = fieldTextures.getFieldBlue();
                break;
            case RED:
                this.texture = fieldTextures.getFieldMagenta();
                break;
        }

        this.fieldBorder.create(fieldTextures);

        this.debugFont = new BitmapFont(Gdx.files.internal("fonts/beleren.fnt"));
        this.debugFont.getData().markupEnabled = true; // enable color markup in font rendering strings

        this.playerDotTextures = fieldTextures.getPlayerDotTextures();

        this.gameData = MankomaniaGame.getMankomaniaGame().getGameData();
    }

    public void draw(SpriteBatch batch) {
        this.fieldBorder.update();
        this.fieldBorder.render(batch, (int) this.currentPosX, (int) this.currentPosY, BOX_WIDTH, BOX_WIDTH);

        batch.draw(this.texture, this.currentPosX, this.currentPosY, BOX_WIDTH, BOX_WIDTH);
        this.debugFont.draw(batch, "[BLACK]" + this.fieldId, this.currentPosX + 30, this.currentPosY + 80);

        // check if a player is on this field
        for (int i = 0; i < this.gameData.getPlayers().size(); i++) {
            Player p = this.gameData.getPlayers().get(i);
            if (p.getCurrentFieldIndex() == this.fieldId) {
                // render dot here
//                batch.draw(this.getDotTextureByPlayerIndex(i), this.currentPosX, this.currentPosY, DOT_SIZE, DOT_SIZE);
                this.drawPlayerDot(batch, i);
            }
        }
    }

    /**
     * Tests if the given point is on this field.
     *
     * @param x x coordinate of the point
     * @param y y coordinate of the point
     * @return true or false wheter the point lies on this field
     */
    public boolean isOverField(int x, int y) {
        return x >= this.currentPosX && x <= this.currentPosX + BOX_WIDTH &&
                y >= this.currentPosY && y <= this.currentPosY + BOX_WIDTH;
    }


    /* === BORDER STUFF */
    public void showBorder() {
        this.fieldBorder.show();
    }

    public void hideBorder() {
        this.fieldBorder.hide();
    }


    /* === GETTER === */
    public int getId() {
        return this.fieldId;
    }

    public int getColumn() {
        return this.fieldInfo.getColumn();
    }

    public Field getBaseField() {
        return this.baseField;
    }

    public void setCurrentPosX(float currentPosX) {
        this.currentPosX = currentPosX - FIELD_PADDING_LEFT;
    }

    @Override
    public String toString() {
        return "FieldOverlayField{" +
                "baseField=" + baseField +
                ", fieldInfo=" + fieldInfo +
                ", fieldInfoId = " + fieldInfo.getId() +
                ", fieldId=" + fieldId + "}";
    }


    /**
     * Calculates the Y position of the current field, depending on which row it is in
     *
     * @return the base Y position of this field
     */
    private int calculateYPosition() {
        int yPos;

        if (this.fieldInfo.getRowPosition() == FieldOverlayRowPosition.TOP) {
            yPos = (Gdx.graphics.getHeight() - BOX_WIDTH) - SPLIT_MARGIN_TOP;
        } else if (this.fieldInfo.getRowPosition() == FieldOverlayRowPosition.MIDDLE) {
            yPos = (Gdx.graphics.getHeight() - BOX_WIDTH) - MARGIN_TOP;
        } else {
            yPos = (Gdx.graphics.getHeight() - BOX_WIDTH) - SPLIT_MARGIN_TOP_ALTERNATE;
        }

        return yPos;
    }

    private void drawPlayerDot(SpriteBatch batch, int playerIndex) {
        // p1: (0,0), p2: (1,0), p3: (0,1), p4: (1,1)
        switch (playerIndex) {
            case 0:
                batch.draw(this.playerDotTextures.getPlayerDotBlue(), this.currentPosX, this.currentPosY + BOX_WIDTH - DOT_SIZE, DOT_SIZE, DOT_SIZE);
                break;
            case 1:
                batch.draw(this.playerDotTextures.getPlayerDotGreen(), this.currentPosX + BOX_WIDTH - DOT_SIZE, this.currentPosY + BOX_WIDTH - DOT_SIZE, DOT_SIZE, DOT_SIZE);
                break;
            case 2:
                batch.draw(this.playerDotTextures.getPlayerDotRed(), this.currentPosX, this.currentPosY, DOT_SIZE, DOT_SIZE);
                break;
            case 3:
                batch.draw(this.playerDotTextures.getPlayerDotYellow(), this.currentPosX + BOX_WIDTH - DOT_SIZE, this.currentPosY, DOT_SIZE, DOT_SIZE);
                break;

        }

    }
}
