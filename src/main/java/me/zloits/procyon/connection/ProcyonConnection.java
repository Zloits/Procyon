package me.zloits.procyon.connection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class ProcyonConnection {

    private final List<IConnection> connections = new ArrayList<>();

    @NonNull
    public IConnection getConnection(@NonNull String primaryName) {
        for (IConnection connectionAbstract : getConnections()) {
            if (connectionAbstract.getPrimaryName().equals(primaryName)) {
                return connectionAbstract;
            }
        } return null;
    }
}
