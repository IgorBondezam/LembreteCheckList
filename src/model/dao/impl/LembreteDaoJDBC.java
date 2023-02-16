package model.dao.impl;

import model.entities.Lembrete;
import db.DB;
import db.DbException;
import model.dao.LembreteDao;
import model.entities.Lembrete;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class LembreteDaoJDBC implements LembreteDao {

    private Connection conn;

    public LembreteDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Lembrete obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("INSERT INTO Lembrete " +
                    "(id, name, date, status) " +
                    "VALUES " +
                    "(?, ?, ? , ?)", Statement.RETURN_GENERATED_KEYS);

            st.setInt(1, obj.getId());
            st.setString(2, obj.getName());
            st.setDate(3, new java.sql.Date(obj.getDate().getTime()));
            st.setBoolean(4, obj.getStatus());


            int rowsAffected = st.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    int id = rs.getInt(1);
                    obj.setId(id);
                }
                DB.closeResultSet(rs);
            } else {
                throw new DbException("Unexpected Error! No rows affected!");
            }

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);

        }
    }

    @Override
    public void updateAll(Lembrete obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("UPDATE Lembrete " +
                    "SET Name = ?, date = ?, status = ? " +
                    "WHERE Id = ?", Statement.RETURN_GENERATED_KEYS);

            st.setString(1, obj.getName());
            st.setDate(2, new java.sql.Date(obj.getDate().getTime()));
            st.setBoolean(3, false);
            st.setInt(4, obj.getId());

            st.executeUpdate();


        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);

        }
    }

    public void updateDateStatus(Lembrete obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("UPDATE Lembrete " +
                    "SET status = ?, date = ? " +
                    "WHERE Id = ?", Statement.RETURN_GENERATED_KEYS);

            st.setBoolean(1, obj.getStatus());
            st.setDate(2, new java.sql.Date(obj.getDate().getTime()));

            st.setInt(3, obj.getId());

            st.executeUpdate();


        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);

        }
    }

    @Override
    public void changeStatus(Lembrete obj) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("UPDATE Lembrete " +
                    "SET Status = ? " +
                    "WHERE Id = ?", Statement.RETURN_GENERATED_KEYS);

            st.setBoolean(1, obj.getStatus());

            st.setInt(2, obj.getId());

            st.executeUpdate();


        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);

        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try {
            st = conn.prepareStatement("DELETE FROM Lembrete " +
                    "WHERE Id = ?");

            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.getMessage();
        } finally {
            DB.closeStatement(st);
        }
    }

    @Override
    public Lembrete findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * from Lembrete " +
                            "WHERE Id = ?"
            );

            st.setInt(1, id);
            rs = st.executeQuery();

            if (rs.next()) {
                Lembrete obj = instanciateLembrete(rs);

                return obj;
            }
            return null;

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public Lembrete findByName(String name) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * from Lembrete " +
                            "WHERE Name = ?"
            );

            st.setString(1, name);
            rs = st.executeQuery();

            if (rs.next()) {
                Lembrete obj = instanciateLembrete(rs);

                return obj;
            }
            return null;

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    @Override
    public Lembrete findByNameAndDate(String name, Date date) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * from Lembrete " +
                            "WHERE Name = ? AND date = ?"
            );

            st.setString(1, name);
            st.setDate(2, new java.sql.Date(date.getTime()));
            rs = st.executeQuery();

            if (rs.next()) {
                Lembrete obj = instanciateLembrete(rs);

                return obj;
            }
            return null;

        }catch (SQLException e){
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }



    @Override
    public List<Lembrete> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * from Lembrete " +
                            "ORDER BY Name"

            );


            rs = st.executeQuery();

            List<Lembrete> list = new ArrayList<>();


            while (rs.next()) {

                Lembrete obj = instanciateLembrete(rs);
                list.add(obj);
            }
            return list;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }


    @Override
    public List<Lembrete> findByDate(Date date) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = conn.prepareStatement(
                    "SELECT * from Lembrete " +
                            "WHERE Date = ?"


            );
            st.setDate(1, new java.sql.Date(date.getTime()));
            rs = st.executeQuery();

            List<Lembrete> list = new ArrayList<>();


            while (rs.next()) {

                Lembrete obj = instanciateLembrete(rs);
                list.add(obj);
            }
            return list;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(st);
            DB.closeResultSet(rs);
        }
    }

    private Lembrete instanciateLembrete(ResultSet rs) throws SQLException {
        Lembrete ind = new Lembrete();
        ind.setId(rs.getInt("Id"));
        ind.setName(rs.getString("Name"));
        ind.setDate(rs.getDate("Date"));
        ind.setStatus(rs.getBoolean("Status"));
        return ind;
    }
}
