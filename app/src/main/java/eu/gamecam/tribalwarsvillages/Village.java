package eu.gamecam.tribalwarsvillages;

/**
 * Created by Erik Juríček on 29.3.2018.
 */
public class Village {
    private int id;
    private int x;
    private int y;
    private String name;
    private int points;
    private int playerID;
    private int rank;

    public Village(int id, int x, int y, String name, int points, int playerID, int rank) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
        this.points = points;
        this.playerID = playerID;
        this.rank = rank;
    }

    public Village() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getPlayerID() {
        return playerID;
    }

    public void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
