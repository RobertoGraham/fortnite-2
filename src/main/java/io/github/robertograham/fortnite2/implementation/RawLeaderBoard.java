package io.github.robertograham.fortnite2.implementation;

import io.github.robertograham.fortnite2.domain.LeaderBoardEntry;

import javax.json.JsonObject;
import javax.json.bind.adapter.JsonbAdapter;
import java.util.List;
import java.util.Objects;

final class RawLeaderBoard {

    private final List<LeaderBoardEntry> leaderBoardEntries;

    private RawLeaderBoard(final List<LeaderBoardEntry> leaderBoardEntries) {
        this.leaderBoardEntries = leaderBoardEntries;
    }

    List<LeaderBoardEntry> leaderBoardEntries() {
        return leaderBoardEntries;
    }

    @Override
    public String toString() {
        return "RawLeaderBoard{" +
            "leaderBoardEntries=" + leaderBoardEntries +
            '}';
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object)
            return true;
        if (!(object instanceof RawLeaderBoard))
            return false;
        final RawLeaderBoard rawLeaderBoard = (RawLeaderBoard) object;
        return leaderBoardEntries.equals(rawLeaderBoard.leaderBoardEntries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leaderBoardEntries);
    }

    enum Adapter implements JsonbAdapter<RawLeaderBoard, JsonObject> {

        INSTANCE;

        @Override
        public JsonObject adaptToJson(final RawLeaderBoard rawLeaderBoard) {
            throw new UnsupportedOperationException();
        }

        @Override
        public RawLeaderBoard adaptFromJson(final JsonObject jsonObject) {
            return new RawLeaderBoard(
                jsonObject.getJsonArray("entries")
                    .getValuesAs(jsonValue -> {
                        final JsonObject leaderBoardEntryJsonObject = jsonValue.asJsonObject();
                        return new DefaultLeaderBoardEntry(
                            leaderBoardEntryJsonObject.getString("accountId"),
                            leaderBoardEntryJsonObject.getJsonNumber("value")
                                .longValueExact()
                        );
                    })
            );
        }
    }
}
