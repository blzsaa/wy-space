package hu.blzsaa.wyspace.calculator;

class IndexRangeStrengthPair {
  private final int index;
  private final Long rangeStrength;

  IndexRangeStrengthPair(int index, Long rangeStrength) {
    this.index = index;
    this.rangeStrength = rangeStrength;
  }

  int getIndex() {
    return index;
  }

  Long getRangeStrength() {
    return rangeStrength;
  }
}
