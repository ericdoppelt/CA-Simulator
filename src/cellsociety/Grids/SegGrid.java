package cellsociety.Grids;

import java.awt.*;
import java.util.List;
import java.util.Map;

import cellsociety.Cell;
import java.util.ArrayList;
import java.util.Random;

public class SegGrid extends Grid {

  private static final List<String> DATA_FIELDS = List.of(
      "rows",
      "columns",
      "satisfiedThreshold",
      "percentFull"
  );

  private Random r = new Random();
  private double prob;
  private double percentFull;
  private ArrayList<Point> sameCells;
  private final String X = "X";
  private final String O = "O";
  private final String EMPTY = "empty";


  /**
   * Sets rows and columns and instance variables Calls createGrid to initialize a grid of cells
   * based on given rows and columns
   *
   * @param rows    the number of rows to generate in our grid
   * @param columns the number of columns to generate in our grid
   **/
  public SegGrid(int rows, int columns, double satisfiedThreshold, double percentFull) {
    super(rows, columns);
    this.prob = satisfiedThreshold * 8;
    this.percentFull = percentFull;
    this.sameCells = new ArrayList<>();
    setInits();
  }

  /**
   *
   * @return the instance variables in our simulation
   */
  public static List<String> getDataFields() {
    return DATA_FIELDS;
  }

  public SegGrid(Map<String, String> dataValues) {
    this(Integer.parseInt(dataValues.get(DATA_FIELDS.get(0))),
        Integer.parseInt(dataValues.get(DATA_FIELDS.get(1))),
        Double.parseDouble(dataValues.get(DATA_FIELDS.get(2))),
        Double.parseDouble(dataValues.get(DATA_FIELDS.get(3))));
  }

  @Override
  public void updateGrid(){
    int x=0;
    int y=0;
    storeNeighborState(sameCells, getGrid().get(x).get(y).getState());
    super.updateGrid();
  }

  @Override
  protected void updateCells(int x, int y, List<Cell> neighbors){
    int similar_count = 0;
    for (Cell c : neighbors) {
      if (c.getState().equals(current(x, y).getState())) {
        similar_count++;
      }
    }
    if (similar_count >= prob) {
      System.out.println("satisfied: " + (x) + ", " + (y));
    } else {
      System.out.println("unsatisfied: " + (x) + ", " + (y));
      int ran_x = r.nextInt(getColumns());
      int ran_y = r.nextInt(getRows());
      while (current(ran_x, ran_y).getState().equals(EMPTY)) {
        current(ran_x, ran_y).updateState(current(x, y).getState());
        System.out.println("relocated to: " + (ran_x) + ", " + (y));
        ran_x = r.nextInt(getColumns());
        ran_y = r.nextInt(getRows());
      }
      current(x, y).updateState(EMPTY);
    }
  }

  private void setInits() {
    for (int i = 0; i < this.getRows(); i++) {
      for (int j = 0; j < this.getColumns(); j++) {

        if (r.nextFloat() <= percentFull/2) {
          this.current(i, j).updateState(X);
        }
        if (r.nextFloat() <= percentFull/2) {
          this.current(i, j).updateState(O);
        }
      }
    }
  }
}
