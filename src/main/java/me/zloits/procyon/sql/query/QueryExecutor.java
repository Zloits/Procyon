package me.zloits.procyon.sql.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.zloits.procyon.abstracts.StartAbstract;
import me.zloits.procyon.sql.SQLConnection;
import me.zloits.procyon.sql.callback.ExecutorCallback;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * {@link QueryExecutor} is a utility for executing SQL update queries such as INSERT, UPDATE, or DELETE.
 * It prepares and executes the query while allowing a callback to be executed upon completion.
 */
@AllArgsConstructor
@Getter
public class QueryExecutor extends StartAbstract {

    private SQLConnection connection;
    private String query;
    private ExecutorCallback executorCallback;
    private List<Object> inserts;

    private boolean next;

    /**
     * Executes the query and triggers the callback upon completion.
     *
     * @param <T> The expected return type (typically {@code QueryExecutor} for method chaining).
     * @return The instance of the executing class for method chaining.
     */
    @Override
    public <T extends StartAbstract> T start() {
        try {
            PreparedStatement preparedStatement = getConnection().connection().prepareStatement(getQuery());

            for (int i = 0; i < getInserts().size(); i++) {
                preparedStatement.setObject((i + 1), getInserts().get(i));
            }

            preparedStatement.executeUpdate();

            next = true;
            getExecutorCallback().call();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (T) this;
    }

    public boolean next() {
        return next;
    }
}
