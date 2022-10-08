package model;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;

@Data
public class Transaction {
    private Long id;
    private Long clanId;
    private Date date;
    private int baseGold;
    private int operationGold;
    private boolean status;

    public Transaction(Long clanId, Date date, int baseGold, int operationGold) {
        this.clanId = clanId;
        this.date = date;
        this.baseGold = baseGold;
        this.operationGold = operationGold;
        this.status = false;
    }

    public Transaction(Long id, Long clanId, Date date, int baseGold, int operationGold, boolean status) {
        this.id = id;
        this.clanId = clanId;
        this.date = date;
        this.baseGold = baseGold;
        this.operationGold = operationGold;
        this.status = status;
    }
}