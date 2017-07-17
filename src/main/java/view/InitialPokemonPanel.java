package view;

import controller.GameController;
import database.remote.DBConnect;
import model.entities.PokemonBehaviour;
import model.entities.PokemonBehaviourImpl;
import model.entities.PokemonWithLife;

import javax.swing.*;
import java.awt.*;

public class InitialPokemonPanel extends JPanel {

    public InitialPokemonPanel(final GameController buildingController, final PokemonWithLife pokemonWithLife){
        setLayout(new BorderLayout());
        final PokemonPanel pokemonPanel = new PokemonPanel();
        pokemonPanel.setPokemon(pokemonWithLife);
        add(pokemonPanel, BorderLayout.CENTER);

        final JPanel buttonPanel = new JPanel();
        final JButton yes = new JButton("yes");
        final JButton no = new JButton("no");
        yes.addActionListener(e ->{
            int autoIncrementCaptured = DBConnect.getAutoIncrement("pokemon");
            final PokemonBehaviour pokemonBehaviour = new PokemonBehaviourImpl(pokemonWithLife);
            buildingController.trainer().addMetPokemon(pokemonWithLife.pokemon().id());
            pokemonBehaviour.insertPokemonIntoDB(buildingController.trainer().id());
            buildingController.trainer().updateTrainer(0);
            buildingController.trainer().addFavouritePokemon(autoIncrementCaptured);
            buildingController.resume();
        });
        no.addActionListener(e -> buildingController.resume());
        buttonPanel.add(new JLabel("Do you choose this Pokémon?"), buttonPanel);
        buttonPanel.add(yes, buttonPanel);
        buttonPanel.add(no, buttonPanel);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}
