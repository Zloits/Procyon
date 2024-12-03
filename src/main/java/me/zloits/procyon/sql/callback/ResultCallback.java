package me.zloits.procyon.sql.callback;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultCallback<T> {

    T call(ResultSet resultSet) throws SQLException;

}
