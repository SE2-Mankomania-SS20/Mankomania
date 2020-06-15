package com.mankomania.game.gamecore.minigames;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Align;
import com.mankomania.game.core.network.messages.servertoclient.roulette.RouletteResultMessage;
import com.mankomania.game.gamecore.MankomaniaGame;

import java.util.List;

public class RouletteResultOfPlayers {
    private static final String BLACK = "black";
    private final Label[] playersID;
    private final TextField[] playersBets;
    private final TextField[] playersWonOrLost;
    private final TextField[] playersWinLostMoney;

    public RouletteResultOfPlayers() {
        this.playersID = new Label[4];
        this.playersBets = new TextField[4];
        this.playersWonOrLost = new TextField[4];
        this.playersWinLostMoney = new TextField[4];
    }

    public Table createRouletteResultOfPlayers(List<RouletteResultMessage> resultList) {
        Skin skin1 = new Skin(Gdx.files.internal("skin/terra-mother-ui.skin"));
        skin1.getFont("font").getData().setScale(3, 3);

        for (int i = 0; i < playersID.length; i++) {
            playersID[i] = new Label("", skin1, BLACK);
            playersID[i].setAlignment(Align.center);
            playersBets[i] = new TextField("", skin1, BLACK);
            playersBets[i].setAlignment(Align.center);
            playersBets[i].setColor(MankomaniaGame.getMankomaniaGame().getGameData().getColorOfPlayer(i));
            playersWonOrLost[i] = new TextField("", skin1, BLACK);
            playersWonOrLost[i].setAlignment(Align.center);
            playersWonOrLost[i].setColor(MankomaniaGame.getMankomaniaGame().getGameData().getColorOfPlayer(i));
            playersWinLostMoney[i] = new TextField("", skin1, BLACK);
            playersWinLostMoney[i].setColor(MankomaniaGame.getMankomaniaGame().getGameData().getColorOfPlayer(i));
            playersWinLostMoney[i].setAlignment(Align.center);
        }

        for (RouletteResultMessage rouletteResultMessage : resultList) {
            int playerIndex = rouletteResultMessage.getPlayerIndex();
            playersID[playerIndex].setText("PLAYER " + (playerIndex + 1));
            playersBets[playerIndex].setText(setTextForBet(rouletteResultMessage.getBet()));
            playersWonOrLost[playerIndex].setText(setTextForWin(rouletteResultMessage.getAmountWin()));
            playersWinLostMoney[playerIndex].setText(String.valueOf((rouletteResultMessage.getAmountWin())));
        }

        //label for table
        Label betPLayer = new Label("BET", skin1, BLACK);
        Label moneyPlayer = new Label("MONEY", skin1, BLACK);
        Label wonPlayer = new Label("WON/LOST", skin1, BLACK);
        Label result = new Label("RESULT", skin1, BLACK);
        Label emptySpace = new Label("", skin1, BLACK);

        //result for all players
        TextField resultOfRouletteWheel = new TextField(MankomaniaGame.getMankomaniaGame().getGameData().getArrayPlayerInformation().get(0).getResultOfRouletteWheel(), skin1, BLACK);
        resultOfRouletteWheel.setColor(Color.BLACK);
        resultOfRouletteWheel.setAlignment(Align.center);

        Stage stage = new Stage();

        Table tableBetAndResult = new Table();
        tableBetAndResult.setFillParent(false);
        tableBetAndResult.setWidth(Gdx.graphics.getWidth());
        tableBetAndResult.setHeight(Gdx.graphics.getHeight());
        tableBetAndResult.setBackground(new TiledDrawable(skin1.getTiledDrawable("tile-a")));

        tableBetAndResult.add(emptySpace).width(350).height(120);
        tableBetAndResult.add(playersID[0]).width(350).height(120);
        tableBetAndResult.add(playersID[1]).width(350).height(120);
        tableBetAndResult.add(playersID[2]).width(350).height(120);
        tableBetAndResult.add(playersID[3]).width(350).height(120);
        tableBetAndResult.row();
        tableBetAndResult.add(betPLayer).width(350).height(120);
        tableBetAndResult.add(playersBets[0]).width(350).height(120);
        tableBetAndResult.add(playersBets[1]).width(350).height(120);
        tableBetAndResult.add(playersBets[2]).width(350).height(120);
        tableBetAndResult.add(playersBets[3]).width(350).height(120);
        tableBetAndResult.row();
        tableBetAndResult.add(wonPlayer).width(350).height(120);
        tableBetAndResult.add(playersWonOrLost[0]).width(350).height(120);
        tableBetAndResult.add(playersWonOrLost[1]).width(350).height(120);
        tableBetAndResult.add(playersWonOrLost[2]).width(350).height(120);
        tableBetAndResult.add(playersWonOrLost[3]).width(350).height(120);
        tableBetAndResult.row();
        tableBetAndResult.add(moneyPlayer).width(350).height(120);
        tableBetAndResult.add(playersWinLostMoney[0]).width(350).height(120);
        tableBetAndResult.add(playersWinLostMoney[1]).width(350).height(120);
        tableBetAndResult.add(playersWinLostMoney[2]).width(350).height(120);
        tableBetAndResult.add(playersWinLostMoney[3]).width(350).height(120);
        tableBetAndResult.row();
        tableBetAndResult.add(result).width(350).height(120).padTop(20);
        tableBetAndResult.add(resultOfRouletteWheel).width(350).height(120).padTop(20);

        Gdx.input.setInputProcessor(stage);

        return tableBetAndResult;
    }

    public String setTextForBet(int index) {
        if (index > 36) {
            switch (index) {
                case 37:
                    return ("1-12");
                case 38:
                    return ("13-24");
                case 39:
                    return ("25-36");
                case 40:
                    return ("red");
                case 41:
                    return (BLACK);
                default:
                    return ("0");
            }
        }
        return String.valueOf(index);
    }

    public String setTextForWin(int win) {
        if (win > 1) {
            return "WON";
        } else {
            return "LOST";
        }
    }
}
