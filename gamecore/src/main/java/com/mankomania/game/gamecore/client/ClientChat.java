package com.mankomania.game.gamecore.client;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.StringBuilder;

import java.util.ArrayList;

/*********************************
 Created by Fabian Oraze on 18.04.20
 *********************************/

public class ClientChat {

    private static String[] chat = new String[6];

    public static void setText(CharSequence msg) {

        if (chat[chat.length - 1] == null) {
            for (int i = 0; i < chat.length; i++) {
                if (chat[i] == null) {
                    chat[i] = msg.toString();
                    break;
                }
            }
        } else {
            for (int i = chat.length - 1; i >= 0; i--) {
                if (i == 0) {
                    chat[i] = msg.toString();
                } else {
                    chat[i] = chat[i - 1];
                }
            }
        }

    }
    public static StringBuilder getText() {
        StringBuilder msg = new StringBuilder();
        for (String s : chat) {
            if (s != null) {
                msg.append(s + "\n\n");
            }
        }
        return msg;
    }


}
