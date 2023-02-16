package model.service;

import model.dao.DaoFactory;
import model.dao.LembreteDao;
import model.entities.Lembrete;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LembretesService {

    private LembreteDao dao = DaoFactory.createLembreteDao();


    public void insert(Lembrete obj){
        dao.insert(obj);
    }
    public void update(Lembrete obj){
        dao.updateDateStatus(obj);
    }
    public void updateAll(Lembrete obj){
        dao.updateAll(obj);
    }
    public List<Lembrete> findAll(){
        return dao.findAll();
    }

    public List<Lembrete> findByDate(Date date){
        return dao.findByDate(date);
    }

    public Lembrete findById(Integer id){
        return dao.findById(id);
    }
    public void deleteById(Integer id){
        dao.deleteById(id);
    }

    public Lembrete findByNameAndDate(String name, Date date){
        return dao.findByNameAndDate(name, date);
    }

    public Lembrete findByName(String name){
        return dao.findByName(name);
    }

    public void changeStatus(Lembrete obj){
        dao.changeStatus(obj);
    }


}
