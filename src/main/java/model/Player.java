package model;

public class Player {
    private static int idCounter = 1;
    private int id;
    private String name;
    private Double rate;

    public Player(String name) {
        this.name = name;
        rate = 0.0;
        id = idCounter++;
    }

    public Player(String name, Double rate) {
        this(name);
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return id == player.id;

    }

    @Override
    public int hashCode() {
        return id;
    }
}
