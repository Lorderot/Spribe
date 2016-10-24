package collections;
import model.Player;

import java.util.Random;

/* Random Tree: insertion, deletion, search ~ O(log(N)) with high probability */
public class PlayersContainer {
    private final Node DUMMY_NODE = new Node(null, 0);
    private final int SEED = 1;
    private final Random generator = new Random(SEED);
    private Node root = DUMMY_NODE;

    public void addPlayer(Player player) {
        root = insert(root, player);
    }

    public void removePlayer(Player player) {
        root = delete(root, player);
    }

    public Player findTheNearestOpponent(Player player) {
        Player opponentWithLowerRate =
                findTheNearestOpponentWithLowerOrSameRate(root, player);
        Player opponentWithHigherRate =
                findTheNearestOpponentWithHigherOrSameRate(root, player);
        if (opponentWithLowerRate == null) {
            return opponentWithHigherRate;
        }
        if (opponentWithHigherRate == null) {
            return opponentWithLowerRate;
        }
        if (player.getRate() - opponentWithLowerRate.getRate() <
                opponentWithHigherRate.getRate() - player.getRate()) {
            return opponentWithLowerRate;
        }
        return opponentWithHigherRate;
    }

    public boolean contains(Player player) {
        return find(root, player);
    }

    private Node insert(Node root, Player player) {
        if (root.equals(DUMMY_NODE)) {
            return new Node(player);
        }
        if (generator.nextInt(root.size + 1) == 0) {
            return insertRoot(root, player);
        }
        if (player.getRate() < root.player.getRate()) {
            root.left = insert(root.left, player);
        } else {
            root.right = insert(root.right, player);
        }
        updateSize(root);
        return root;
    }

    private Node insertRoot(Node root, Player player) {
        if (root.equals(DUMMY_NODE)) {
            return new Node(player);
        }
        if (player.getRate() < root.player.getRate()) {
            root.left = insertRoot(root.left, player);
            updateSize(root);
            return rotateRight(root);
        } else {
            root.right = insertRoot(root.right, player);
            updateSize(root);
            return rotateLeft(root);
        }
    }

    private Node rotateLeft(Node root) {
        Node newRoot = root.right;
        root.right = newRoot.left;
        newRoot.left = root;
        newRoot.size = root.size;
        updateSize(root);
        return newRoot;
    }

    private Node rotateRight(Node root) {
        Node newRoot = root.left;
        root.left = newRoot.right;
        newRoot.right = root;
        newRoot.size = root.size;
        updateSize(root);
        return newRoot;
    }

    private Node delete(Node root, Player player) {
        if (root.equals(DUMMY_NODE)) {
            return root;
        }
        if (root.player.getRate().equals(player.getRate())) {
            if (root.player.equals(player)) {
                return merge(root.left, root.right);
            } else {
                boolean isAnyPlayerWithSameRateFromTheLeft = false;
                boolean isAnyPlayerWithSameRateFromTheRight = false;
                if (!root.left.equals(DUMMY_NODE)) {
                    isAnyPlayerWithSameRateFromTheLeft = root.left
                            .player.getRate().equals(player.getRate());
                }
                if (!root.right.equals(DUMMY_NODE)) {
                    isAnyPlayerWithSameRateFromTheRight = root.right
                            .player.getRate().equals(player.getRate());
                }
                if (isAnyPlayerWithSameRateFromTheLeft) {
                    root.left = delete(root.left, player);
                }
                if (isAnyPlayerWithSameRateFromTheRight) {
                    root.right = delete(root.right, player);
                }
            }
        } else {
            if (player.getRate() < root.player.getRate()) {
                root.left = delete(root.left, player);
            } else {
                root.right = delete(root.right, player);
            }
        }
        return root;
    }

    private Node merge(Node leftTree, Node rightTree) {
        if (leftTree.equals(DUMMY_NODE)) {
            return rightTree;
        }
        if (rightTree.equals(DUMMY_NODE)) {
            return leftTree;
        }
        if (generator.nextInt(leftTree.size + rightTree.size)
                > leftTree.size) {
            rightTree.left = merge(leftTree, rightTree.left);
            updateSize(rightTree);
            return rightTree;
        } else {
            leftTree.right = merge(leftTree.right, rightTree);
            updateSize(leftTree);
            return leftTree;
        }
    }

    private Player findTheNearestOpponentWithLowerOrSameRate(Node root, Player player) {
        if (root.equals(DUMMY_NODE)) {
            return DUMMY_NODE.player;
        }
        if (root.player.equals(player)) {
            return definePlayerWithHigherRate
                    (findTheNearestOpponentWithLowerOrSameRate(root.left, player),
                            findTheNearestOpponentWithLowerOrSameRate(root.right, player));
        }
        if (root.player.getRate().equals(player.getRate())) {
            return root.player;
        }
        if (root.player.getRate() < player.getRate()) {
            return definePlayerWithHigherRate(root.player,
                    findTheNearestOpponentWithLowerOrSameRate(root.right, player));
        } else {
            return findTheNearestOpponentWithLowerOrSameRate(root.left, player);
        }
    }

    private Player findTheNearestOpponentWithHigherOrSameRate(Node root, Player player) {
        if (root.equals(DUMMY_NODE)) {
            return DUMMY_NODE.player;
        }
        if (root.player.equals(player)) {
            return definePlayerWithLowerRate
                    (findTheNearestOpponentWithHigherOrSameRate(root.left, player),
                            findTheNearestOpponentWithHigherOrSameRate(root.right, player));
        }
        if (root.player.getRate().equals(player.getRate())) {
            return root.player;
        }
        if (root.player.getRate() < player.getRate()) {
            return findTheNearestOpponentWithHigherOrSameRate(root.right, player);
        } else {
            return definePlayerWithLowerRate(
                    findTheNearestOpponentWithHigherOrSameRate(root.left, player),
                    root.player);
        }
    }

    private Player definePlayerWithHigherRate(Player player1, Player player2) {
        if (player1 == null) {
            return  player2;
        }
        if (player2 == null) {
            return player1;
        }
        if (player1.getRate() > player2.getRate()) {
            return player1;
        }
        return player2;
    }

    private Player definePlayerWithLowerRate(Player player1, Player player2) {
        if (player1 == null) {
            return  player2;
        }
        if (player2 == null) {
            return player1;
        }
        if (player1.getRate() < player2.getRate()) {
            return player1;
        }
        return player2;
    }

    private boolean find(Node root, Player player) {
        if (root.equals(DUMMY_NODE)) {
            return false;
        }
        if (player.equals(root.player)) {
            return true;
        }
        if (player.getRate().equals(root.player.getRate())) {
            return find(root.left, player) || find(root.right, player);
        }
        if (player.getRate() < root.player.getRate()) {
            return find(root.left, player);
        } else {
            return find(root.right, player);
        }
    }

    private void updateSize(Node node) {
        node.size = node.left.size + node.right.size + 1;
    }

    private class Node {
        private Player player;
        private int size;
        private Node left;
        private Node right;

        private Node(Player player) {
            this.player = player;
            size = 1;
            left = DUMMY_NODE;
            right = DUMMY_NODE;
        }

        private Node(Player player, int size) {
            this.player = player;
            this.size = size;
            this.left = this;
            this.right = this;
        }
    }
}