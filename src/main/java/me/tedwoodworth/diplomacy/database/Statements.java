package me.tedwoodworth.diplomacy.database;

public class Statements {
    public static final String createMarketTable =
            "CREATE TABLE IF NOT EXISTS market (" +
                    "market_id VARCHAR(32) NOT NULL," +
                    "market_name VARCHAR(32) NOT NULL," +
                    "PRIMARY KEY (market_id)" +
                    ");";

    public static final String selectPlayer =
            "SELECT * FROM player WHERE uuid=?;";


    public static final String createChunkTable =
            "CREATE TABLE IF NOT EXISTS chunk (" +
                    "world VARCHAR(32) NOT NULL," +
                    "x NUMERIC(4) NOT NULL," +
                    "z NUMERIC(4) NOT NULL," +
                    "is_neutral VARCHAR(5) check (is_neutral in ('True', 'False'))," +
                    "type VARCHAR(32)," +
                    "PRIMARY KEY (world, x, z)" +
                    ");";

    public static final String createPlayerTable =
            "CREATE TABLE IF NOT EXISTS player (" +
                    "uuid VARCHAR(200) NOT NULL," +
                    "balance NUMERIC(14, 2)," +
                    "PRIMARY KEY (uuid)" +
                    ");";

    public static final String insertPlayer =
            "INSERT INTO player (uuid, balance)" +
                    "VALUES (?, ?);";

    public static final String selectBalance =
            "SELECT balance FROM player WHERE uuid=?;";

    public static final String createCoordinateTable =
            "CREATE TABLE IF NOT EXISTS coordinate (" +
                    "world VARCHAR(32) NOT NULL," +
                    "x NUMERIC(7,2) NOT NULL," +
                    "y NUMERIC(7,2) NOT NULL," +
                    "z NUMERIC(7,2) NOT NULL," +
                    "PRIMARY KEY (world, x, y, z)" +
                    ");";

    public static final String createTownTable =
            "CREATE TABLE IF NOT EXISTS town (" +
                    "town_id VARCHAR(32) NOT NULL," +
                    "town_name VARCHAR(32) NOT NULL," +
                    "town_balance NUMERIC(14, 2) NOT NULL," +
                    "is_open VARCHAR(5) check (is_open in ('True', 'False')) NOT NULL," +
                    "PRIMARY_KEY (town_id)" +
                    ");";

    public static final String createNationTable =
            "CREATE TABLE IF NOT EXISTS town (" +
                    "nation_id VARCHAR(32) NOT NULL," +
                    "nation_name VARCHAR(32) NOT NULL," +
                    "nation_balance NUMERIC(14, 2) NOT NULL," +
                    "is_open VARCHAR(5) check (is_open in ('True', 'False')) NOT NULL," +
                    "PRIMARY_KEY (town_id)" +
                    ");";

    public static final String createTownMembers =
            "CREATE TABLE IF NOT EXISTS town_members (" +
                    "town_id VARCHAR(32) NOT NULL," +
                    "uuid VARCHAR(200) NOT NULL," +
                    "rank VARCHAR(32) NOT NULL," +
                    "PRIMARY KEY (town_id, uuid)," +
                    "FOREIGN KEY (town_id) REFERENCES town ON DELETE CASCADE," +
                    "FOREIGN KEY (uuid) REFERENCES player ON DELETE CASCADE" +
                    ");";

    public static final String createNationTowns =
            "CREATE TABLE IF NOT EXISTS nation_towns (" +
                    "nation_id VARCHAR(32) NOT NULL," +
                    "town_id VARCHAR(32) NOT NULL," +
                    "PRIMARY KEY (nation_id, town_id)," +
                    "FOREIGN KEY (nation_id) REFERENCES nation ON DELETE CASCADE," +
                    "FOREIGN KEY (town_id) REFERENCES town ON DELETE CASCADE" +
                    ");";

    public static final String createNationOfficers =
            "CREATE TABLE IF NOT EXISTS nation_officers (" +
                    "nation_id VARCHAR(32) NOT NULL," +
                    "uuid VARCHAR(200) NOT NULL," +
                    "rank VARCHAR(32) NOT NULL," +
                    "PRIMARY KEY (nation_id, uuid)," +
                    "FOREIGN KEY (nation_id) REFERENCES nation ON DELETE CASCADE," +
                    "FOREIGN KEY (uuid) REFERENCES player ON DELETE CASCADE" +
                    ");";


    public static final String createChunkResidents =
            "CREATE TABLE IF NOT EXISTS chunk_residents (" +
                    "world VARCHAR(32) NOT NULL," +
                    "x NUMERIC(4) NOT NULL," +
                    "z NUMERIC(4) NOT NULL," +
                    "uuid VARCHAR(200) NOT NULL," +
                    "PRIMARY KEY (world, x, z, uuid)," +
                    "FOREIGN KEY (world, x, z) REFERENCES chunk ON DELETE CASCADE," +
                    "FOREIGN KEY (uuid) REFERENCES player ON DELETE CASCADE" +
                    ");";

    public static final String createTownChunk =
            "CREATE TABLE IF NOT EXISTS town_chunk (" +
                    "town_id VARCHAR(32) NOT NULL," +
                    "world VARCHAR(32) NOT NULL," +
                    "x NUMERIC(4) NOT NULL," +
                    "z NUMERIC(4) NOT NULL," +
                    "PRIMARY KEY (town_id, world, x, z)," +
                    "FOREIGN KEY (town_id) REFERENCES town ON DELETE CASCADE," +
                    "FOREIGN KEY (world, x, z) REFERENCES chunk ON DELETE CASCADE" +
                    ");";

    public static final String createNationChunk =
            "CREATE TABLE IF NOT EXISTS nation_chunk (" +
                    "nation_id VARCHAR(32) NOT NULL," +
                    "world VARCHAR(32) NOT NULL," +
                    "x NUMERIC(4) NOT NULL," +
                    "z NUMERIC(4) NOT NULL," +
                    "PRIMARY KEY (nation_id, world, x, z)," +
                    "FOREIGN KEY (nation_id) REFERENCES nation ON DELETE CASCADE," +
                    "FOREIGN KEY (world, x, z) REFERENCES chunk ON DELETE CASCADE" +
                    ");";

    public static final String createPlayerLocations =
            "CREATE TABLE IF NOT EXISTS player_locations (" +
                    "uuid VARCHAR(200) NOT NULL," +
                    "world VARCHAR(32) NOT NULL," +
                    "x NUMERIC(7,2) NOT NULL," +
                    "y NUMERIC(7,2) NOT NULL," +
                    "z NUMERIC(7,2) NOT NULL," +
                    "FOREIGN KEY (uuid) REFERENCES player ON DELETE CASCADE," +
                    "FOREIGN KEY (world, x, y, z) REFERENCES coordinate ON DELETE CASCADE" +
                    ");";

//    public static final String create[name] =
//            "CREATE TABLE IF NOT EXISTS [table] (" +
//                    "[param] [type] NOT NULL," +
//                    "[param] [type] NOT NULL," +
//                    "PRIMARY KEY ([])," +
//                    "FOREIGN KEY () REFERENCES [] ON DELETE CASCADE," +
//                    "FOREIGN KEY () REFERENCES [] ON DELETE CASCADE" +
//                    ");";


}
