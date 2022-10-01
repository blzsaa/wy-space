package hu.blzsaa.wyspace;

public class TotalStrengthAtPeriod {
  private int startingIndexOfPeriod;
  private long strength;

  public int getStartingIndexOfPeriod() {
    return startingIndexOfPeriod;
  }

  public void setStartingIndexOfPeriod(int startingIndexOfPeriod) {
    this.startingIndexOfPeriod = startingIndexOfPeriod;
  }

  public long getStrength() {
    return strength;
  }

  public void setStrength(long strength) {
    this.strength = strength;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TotalStrengthAtPeriod that = (TotalStrengthAtPeriod) o;

    if (startingIndexOfPeriod != that.startingIndexOfPeriod) return false;
    return strength == that.strength;
  }

  @Override
  public int hashCode() {
    int result = startingIndexOfPeriod;
    result = 31 * result + (int) (strength ^ (strength >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "TotalStrengthAtPeriod{"
        + "startingIndexOfPeriod="
        + startingIndexOfPeriod
        + ", strength="
        + strength
        + '}';
  }
}
