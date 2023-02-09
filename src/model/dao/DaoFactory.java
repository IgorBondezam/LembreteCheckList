package model.dao;

import db.DB;
import model.dao.impl.LembreteDaoJDBC;

public class DaoFactory {

    public static LembreteDao createLembreteDao(){
        return new LembreteDaoJDBC(DB.getConnection());
    }

}
