package model.dao;


import model.entities.Lembrete;

import java.util.Date;
import java.util.List;

public interface LembreteDao {

    void insert(Lembrete obj);
    void update(Lembrete obj);
    void changeStatus(Lembrete obj);
    void deleteById(Integer id);
    Lembrete findById(Integer id);
    Lembrete findByName(String name);
    List<Lembrete> findAll();
    List<Lembrete> findByDate(Date date);

}
