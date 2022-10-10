package model;

import lombok.Data;

import java.sql.Date;

@Data
public class Transaction {
    private Long id;
    private Long clanId;
    private Date date;
    private int baseGold;
    private int operationGold;
    private String text;
    private boolean status;

    public Transaction(Long clanId, Date date, int baseGold, int operationGold, String text) {
        this.clanId = clanId;
        this.date = date;
        this.baseGold = baseGold;
        this.operationGold = operationGold;
        this.text = text;
        this.status = false;
    }

    public Transaction(Long id, Long clanId, Date date, int baseGold, int operationGold,String text, boolean status) {
        this.id = id;
        this.clanId = clanId;
        this.date = date;
        this.baseGold = baseGold;
        this.operationGold = operationGold;
        this.text = text;
        this.status = status;
    }
}