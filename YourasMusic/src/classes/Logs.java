package classes;
// Generated 13/jun/2018 18:32:28 by Hibernate Tools 4.3.1


import java.math.BigDecimal;

/**
 * Logs generated by hbm2java
 */
public class Logs  implements java.io.Serializable {


     private BigDecimal logId;
     private String descricao;

    public Logs() {
    }

    public Logs(BigDecimal logId, String descricao) {
       this.logId = logId;
       this.descricao = descricao;
    }
   
    public BigDecimal getLogId() {
        return this.logId;
    }
    
    public void setLogId(BigDecimal logId) {
        this.logId = logId;
    }
    public String getDescricao() {
        return this.descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }




}

