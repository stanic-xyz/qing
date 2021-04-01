package chenyunlong.zhangli.model.entities;

import java.io.Serializable;
import java.util.Date;

public class Sign implements Serializable {

    private Long id;
    private Long userId;
    private Date dateMonth;
    private Integer mask;
    private Integer continueSignMonth;


    public Sign() {
    }

    public Sign(Long id, Long userId, Date dateMonth, int mask, int continueSignMonth) {
        this.id = id;
        this.userId = userId;
        this.dateMonth = dateMonth;
        this.mask = mask;
        this.continueSignMonth = continueSignMonth;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(Date dateMonth) {
        this.dateMonth = dateMonth;
    }

    public Integer getMask() {
        return mask;
    }

    public void setMask(Integer mask) {
        this.mask = mask;
    }

    public Integer getContinueSignMonth() {
        return continueSignMonth;
    }

    public void setContinueSignMonth(Integer continueSignMonth) {
        this.continueSignMonth = continueSignMonth;
    }
}
