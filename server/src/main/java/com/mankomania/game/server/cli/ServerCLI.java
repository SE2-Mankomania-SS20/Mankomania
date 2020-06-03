package com.mankomania.game.server.cli;

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
        // iterate over all commands and check whether the given "main command" is matching
        for (ICommand cmd : this.commands) {
            // check if the main commands and the amount of args are matching
            if (cmd.getMainCommand().equalsIgnoreCase(splitCommands[0]) &&
                    cmd.getNumberOfParams() == splitCommands.length - 1) {
                // call the command's run method with the given arguments (last one removed)
                cmd.run(this.prepareArgumentParams(splitCommands));
            }
        }

        // check if "help" was typed in, if so, print help command
        if (splitCommands[0].equalsIgnoreCase("help")) {
            this.printHelp();
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

    private void printHelp() {
        // take the longest command and a few character as padding to have equal deep indentations
        int paddingRight = this.getLongestCommand() + 4;
        StringBuilder helpOutput = new StringBuilder();

        helpOutput.append("All available commands:\n");
        for (ICommand cmd : this.commands) {
            helpOutput.append(this.padRight(cmd.getMainCommand(), paddingRight));
            helpOutput.append("\n");
            helpOutput.append(cmd.getHelpText());
            helpOutput.append("\n");
        }

        System.out.println(helpOutput.toString());
    }

    private int getLongestCommand() {
        int longest = -1;
        for (ICommand cmd : this.commands) {
            longest = Math.max(longest, cmd.getMainCommand().length());
        }
        return longest;
    }

    private String[] prepareArgumentParams(String[] splitCommand) {
        return Arrays.copyOfRange(splitCommand, 1, splitCommand.length);
    }

    private String padRight(String toPad, int amount) {
        // use String.format() to pad with spaces
        return String.format("%-" + amount + "s", toPad);
    }
}
