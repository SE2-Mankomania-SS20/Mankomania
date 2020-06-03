package com.mankomania.game.server.cli.commands;

import com.esotericsoftware.minlog.Log;
import com.mankomania.game.server.cli.ICommand;

public class ExitCmd implements ICommand {
    @Override
    public String getMainCommand() {
        return "exit";
    }

    @Override
    public int getNumberOfParams() {
        return 0;
    }

    @Override
    public void run(String... params) {
        Log.info("ExitCommand", "Server shutting down...");
        System.exit(0);
    }

    @Override
    public String getHelpText() {
        return "quits the server";
    }
}
