package services;

import model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlayerServiceTest {
    private PlayerService playerService;

    @Before
    public void initialize() {
        playerService = new PlayerServiceImpl();
    }

    @Test(expected = NullPointerException.class)
    public void registerReadyPlayer_NullPlayer() {
        playerService.registerReadyPlayer(null);
    }

    @Test
    public void registerReadyPlayer_ForUnregisteredPlayerReturnValue() {
        Player player = new Player("name");
        boolean result = playerService.registerReadyPlayer(player);
        Assert.assertTrue(result);
    }

    @Test
    public void registerReadyPlayer_ForRegisteredPlayerReturnValue() {
        Player player = new Player("name");
        playerService.registerReadyPlayer(player);
        boolean result = playerService.registerReadyPlayer(player);
        Assert.assertFalse(result);
    }

    @Test
    public void registerReadyPlayer_IsPlayerAddedToReadyPlayersContainerTwice() {
        Player player = new Player("newbie");
        playerService.registerReadyPlayer(player);
        playerService.registerReadyPlayer(player);
        Player opponent = playerService.findOpponent(player);
        Assert.assertNull(opponent);
    }

    @Test(expected = NullPointerException.class)
    public void findOpponent_NullPlayer() {
        playerService.findOpponent(null);
    }

    @Test
    public void findOpponent_ForUnregisteredPlayer() {
        Player player1 = new Player("newbie");
        Player player2 = new Player("opponent");
        playerService.registerReadyPlayer(player2);
        Player opponent =  playerService.findOpponent(player1);
        Assert.assertNotNull(opponent);
    }

    @Test
    public void findOpponent_NoReadyPlayerReturnValue() {
        Player opponent = playerService.findOpponent(new Player("newbie"));
        Assert.assertNull(opponent);
    }

    @Test
    public void findOpponent_IsOpponentPlayerDeletedFromReadyPlayersContainer() {
        Player opponent = new Player("opponent");
        Player player = new Player("newbie");
        playerService.registerReadyPlayer(opponent);
        playerService.findOpponent(player);
        boolean isNotRegistered = playerService.registerReadyPlayer(opponent);
        Assert.assertTrue(isNotRegistered);
    }

    @Test
    public void findOpponent_WillInputPlayerBeDeletedIfOpponentIsFound() {
        Player player1 = new Player("newbie");
        Player player2 = new Player("opponent");
        playerService.registerReadyPlayer(player1);
        playerService.registerReadyPlayer(player2);
        playerService.findOpponent(player1);
        boolean isNotRegistered = playerService.registerReadyPlayer(player1);
        Assert.assertTrue(isNotRegistered);
    }

    @Test
    public void findOpponent_WillInputPlayerBeDeletedIfOpponentIsNotFound() {
        Player player1 = new Player("newbie");
        playerService.registerReadyPlayer(player1);
        playerService.findOpponent(player1);
        boolean isNotRegistered = playerService.registerReadyPlayer(player1);
        Assert.assertFalse(isNotRegistered);
    }

    @Test
    public void findOpponent_WithLowerNearestRate() {
        Player player = new Player("Player", 2000.0);
        Player opponent1 = new Player("Opponent1", 1995.0);
        Player opponent2 = new Player("Opponent2", 2005.1);
        playerService.registerReadyPlayer(player);
        playerService.registerReadyPlayer(opponent1);
        playerService.registerReadyPlayer(opponent2);
        Player opponent = playerService.findOpponent(player);
        Assert.assertEquals(opponent, opponent1);
    }

    @Test
    public void findOpponent_WithHigherNearestRate() {
        Player player = new Player("Player", 2000.0);
        Player opponent1 = new Player("Opponent1", 1995.0);
        Player opponent2 = new Player("Opponent2", 2004.9);
        playerService.registerReadyPlayer(player);
        playerService.registerReadyPlayer(opponent1);
        playerService.registerReadyPlayer(opponent2);
        Player opponent = playerService.findOpponent(player);
        Assert.assertEquals(opponent2, opponent);
    }

    @Test
    public void findOpponent_MultipleNearestOpponents() {
        Player player = new Player("Player", 2000.0);
        Player opponent1 = new Player("Opponent1", 1995.0);
        Player opponent2 = new Player("Opponent2", 2005.0);
        playerService.registerReadyPlayer(player);
        playerService.registerReadyPlayer(opponent1);
        playerService.registerReadyPlayer(opponent2);
        Player opponent = playerService.findOpponent(player);
        Assert.assertNotNull(opponent);
    }

    @Test
    public void findOpponent_MultipleOpponentsWithSameRate() {
        Player player = new Player("Player", 2000.0);
        Player opponent1 = new Player("Opponent1", 2000.0);
        Player opponent2 = new Player("Opponent2", 2000.0);
        playerService.registerReadyPlayer(player);
        playerService.registerReadyPlayer(opponent1);
        playerService.registerReadyPlayer(opponent2);
        Player opponent = playerService.findOpponent(player);
        boolean notNullOpponent = opponent1.equals(opponent)
                || opponent2.equals(opponent);
        boolean opponentDeleted = !(playerService.registerReadyPlayer(opponent1)
                && playerService.registerReadyPlayer(opponent2));
        Assert.assertTrue(notNullOpponent && opponentDeleted);
    }

    @Test
    public void findOpponent_HugeAmountOfOpponentsWithSameRate() {
        int numberOfOpponents = 10000;
        for (int i = 0; i < numberOfOpponents; i++) {
            playerService.registerReadyPlayer(new Player("Nickname", 2000.0));
        }
        Player player = new Player("Nickname", 2000.0);
        for (int i = 0; i < numberOfOpponents; i++) {
            Player opponent = playerService.findOpponent(player);
            Assert.assertNotNull(opponent);
        }
    }

}