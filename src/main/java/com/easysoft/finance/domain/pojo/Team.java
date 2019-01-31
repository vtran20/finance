package com.easysoft.finance.domain.pojo;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vutran on 11/6/2018.
 */
public class Team implements Comparable <Team> {
    List players = new ArrayList();
    public void add (Player player) {
        players.add(player);
    }
    public float getTotalPoint () {
        float thisTotal = 0;
        List l = this.getPlayers();
        for (int i=0; i<l.size(); i++) {
            thisTotal += ((Player)l.get(i)).getTotal();
        }
        return thisTotal;
    }
    public String getNames () {
        String result = "";
        List l = this.getPlayers();
        for (int i=0; i<l.size(); i++) {
            if (StringUtils.isEmpty(result)) {
                result = ((Player)l.get(i)).getName();
            } else {
                result += ", " + ((Player)l.get(i)).getName();
            }
        }
        return result;
    }

    public List getPlayers() {
        return players;
    }

    public void setPlayers(List players) {
        this.players = players;
    }

    @Override
    public int compareTo(Team o) {
        float thisTotal = 0;
        List l = this.getPlayers();
        for (int i=0; i<l.size(); i++) {
            thisTotal += ((Player)l.get(i)).getTotal();
        }

        float oTotal = 0;
        if (o != null) {
            l = o.getPlayers();
            for (int i=0; i<l.size(); i++) {
                oTotal += ((Player)l.get(i)).getTotal();
            }
        }
        float temp = oTotal - thisTotal;
        if (temp > 0) return 1;
        else if (temp < 0) return -1;
        else return 0;

    }
}

