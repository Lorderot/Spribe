package services;

import collections.PlayersContainer;
import model.Player;

public class PlayerServiceRandomTreeImpl implements PlayerService {
    private PlayersContainer containerOfReadyPlayers = new PlayersContainer();

    @Override
    public boolean registerReadyPlayer(Player player) {
        if (player == null) {
            throw new NullPointerException();
        }
        if (containerOfReadyPlayers.contains(player)) {
            return false;
        }
        containerOfReadyPlayers.addPlayer(player);
        return true;
    }

    @Override
    public Player findOpponent(Player player) {
        if (player == null) {
            throw new NullPointerException();
        }
        Player opponent = containerOfReadyPlayers
                .findTheNearestOpponent(player);
        if (opponent != null) {
            containerOfReadyPlayers.removePlayer(opponent);
            containerOfReadyPlayers.removePlayer(player);
        }
        return opponent;
    }
}