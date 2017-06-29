package model.map

import model.environment.CoordinateImpl
import utilities.Settings

case class InitialTownElements() extends MapElementsImpl {

  addBuildings()
  addLakes()
  addTallGrass()
  addTrees()

  addCompositeElement(Square(), CoordinateImpl(21,21), CoordinateImpl(28,28))
  addRoadFromSquareToPokemonCenter()
  addRoadFromSquareToLaboratory()
  addRoadFromSquareToBottomLeftLake()
  addRoadFromSquareToTopTallGrass()

  private def addBuildings(): Unit ={
    addTile(PokemonCenter(CoordinateImpl(10,19)), CoordinateImpl(10,19))
    addTile(Laboratory(CoordinateImpl(40,20)), CoordinateImpl(40,20))
  }

  private def addTrees(): Unit ={
    for (x <- 0 until Settings.MAP_WIDTH)
      for (y <- 0 until Settings.MAP_HEIGHT)
        if (x == 0 || x == Settings.MAP_WIDTH - 1 || y == 0 || y == Settings.MAP_HEIGHT - 1) addTile(Tree(), CoordinateImpl(x,y))

    for (x <- 1 to 14)
      addTile(Tree(), CoordinateImpl(x,18))

    for (y <- 18 to 23)
      addTile(Tree(), CoordinateImpl(20,y))

    for (y <- 28 to 31) {
      addTile(Tree(), CoordinateImpl(9, y))
      addTile(Tree(), CoordinateImpl(20, y))
    }

    for (x <- 10 to 11)
      addTile(Tree(), CoordinateImpl(x,31))
    for (x <- 18 to 19)
      addTile(Tree(), CoordinateImpl(x,31))

    for (x <- 15 to 16)
      for(y <- 18 to 23)
      addTile(Tree(), CoordinateImpl(x,y))

    for (x <- 43 to 49)
      for (y <- 1 to 4)
        addTile(Tree(), CoordinateImpl(x,y))

    val trees = Seq[CoordinateImpl](CoordinateImpl(39,23), CoordinateImpl(47,23), CoordinateImpl(29, 23), CoordinateImpl(34,23))
    addTileInMultipleCoordinates(Tree(), trees)
  }

  private def addLakes(): Unit ={
    addCompositeElement(Lake(),CoordinateImpl(4,5), CoordinateImpl(20,10))
    addCompositeElement(Lake(),CoordinateImpl(40,40), CoordinateImpl(48,48))
    addCompositeElement(Lake(),CoordinateImpl(10,28), CoordinateImpl(19,30))
    addCompositeElement(Lake(),CoordinateImpl(10,42), CoordinateImpl(11,44))
  }

  private def addTallGrass(): Unit ={

    for (x <- 1 to 42)
      for (y <- 1 to 4)
        addTile(TallGrass(), CoordinateImpl(x,y))

    for (x <- 1 to 3)
      for (y <- 5 to 10)
        addTile(TallGrass(), CoordinateImpl(x,y))

    for (x <- 1 to 9)
      for (y <- 19 to 23)
        addTile(TallGrass(), CoordinateImpl(x,y))

    for (x <- 1 to 8)
      for (y <- 24 to 27)
        addTile(TallGrass(), CoordinateImpl(x,y))

    for (x <- 1 to 8)
      for (y <- 28 to 31)
        addTile(TallGrass(), CoordinateImpl(x,y))

    for (x <- 37 to 49)
      for (y <- 5 to 11)
        addTile(TallGrass(), CoordinateImpl(x,y))

    for (x <- 17 to 19)
      for (y <- 18 to 23)
        addTile(TallGrass(), CoordinateImpl(x,y))

    for (y <- 1 to 8)
      addTile(TallGrass(), CoordinateImpl(38,y))

    for (x <- 36 to 37)
      for (y <- 1 to 5)
        addTile(TallGrass(), CoordinateImpl(x,y))

    for (x <- 42 to 48)
      for (y <- 11 to 13)
        addTile(TallGrass(), CoordinateImpl(x,y))
  }

  private def addRoadFromSquareToPokemonCenter(): Unit = {
    for (x <- 9 to 21)  x match {
      case 9 =>
        addTile(RoadMarginTopLeft(),CoordinateImpl(x,24))
        addTile(RoadMarginLeft(), CoordinateImpl(x,25))
        addTile(RoadMarginLeft(), CoordinateImpl(x,26))
        addTile(RoadMarginBottomLeft(),CoordinateImpl(x,27))
      case 21 =>
        addTile(Road(), CoordinateImpl(x,24))
        addTile(Road(), CoordinateImpl(x,25))
        addTile(Road(), CoordinateImpl(x,26))
        addTile(Road(), CoordinateImpl(x,27))
      case _ =>
        addTile(RoadMarginTop(), CoordinateImpl(x,24))
        addTile(Road(), CoordinateImpl(x,25))
        addTile(Road(), CoordinateImpl(x,26))
        addTile(RoadMarginBottom(), CoordinateImpl(x,27))
    }
  }

  private def addRoadFromSquareToLaboratory(): Unit = {
    for (x <- 28 to 47)  x match {
      case 28 =>
        addTile(Road(), CoordinateImpl(x,24))
        addTile(Road(), CoordinateImpl(x,25))
        addTile(Road(), CoordinateImpl(x,26))
        addTile(Road(), CoordinateImpl(x,27))
      case 47 =>
        addTile(RoadMarginTopRight(),CoordinateImpl(x,24))
        addTile(RoadMarginRight(), CoordinateImpl(x,25))
        addTile(RoadMarginRight(), CoordinateImpl(x,26))
        addTile(RoadMarginBottomRight(),CoordinateImpl(x,27))
      case _ =>
        addTile(RoadMarginTop(), CoordinateImpl(x,24))
        addTile(Road(), CoordinateImpl(x,25))
        addTile(Road(), CoordinateImpl(x,26))
        addTile(RoadMarginBottom(), CoordinateImpl(x,27))
    }
  }

  private def addRoadFromSquareToBottomLeftLake(): Unit = {
    for (y <- 28 to 46)  y match {
      case 28 =>
        addTile(Road(), CoordinateImpl(23,y))
        addTile(Road(), CoordinateImpl(24,y))
        addTile(Road(), CoordinateImpl(25,y))
        addTile(Road(), CoordinateImpl(26,y))
      case 46 =>
        addTile(RoadMarginBottomLeft(),CoordinateImpl(23,y))
        addTile(RoadMarginBottom(), CoordinateImpl(24,y))
        addTile(RoadMarginBottom(), CoordinateImpl(25,y))
        addTile(RoadMarginBottomRight(),CoordinateImpl(26,y))
      case _ =>
        addTile(RoadMarginLeft(), CoordinateImpl(23,y))
        addTile(Road(), CoordinateImpl(24,y))
        addTile(Road(), CoordinateImpl(25,y))
        addTile(RoadMarginRight(), CoordinateImpl(26,y))
    }

    for (x <- 26 to 39) x match {
      case 26 =>
        addTile(Road(), CoordinateImpl(x,43))
        addTile(Road(), CoordinateImpl(x,44))
        addTile(Road(), CoordinateImpl(x,45))
        addTile(RoadMarginBottom(), CoordinateImpl(x,46))
      case 39 =>
        addTile(RoadMarginTopRight(),CoordinateImpl(x,43))
        addTile(RoadMarginRight(), CoordinateImpl(x,44))
        addTile(RoadMarginRight(), CoordinateImpl(x,45))
        addTile(RoadMarginBottomRight(),CoordinateImpl(x,46))
      case _ =>
        addTile(RoadMarginTop(), CoordinateImpl(x,43))
        addTile(Road(), CoordinateImpl(x,44))
        addTile(Road(), CoordinateImpl(x,45))
        addTile(RoadMarginBottom(), CoordinateImpl(x,46))
    }
  }

  private def addRoadFromSquareToTopTallGrass(): Unit = {
    for (y <- 5 to 21)  y match {
      case 5 =>
        addTile(RoadMarginTopLeft(),CoordinateImpl(23,y))
        addTile(RoadMarginTop(), CoordinateImpl(24,y))
        addTile(RoadMarginTop(), CoordinateImpl(25,y))
        addTile(RoadMarginTopRight(),CoordinateImpl(26,y))
      case 21 =>
        addTile(Road(), CoordinateImpl(23,y))
        addTile(Road(), CoordinateImpl(24,y))
        addTile(Road(), CoordinateImpl(25,y))
        addTile(Road(), CoordinateImpl(26,y))
      case _ =>
        addTile(RoadMarginLeft(), CoordinateImpl(23,y))
        addTile(Road(), CoordinateImpl(24,y))
        addTile(Road(), CoordinateImpl(25,y))
        addTile(RoadMarginRight(), CoordinateImpl(26,y))
    }
  }

}