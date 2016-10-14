package services;

import model.Player;

public interface PlayerService {
    boolean registerReadyPlayer(Player player) throws NullPointerException;

    Player findOpponent(Player player) throws NullPointerException;
}
