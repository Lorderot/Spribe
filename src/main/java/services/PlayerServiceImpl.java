package services;

import model.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerServiceImpl implements PlayerService{
    private List<Player> containerOfReadyPlayers = new ArrayList<>();

    @Override
    public boolean registerReadyPlayer(Player player) throws NullPointerException {
        if (player == null) {
            throw new NullPointerException();
        }

        if (containerOfReadyPlayers.contains(player)) {
            return false;
        }

        containerOfReadyPlayers.add(player);
        return true;
    }

    @Override
    public Player findOpponent(Player player) throws NullPointerException {
        if (player == null) {
            throw new NullPointerException();
        }

        Player opponentForPlayer = null;
        for (Player opponent : containerOfReadyPlayers) {

            if (opponent.equals(player)) {
                continue;
            }

            if (opponentForPlayer == null) {
                opponentForPlayer = opponent;
                continue;
            }

            if (Math.abs(opponent.getRate() - player.getRate()) <
                    Math.abs(opponentForPlayer.getRate() - player.getRate())) {
                opponentForPlayer = opponent;
            }
        }

        if (opponentForPlayer != null) {
            containerOfReadyPlayers.remove(opponentForPlayer);
            containerOfReadyPlayers.remove(player);
        }

        return opponentForPlayer;
    }
}