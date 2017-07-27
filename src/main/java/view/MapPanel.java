package view;

import controller.DistributedMapController;
import controller.GameController;
import distributed.Player;
import distributed.PlayerPositionDetails;
import model.map.Building;
import model.map.GameMap;
import utilities.Settings;

import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapPanel extends GamePanel{

    private GameMap gameMap;
    private GameController mapController;
    private DistributedMapController distributedMapController;

    public MapPanel(GameController mapController, DistributedMapController distributedMapController, GameMap gameMap) {
        super(mapController);
        this.gameMap = gameMap;
        this.mapController = mapController;
        this.distributedMapController = distributedMapController;
    }

    @Override
    protected void doPaint(Graphics g) {
        drawMapElements(g);
        drawOtherTrainers(g);
        drawTrainer(g);
    }

    private void drawMapElements(Graphics g){
        for (int x = 0; x < Settings.MAP_WIDTH(); x++) {
            for (int y = 0; y < Settings.MAP_HEIGHT(); y++) {
                if (!(this.gameMap.map()[x][y] instanceof Building) ||
                        (this.gameMap.map()[x][y] instanceof Building
                                && (((Building) this.gameMap.map()[x][y]).topLeftCoordinate().x() == x)
                                && (((Building) this.gameMap.map()[x][y])).topLeftCoordinate().y() == y)) {
                    g.drawImage(LoadImage.load(this.gameMap.map()[x][y].image()),
                            ((x * Settings.TILE_PIXEL()) - super.getCurrentX()) + Settings.FRAME_SIDE() / 2 ,
                            ((y  * Settings.TILE_PIXEL()) - super.getCurrentY()) + Settings.FRAME_SIDE() / 2 ,
                            null);
                }
            }
        }
    }

    private void drawOtherTrainers(Graphics g){
        ConcurrentMap<Object, PlayerPositionDetails> map = this.distributedMapController.playersPositionDetails();
        if(!this.distributedMapController.playersPositionDetails().isEmpty()){
            for(Player player : this.distributedMapController.connectedPlayers().getAll().values()){
                if(player.isVisible()) {
                    PlayerPositionDetails positionDetails = map.get(player.userId());
                    g.drawImage(LoadImage.load((positionDetails.currentSprite().image())),
                            ((coordinateInPixels(positionDetails.coordinateX())) - super.getCurrentX()) + Settings.FRAME_SIDE() / 2,
                            ((coordinateInPixels(positionDetails.coordinateY())) - super.getCurrentY()) + Settings.FRAME_SIDE() / 2,
                            null);
                }
            }
        }
    }

    private int coordinateInPixels(double currentCoordinate) {
        return (int)(currentCoordinate * Settings.TILE_PIXEL());
    }

    private void drawTrainer(Graphics g){
        g.drawImage(LoadImage.load(mapController.trainer().currentSprite().image()),
                Settings.FRAME_SIDE() / 2,
                Settings.FRAME_SIDE() / 2,
                null);
    }

}
