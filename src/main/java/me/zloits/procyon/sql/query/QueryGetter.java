package me.zloits.procyon.sql.query;

import lombok.*;
import me.zloits.procyon.Procyon;
import me.zloits.procyon.abstracts.StartAbstract;
import me.zloits.procyon.sql.SQLConnection;
import me.zloits.procyon.sql.callback.ResultCallback;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RequiredArgsConstructor
@Getter
public class QueryGetter<A> extends StartAbstract {

    private final Procyon procyon = Procyon.getProcyon();
    @NonNull
    private final SQLConnection connection;
    @NonNull
    private final String query;
    @NonNull
    private final ResultCallback<A> resultCallback;
    @NonNull
    private final List<Object> inserts;

    @Setter
    private A result;

    public boolean isPresent() {
        return getResult() != null;
    }

    public boolean isEmpty() {
        return getResult() == null;
    }

    public A get() {
        return getResult();
    }

    @Override
    public <T extends StartAbstract> T start() {
        try {
            PreparedStatement preparedStatement = getConnection().getConnection().prepareStatement(getQuery());

            for (int i = 0; i < getInserts().size(); i++) {
                preparedStatement.setObject((i + 1), getInserts().get(i));
            }

            getProcyon().getExecutorService().execute(() -> {
                try {
                    getResultCallback().call(preparedStatement.executeQuery());
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return (T) this;
    }
}
