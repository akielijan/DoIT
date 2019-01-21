package com.potatoprogrammers.doit;

public class DBService {
    private static DBService dbService;

    public static DBService getInstance() {
        if (dbService == null) {
            dbService = new DBService();
        }
        return dbService;
    }


}
