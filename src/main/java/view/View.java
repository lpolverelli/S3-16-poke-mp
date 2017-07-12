package view;

import controller.BattleController;
import controller.Controller;
import controller.GameController;
import controller.GameViewObserver;
import model.entities.PokemonWithLife;
import model.entities.Trainer;

import javax.swing.*;

public interface View {

    void setController(Controller controller);

    void showMenu();

    void showLogin();

    void showSignIn();

    void showPanel(JPanel gamePanel);

    void showDialogue(JPanel dialoguePanel);

    void showGameMenuPanel(GameViewObserver controller);

    void showBattle(PokemonWithLife myPokemon, PokemonWithLife otherPokemon, BattleController battleController);

    BattleView getBattlePanel();

    void showPokemonChoice(BattleController battleController, Trainer trainer);

    void showPokedex(Trainer trainer, GameViewObserver gameController);

    void showPause();

    void showError(final String error, final String title);

}
