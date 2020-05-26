package com.mankomania.game.server.cli;

import com.esotericsoftware.minlog.Log;

import java.util.ArrayList;
import java.util.Arrays;

public class ServerCLI {
    private ArrayList<ICommand> commands = new ArrayList<>();

    /**
     * Processes a command.
     *
     * @param command the full command as a string
     */
    public void processCommand(String command) {
        String[] splitCommands = command.split(" ");
        // Log.info("CLI", "got command: " + command + ", split length: " + splitCommands.length);
        // iterate over all commands and check wheter the given "main command" are matching
        for (ICommand cmd : this.commands) {
            if (cmd.getMainCommand().toLowerCase().equals(splitCommands[0].toLowerCase())) {
                // Log.info("CLI", "given string matches with command '" + cmd.getMainCommand() + "' with args " + cmd.getNumberOfParams());
                // check if the amount of args is matching
                if (cmd.getNumberOfParams() == splitCommands.length - 1) {
                    // Log.info("CLI", "arg count matched! new arg list is: " + Arrays.toString(this.prepareArgumentParams(splitCommands)));
                    // call the commands method with with the commands arguments (last one removed)
                    cmd.run(this.prepareArgumentParams(splitCommands));
                }
            }
        }
    }

    /**
     * Adds a command to the ServerCLI.
     *
     * @param newCommand the new command to add
     */
    public void addCommand(ICommand newCommand) {
        this.commands.add(newCommand);
    }

    private String[] prepareArgumentParams(String[] splitCommand) {
        return Arrays.copyOfRange(splitCommand, 1, splitCommand.length);
    }
}
