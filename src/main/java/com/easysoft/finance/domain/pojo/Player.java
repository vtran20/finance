package com.easysoft.finance.domain.pojo;

/**
 * Created by vutran on 11/6/2018.
 */
public class Player implements Comparable<Player>{
    private String name;
    private boolean join;
    private String attack;
    private String defense;
    private String performance;
    private float total;

    public Player () {}
    public Player (String name, String attack, String defense) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.join = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isJoin() {
        return join;
    }

    public void setJoin(boolean join) {
        this.join = join;
    }
    public void setJoin(String join) {
        if ("on".equalsIgnoreCase(join) || "true".equalsIgnoreCase(join) || "y".equalsIgnoreCase(join)) {
            this.join = true;
        } else {
            this.join = false;
        }

    }

    public String getAttack() {
        return attack;
    }

    public void setAttack(String attack) {
        this.attack = attack;
    }

    public String getDefense() {
        return defense;
    }

    public void setDefense(String defense) {
        this.defense = defense;
    }

    public String getPerformance() {
        return performance;
    }

    public void setPerformance(String performance) {
        this.performance = performance;
    }

    public float getTotal() {
        float temp = 0;
        if (attack != null) {
            temp += Float.parseFloat(attack);
        }
        if (defense != null) {
            temp += Float.parseFloat(defense);
        }
        if (performance != null) {
            temp += Float.parseFloat(performance);
        }
        return temp;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    @Override
    public int compareTo(Player o) {
        if (o == null) return -1;
        float temp = o.getTotal() - this.getTotal();
        if (temp > 0) return 1;
        else if (temp < 0) return -1;
        else return 0;
    }
}
