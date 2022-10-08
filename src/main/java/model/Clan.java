package model;

import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.atomic.AtomicInteger;

// Упрощенный объект клана
@Getter
@Setter
public class Clan {
    private Long id;     // id клана
    private String name; // имя клана
    private AtomicInteger gold;    // текущее количество золота в казне клана

    public Clan() {
    }

    public Clan(Long id, String name, AtomicInteger gold) {
        this.id = id;
        this.name = name;
        this.gold = gold;
    }

    public Clan(String name, AtomicInteger gold) {
        this.name = name;
        this.gold = gold;
    }

    public void addGold(int plus){
       gold.getAndAdd(plus);
    }

    public void minusGold(int minus){
        gold.getAndSet(gold.get() - minus);
    }

    public int getGold() {
        return gold.get();
    }

    @Override
    public String toString() {
        return "Clan{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", gold=" + gold +
                '}';
    }


}
