package model.map

import model.environment.CoordinateImpl
import utilities.Settings

case class InitialTownElements() extends MapElementsImpl {

  addBuildings()
  addLakes()
  addTallGrass()
  addTrees()

  square()
  roadFromSquareToPokemonCenter()
  roadFromSquareToLaboratory()

  private def square(): Unit = {
    for (x <- 21 to 28)
      for (y <- 21 to 28)
        (x,y) match {
          case (21,21) => addTile(RoadMarginTopLeft(), CoordinateImpl(x,y))
          case (22,21) | (23,21) | (24,21) | (25,21) | (26,21) | (27,21) => addTile(RoadMarginTop(), CoordinateImpl(x,y))
          case (28,21) => addTile(RoadMarginTopRight(), CoordinateImpl(x,y))
          case (21,22) | (21,23) | (21,24) | (21,25) | (21,26) | (21,27) => addTile(RoadMarginLeft(), CoordinateImpl(x,y))
          case (21,28) => addTile(RoadMarginBottomLeft(), CoordinateImpl(x,y))
          case (28,22) | (28,23) | (28,24) | (28,25) | (28,26) | (28,27) => addTile(RoadMarginRight(), CoordinateImpl(x,y))
          case (28,28) => addTile(RoadMarginBottomRight(), CoordinateImpl(x,y))
          case (22,28) | (23,28) | (24,28) | (25,28) | (26,28) | (27,28) => addTile(RoadMarginBottom(), CoordinateImpl(x,y))
          case (_,32) => addTile(RoadMarginBottom(), CoordinateImpl(x,y))
          case _ => addTile(Road(), CoordinateImpl(x,y))
        }
  }

  private def roadFromSquareToPokemonCenter(): Unit = {
    for (x <- 9 to 21)  x match {
      case 9 =>
        addTile(RoadMarginTopLeft(),CoordinateImpl(9,24))
        addTile(RoadMarginLeft(), CoordinateImpl(9,25))
        addTile(RoadMarginLeft(), CoordinateImpl(9,26))
        addTile(RoadMarginBottomLeft(),CoordinateImpl(9,27))
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

  private def roadFromSquareToLaboratory(): Unit = {
    for (x <- 28 to 48)  x match {
      case 28 =>
        addTile(Road(), CoordinateImpl(x,24))
        addTile(Road(), CoordinateImpl(x,25))
        addTile(Road(), CoordinateImpl(x,26))
        addTile(Road(), CoordinateImpl(x,27))
      case 47 =>
        addTile(RoadMarginTopRight(),CoordinateImpl(48,24))
        addTile(RoadMarginRight(), CoordinateImpl(48,25))
        addTile(RoadMarginRight(), CoordinateImpl(48,26))
        addTile(RoadMarginBottomRight(),CoordinateImpl(48,27))
      case _ =>
        addTile(RoadMarginTop(), CoordinateImpl(x,24))
        addTile(Road(), CoordinateImpl(x,25))
        addTile(Road(), CoordinateImpl(x,26))
        addTile(RoadMarginBottom(), CoordinateImpl(x,27))
    }
  }

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
    addLake(CoordinateImpl(4,5), CoordinateImpl(20,10))
    addLake(CoordinateImpl(40,40), CoordinateImpl(48,48))
    addLake(CoordinateImpl(10,28), CoordinateImpl(19,30))
    addLake(CoordinateImpl(10,42), CoordinateImpl(11,44))
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

}