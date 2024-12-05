package me.zloits.procyon.sql.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.zloits.procyon.Procyon;
import me.zloits.procyon.abstracts.StartAbstract;
import me.zloits.procyon.sql.SQLConnection;
import me.zloits.procyon.sql.callback.ExecutorCallback;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@AllArgsConstructor
@Getter
public class QueryExecutor extends StartAbstract {

    private final Procyon procyon = Procyon.getProcyon();

    private SQLConnection connection;
    private String query;
    private ExecutorCallback executorCallback;
    private List<Object> inserts;

    @Override
    public <T extends StartAbstract> T start() {
        try {
            PreparedStatement preparedStatement = getConnection().getConnection().prepareStatement(getQuery());

            for (int i = 0; i < getInserts().size(); i++) {
                preparedStatement.setObject((i + 1), getInserts().get(i));
            }

            preparedStatement.executeUpdate();

            getProcyon().getExecutorService().execute(getExecutorCallback()::call);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (T) this;
    }
}
