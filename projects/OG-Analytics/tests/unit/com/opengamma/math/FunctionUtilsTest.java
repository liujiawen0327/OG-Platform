/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.math;

import static com.opengamma.math.FunctionUtils.fromTensorIndex;
import static com.opengamma.math.FunctionUtils.toTensorIndex;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 */
public class FunctionUtilsTest {
  private static final double EPS = 1e-15;

  @Test
  public void testSquare() {
    for (int i = 0; i < 100; i++) {
      final double x = Math.random();
      assertEquals(FunctionUtils.square(x), x * x, EPS);
    }
  }

  @Test
  public void testCube() {
    for (int i = 0; i < 100; i++) {
      final double x = Math.random();
      assertEquals(FunctionUtils.cube(x), x * x * x, EPS);
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullIndices() {
    FunctionUtils.toTensorIndex(null, new int[] {1, 2, 3});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullDimensions1() {
    FunctionUtils.toTensorIndex(new int[] {1, 2, 3}, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWrongLength() {
    FunctionUtils.toTensorIndex(new int[] {1, 2}, new int[] {1, 2, 3});
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullDimensions2() {
    FunctionUtils.fromTensorIndex(2, null);
  }

  @Test
  public void testTensorIndexTest1() {

    final int[] indices = new int[] {2};
    final int[] dimensions = new int[] {5};
    final int index = toTensorIndex(indices, dimensions);
    assertEquals(indices[0], index, 0);

    final int[] res = fromTensorIndex(index, dimensions);
    assertEquals(indices[0], res[0], 0);

  }

  @Test
  public void testTensorIndexTest2() {

    final int[] indices = new int[] {2, 3};
    final int[] dimensions = new int[] {5, 7};
    final int index = toTensorIndex(indices, dimensions);
    final int[] res = fromTensorIndex(index, dimensions);
    assertEquals(indices[0], res[0], 0);
    assertEquals(indices[1], res[1], 0);
  }

  @Test
  public void testTensorIndexTest3() {

    final int[] indices = new int[] {2, 3, 1};
    final int[] dimensions = new int[] {5, 7, 3};
    final int index = toTensorIndex(indices, dimensions);
    final int[] res = fromTensorIndex(index, dimensions);
    assertEquals(indices[0], res[0], 0);
    assertEquals(indices[1], res[1], 0);
    assertEquals(indices[2], res[2], 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOutOfBounds() {
    final int[] indices = new int[] {2, 7, 1};
    final int[] dimensions = new int[] {5, 7, 3};
    toTensorIndex(indices, dimensions);
  }
}
