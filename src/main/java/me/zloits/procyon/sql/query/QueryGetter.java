package me.zloits.procyon.sql.query;

import lombok.*;
import me.zloits.procyon.Procyon;
import me.zloits.procyon.abstracts.StartAbstract;
import me.zloits.procyon.sql.SQLConnection;
import me.zloits.procyon.sql.callback.ResultCallback;
import me.zloits.procyon.util.InstanceRegistry;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * {@link QueryGetter} is a utility for executing SQL queries and retrieving a single result.
 * It simplifies query execution by handling prepared statements and result processing.
 *
 * @param <A> The expected result type.
 */
@RequiredArgsConstructor
@Getter
public class QueryGetter<A> extends StartAbstract {

    private final Procyon procyon = InstanceRegistry.get(Procyon.class).orElseThrow();
    @NonNull private final SQLConnection connection;
    @NonNull private final String query;
    @NonNull private final ResultCallback<A> resultCallback;
    @NonNull private final List<Object> inserts;

    @Setter
    private A result;

    /**
     * Checks if a result is present.
     *
     * @return {@code true} if a result is available, otherwise {@code false}.
     */
    public boolean isPresent() {
        return getResult() != null;
    }

    /**
     * Checks if no result is available.
     *
     * @return {@code true} if no result is available, otherwise {@code false}.
     */
    public boolean isEmpty() {
        return getResult() == null;
    }

    /**
     * Retrieves the result of the query.
     *
     * @return The result, or {@code null} if not available.
     */
    public A get() {
        return getResult();
    }

    /**
     * Executes the query and processes the result using the provided callback.
     *
     * @param <T> The expected return type (typically {@code QueryGetter<A>}).
     * @return The instance of the executing class for method chaining.
     */
    @Override
    public <T extends StartAbstract> T start() {
        try {
            PreparedStatement preparedStatement = getConnection().getConnection().prepareStatement(getQuery());

            for (int i = 0; i < getInserts().size(); i++) {
                preparedStatement.setObject((i + 1), getInserts().get(i));
            }

            setResult(getResultCallback().call(preparedStatement.executeQuery()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (T) this;
    }
}

