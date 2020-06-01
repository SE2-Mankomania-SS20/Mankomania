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

import java.util.ArrayList;


public class RouletteResultOfPlayers {
    private Stage stage;
    private Label player1;
    private Label player2;
    private Label player3;
    private Label player4;
    private Label result;
    private Label moneyPlayer;
    private Label emptySpace;
    private Label betPLayer;
    private Label wonPlayer;
    private TextField textFieldBetPlayer1;
    private TextField textFieldBetPlayer2;
    private TextField textFieldBetPlayer3;
    private TextField textFieldBetPlayer4;
    private TextField textFieldWonOrLost1;
    private TextField textFieldWonOrLost2;
    private TextField textFieldWonOrLost3;
    private TextField textFieldWonOrLost4;
    private TextField textFieldWonLostMoney1;
    private TextField textFieldWonLostMoney2;
    private TextField textFieldWonLostMoney3;
    private TextField textFieldWonLostMoney4;
    private TextField resultOfRouletteWheel;
    private Table tableBetAndResult;
    private Skin skin1;
    private Label [] playersID = {player1, player2, player3, player4};
    private TextField [] playersBets = {textFieldBetPlayer1, textFieldBetPlayer2, textFieldBetPlayer3, textFieldBetPlayer4};
    private TextField [] playersWonOrLost = {textFieldWonOrLost1, textFieldWonOrLost2, textFieldWonOrLost3, textFieldWonOrLost4};
    private TextField [] playersWinLostMoney = {textFieldWonLostMoney1, textFieldWonLostMoney2, textFieldWonLostMoney3, textFieldWonLostMoney4};
    private ArrayList<RouletteResultMessage> resultList;
    private final String black = "black";

    public Table createRouletteResultOfPlayers(ArrayList<RouletteResultMessage> results) {
        skin1 = new Skin(Gdx.files.internal("skin/terra-mother-ui.json"));
        skin1.getFont("font").getData().setScale(5, 5);
        this.resultList = results;

        for (int i = 0; i < playersID.length ; i++) {
            playersID[i] = new Label("" , skin1, black);
            playersID[i].setAlignment(Align.center);
            playersBets[i] = new TextField(""  , skin1, black);
            playersBets[i].setAlignment(Align.center);
            playersBets[i].setColor(MankomaniaGame.getMankomaniaGame().getGameData().getColorOfPlayer(i));
            playersWonOrLost[i] = new TextField("",skin1,black);
            playersWonOrLost[i].setAlignment(Align.center);
            playersWonOrLost[i].setColor(MankomaniaGame.getMankomaniaGame().getGameData().getColorOfPlayer(i));
            playersWinLostMoney[i] = new TextField("" , skin1, black);
            playersWinLostMoney[i].setColor(MankomaniaGame.getMankomaniaGame().getGameData().getColorOfPlayer(i));
            playersWinLostMoney[i].setAlignment(Align.center);
        }

        for (int i = 0; i < resultList.size(); i++) {
            playersID[i].setText("PLAYER "+ resultList.get(i).getPlayerID());
            playersBets[i].setText(setTextForBet(resultList.get(i).getBet()));
            playersWonOrLost[i].setText(setTextForWin(resultList.get(i).getAmountWin()));
            playersWinLostMoney[i].setText(String.valueOf((resultList.get(i).getAmountWin())));
        }

        //label for table
        betPLayer = new Label("BET",skin1,"black");
        moneyPlayer = new Label("MONEY", skin1, "black");
        wonPlayer = new Label("WON/LOST",skin1,"black");
        result = new Label("RESULT", skin1, "black");
        emptySpace = new Label("",skin1,"black");

        //result for all players
        resultOfRouletteWheel = new TextField(MankomaniaGame.getMankomaniaGame().getGameData().getArrayPlayerInformation().get(0).getResultOfRouletteWheel(), skin1, black);
        resultOfRouletteWheel.setColor(Color.BLACK);
        resultOfRouletteWheel.setAlignment(Align.center);

        stage = new Stage();

        tableBetAndResult = new Table();
        tableBetAndResult.setFillParent(false);
        tableBetAndResult.setWidth(Gdx.graphics.getWidth());
        tableBetAndResult.setHeight(Gdx.graphics.getHeight());
        tableBetAndResult.setBackground(new TiledDrawable(skin1.getTiledDrawable("tile-a")));

        tableBetAndResult.add(emptySpace).width(500).height(120);
        tableBetAndResult.add(playersID[0]).width(500).height(120);
        tableBetAndResult.add(playersID[1]).width(500).height(120);
        tableBetAndResult.add(playersID[2]).width(500).height(120);
        tableBetAndResult.add(playersID[3]).width(500).height(120);
        tableBetAndResult.row();
        tableBetAndResult.add(betPLayer).width(500).height(120);
        tableBetAndResult.add(playersBets[0]).width(500).height(120);
        tableBetAndResult.add(playersBets[1]).width(500).height(120);
        tableBetAndResult.add(playersBets[2]).width(500).height(120);
        tableBetAndResult.add(playersBets[3]).width(500).height(120);
        tableBetAndResult.row();
        tableBetAndResult.add(wonPlayer).width(500).height(120);
        tableBetAndResult.add(playersWonOrLost[0]).width(500).height(120);
        tableBetAndResult.add(playersWonOrLost[1]).width(500).height(120);
        tableBetAndResult.add(playersWonOrLost[2]).width(500).height(120);
        tableBetAndResult.add(playersWonOrLost[3]).width(500).height(120);
        tableBetAndResult.row();
        tableBetAndResult.add(moneyPlayer).width(500).height(120);
        tableBetAndResult.add(playersWinLostMoney[0]).width(500).height(120);
        tableBetAndResult.add(playersWinLostMoney[1]).width(500).height(120);
        tableBetAndResult.add(playersWinLostMoney[2]).width(500).height(120);
        tableBetAndResult.add(playersWinLostMoney[3]).width(500).height(120);
        tableBetAndResult.row();
        tableBetAndResult.add(result).width(500).height(120).padTop(20);
        tableBetAndResult.add(resultOfRouletteWheel).width(500).height(120).padTop(20);

        Gdx.input.setInputProcessor(stage);

        return tableBetAndResult;
    }

    public String setTextForBet(int index) {
        if (index > 36) {
            switch (index) {
                case 37: return ("1-12");
                case 38: return ("13-24");
                case 39: return ("25-36");
                case 40: return ("rot");
                case 41: return ("schwarz");
            }
        }
        return String.valueOf(index);
    }
    public String setTextForWin(int win) {
        if (win > 1) {
            return "WON";
        }else {
            return "LOST";
        }
    }
}
