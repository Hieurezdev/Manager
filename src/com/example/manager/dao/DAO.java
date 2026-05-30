package com.example.manager.dao;

import java.sql.Connection;

public class DAO {
    protected Connection con;

    public DAO() {
        this.con = null;
    }

    public DAO(Connection con) {
        this.con = con;
    }

    public Connection getConnection() {
        return con;
    }
}
