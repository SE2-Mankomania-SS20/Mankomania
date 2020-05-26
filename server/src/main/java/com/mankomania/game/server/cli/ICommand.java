package com.mankomania.game.server.cli;

public interface ICommand {
    /**
     * Returns the string (the "main command") that should trigger the command.
     * i.e. givemoney 3 10000       here would be "givemoney" the main command
     *
     * @return said main command
     */
    public String getMainCommand();

    /**
     * Gets the number of params that this command is accepting.
     *
     * @return the number of paramaters this command is accepting.
     */
    public int getNumberOfParams();

    /**
     * This method gets called when the main command of this command is typed in at the CLI.
     *
     * @param params the arguments given to the CLI. arguments get split by spaces first.
     */
    public void run(String... params);
}
