package model.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Lembrete implements Serializable {


    private Integer id;
    private String name;
    private Date date;

    private Boolean status;
    public Lembrete(){

    }

    public Lembrete(Integer id, String name, Date date, Boolean status) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lembrete that)) return false;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }

    @Override
    public String toString() {
        return "LembreteIndividual{" +
                "name='" + name + '\'' +
                ", date=" + date +
                '}';
    }
}
