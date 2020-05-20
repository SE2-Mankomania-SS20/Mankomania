package com.mankomania.game.gamecore.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/*
 Created by Fabian Oraze on 30.04.20
 */

public class ClientChatTest {

    private String text;
    private String text2;
    private String expText;
    private String expText2;

    @BeforeEach
    public void init() {
        text = "test";
        text2 = "hello";
        expText = text + "\n\n";
        expText2 = text2 + "\n\n";
    }

    @AfterEach
    public void reset() {
        text = null;
        text2 = null;
        expText = null;
        expText2 = null;
        ClientChat.clearChat();
    }

    @Test
    public void testClearChat() {

        ClientChat.addText(text);
        Assertions.assertNotNull(ClientChat.getText());
        ClientChat.clearChat();
        Assertions.assertEquals("", ClientChat.getText());
    }


    @Test
    public void addOneLineOfText() {
        ClientChat.addText(text);
        String exp = expText;

        Assertions.assertEquals(exp, ClientChat.getText());
    }

    @Test
    public void addFourLinesOfText() {
        ClientChat.addText(text2);
        ClientChat.addText(text);
        ClientChat.addText(text2);
        ClientChat.addText(text);

        String exp = expText2 + expText + expText2 + expText;

        Assertions.assertEquals(exp, ClientChat.getText());
    }

    @Test
    public void addMoreThanSixLinesOfText() {
        for (int i = 0; i < 6; i++) {
            ClientChat.addText(text);
        }

        ClientChat.addText(text2);
        ClientChat.addText(text2);

        String exp = expText + expText + expText + expText + expText2 + expText2;

        Assertions.assertEquals(exp, ClientChat.getText());
    }

    @Test
    public void addThanClearAndAddText() {
        ClientChat.addText(text);
        ClientChat.clearChat();
        ClientChat.addText(text2);

        String exp = expText2;

        Assertions.assertEquals(exp, ClientChat.getText());

    }
}
