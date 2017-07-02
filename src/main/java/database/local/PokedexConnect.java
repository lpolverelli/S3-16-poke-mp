package database.local;

import scala.Tuple2;
import java.sql.*;
import java.util.*;


public final class PokedexConnect {
    private static final int LAST_POKEMON_ID = 151;
    private static final int ZERO = 0;
    private static Connection con;

    private PokedexConnect() {}
    private enum Type {
        String,Tuple2,Integer;
    }

    public static void init() {
        if(con == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                String url = "jdbc:sqlite:src/main/resources/database/pokedex.db";
                con = DriverManager.getConnection(url);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e){
                e.printStackTrace();
            }
        }
    }

    private static Optional<?> executeTheQuery(String sql, Type t,List<String> columns) {
        init();
        try (PreparedStatement pstmt  = con.prepareStatement(sql)){
            ResultSet rs  = pstmt.executeQuery();
            if (rs.next()) {
                switch (t) {
                    case String:
                        return Optional.of(rs.getString(columns.get(0)));
                    case Integer:
                        return Optional.of(rs.getInt(columns.get(0)));
                    case Tuple2:
                        return Optional.of(new Tuple2<>(rs.getString(columns.get(0)),rs.getInt(columns.get(1))));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static Optional<String> getPokemonName(int id){
        String sql = "SELECT identifier FROM pokemon WHERE id = "+id;
        Optional<?> result = executeTheQuery(sql,Type.String, Collections.singletonList("identifier"));
        return result.isPresent() ? (Optional<String>)result : Optional.empty();
    }

    public static Optional<Tuple2<String,Integer>> getPokemonAttack(int id){
        String sql = "SELECT identifier, power FROM moves WHERE id = "+id;
        Optional<?> result = executeTheQuery(sql,Type.Tuple2, Arrays.asList("identifier","power"));
        return result.isPresent() ? (Optional<Tuple2<String,Integer>>)result : Optional.empty();
    }

    public static Tuple2<Boolean,Optional<Integer>> pokemonHasToEvolve(int id, int actualLevel) {
        String sql = "SELECT id FROM pokemon_species WHERE evolves_from_species_id = "+id;
        Optional<?> result = executeTheQuery(sql,Type.Integer, Collections.singletonList("id"));
        if(result.isPresent()){
            int evolvedId = ((Optional<Integer>)result).get();
            sql = "SELECT minimum_level FROM pokemon_evolution WHERE evolved_species_id = "+evolvedId;
            result = executeTheQuery(sql,Type.Integer, Collections.singletonList("minimum_level"));
            if (((Optional<Integer>)result).get() == actualLevel) {
                return new Tuple2<Boolean,Optional<Integer>>(true,Optional.of(evolvedId));
            }
        }
        return new Tuple2<>(false,Optional.empty());
    }

    public static Optional<List<Integer>> getPossibleWildPokemon(int rarity) {
        init();
        String sql = "SELECT id "+
                     "FROM pokemon "+
                     "WHERE pokemon_rarity = "+ rarity +
                     " AND id <= "+ LAST_POKEMON_ID;
        try (PreparedStatement pstmt  = con.prepareStatement(sql)){
            ResultSet rs  = pstmt.executeQuery();
            List<Integer> pokemonList = new ArrayList<>();
            while (rs.next()) {
                pokemonList.add(rs.getInt("id"));
            }
            return Optional.of(pokemonList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static int getMinLevelWildPokemon(int id) {
        String sql = "SELECT minimum_level FROM pokemon_evolution WHERE evolved_species_id = "+id;
        Optional<?> result = executeTheQuery(sql,Type.Integer, Collections.singletonList("minimum_level"));
        return result.isPresent() ? (((Optional<Integer>)result).get()) : 1;
    }

    public static int getMaxLevelWildPokemon(int id) {
        String sql = "SELECT id FROM pokemon_species WHERE evolves_from_species_id = "+id;
        Optional<?> res = executeTheQuery(sql,Type.Integer, Collections.singletonList("id"));
        return res.isPresent() ? getMinLevelWildPokemon((((Optional<Integer>)res).get())) : 70;
    }
}