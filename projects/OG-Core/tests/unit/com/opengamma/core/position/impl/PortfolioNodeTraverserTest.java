/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.core.position.impl;

import static org.testng.Assert.assertEquals;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

import org.testng.annotations.Test;

import com.opengamma.core.position.PortfolioNode;
import com.opengamma.core.position.Position;
import com.opengamma.id.UniqueId;
import com.opengamma.id.UniqueIdentifiable;

/**
 * Test {@link PortfolioNodeTraverser}.
 */
public class PortfolioNodeTraverserTest {

  /* Test tree =

                                       N0
                N1              N10               P19 P20
         N2    N5  P8 P9    N11     N14   P17 P18
       P3 P4 P6 P7        P12 P13 P15 P16
   
   */

  private Position createTestPosition(final AtomicInteger nextId) {
    final SimplePosition position = new SimplePosition();
    position.setUniqueId(UniqueId.of("Test", Integer.toString(nextId.getAndIncrement())));
    return position;
  }

  private PortfolioNode createTestPortfolioNode(final AtomicInteger nextId, final int depth) {
    final SimplePortfolioNode node = new SimplePortfolioNode();
    node.setUniqueId(UniqueId.of("Test", Integer.toString(nextId.getAndIncrement())));
    if (depth > 0) {
      node.addChildNode(createTestPortfolioNode(nextId, depth - 1));
      node.addChildNode(createTestPortfolioNode(nextId, depth - 1));
    }
    node.addPosition(createTestPosition(nextId));
    node.addPosition(createTestPosition(nextId));
    return node;
  }

  private static final int NODE_PRE = 0;
  private static final int NODE_POST = 1;
  private static final int POSITION_PRE = 2;
  private static final int POSITION_POST = 3;

  private static class Callback implements PortfolioNodeTraversalCallback {

    private final Queue<Integer> _visited = new LinkedList<Integer>();

    private void visit(final int type, final UniqueIdentifiable uniqueId) {
      _visited.add(type);
      _visited.add(Integer.parseInt(uniqueId.getUniqueId().getValue()));
    }

    @Override
    public void postOrderOperation(final PortfolioNode portfolioNode) {
      visit(NODE_POST, portfolioNode);
    }

    @Override
    public void postOrderOperation(final Position position) {
      visit(POSITION_POST, position);
    }

    @Override
    public void preOrderOperation(final PortfolioNode portfolioNode) {
      visit(NODE_PRE, portfolioNode);
    }

    @Override
    public void preOrderOperation(final Position position) {
      visit(POSITION_PRE, position);
    }

    private void assertVisit(final int type, final int... identifiers) {
      for (int identifier : identifiers) {
        assertEquals(_visited.remove().intValue(), type);
        assertEquals(_visited.remove().intValue(), identifier);
      }
    }

    private void assertVisitCount() {
      // four values for each of the 21 node/position entries in the graph
      assertEquals(_visited.size(), 84);
    }

  }

  @Test
  public void testDepthFirst() {
    final Callback cb = new Callback();
    PortfolioNodeTraverser.depthFirst(cb).traverse(createTestPortfolioNode(new AtomicInteger(), 2));
    cb.assertVisitCount();
    cb.assertVisit(NODE_PRE, 0);
    cb.assertVisit(POSITION_PRE, 19, 20);
    cb.assertVisit(NODE_PRE, 1);
    cb.assertVisit(POSITION_PRE, 8, 9);
    cb.assertVisit(NODE_PRE, 2);
    cb.assertVisit(POSITION_PRE, 3, 4);
    cb.assertVisit(POSITION_POST, 3, 4);
    cb.assertVisit(NODE_POST, 2);
    cb.assertVisit(NODE_PRE, 5);
    cb.assertVisit(POSITION_PRE, 6, 7);
    cb.assertVisit(POSITION_POST, 6, 7);
    cb.assertVisit(NODE_POST, 5);
    cb.assertVisit(POSITION_POST, 8, 9);
    cb.assertVisit(NODE_POST, 1);
    cb.assertVisit(NODE_PRE, 10);
    cb.assertVisit(POSITION_PRE, 17, 18);
    cb.assertVisit(NODE_PRE, 11);
    cb.assertVisit(POSITION_PRE, 12, 13);
    cb.assertVisit(POSITION_POST, 12, 13);
    cb.assertVisit(NODE_POST, 11);
    cb.assertVisit(NODE_PRE, 14);
    cb.assertVisit(POSITION_PRE, 15, 16);
    cb.assertVisit(POSITION_POST, 15, 16);
    cb.assertVisit(NODE_POST, 14);
    cb.assertVisit(POSITION_POST, 17, 18);
    cb.assertVisit(NODE_POST, 10);
    cb.assertVisit(POSITION_POST, 19, 20);
    cb.assertVisit(NODE_POST, 0);
  }

  @Test(enabled = false)
  public void testBreadthFirst() {
    final Callback cb = new Callback();
    PortfolioNodeTraverser.breadthFirst(cb).traverse(createTestPortfolioNode(new AtomicInteger(), 2));
    cb.assertVisitCount();
    // Is this the desired order for a "breadth-first" search??
    cb.assertVisit(NODE_PRE, 0);
    cb.assertVisit(POSITION_PRE, 19, 20);
    cb.assertVisit(NODE_PRE, 1, 10);
    cb.assertVisit(POSITION_PRE, 8, 9, 17, 18);
    cb.assertVisit(NODE_PRE, 2, 5, 11, 14);
    cb.assertVisit(POSITION_PRE, 3, 4, 6, 7, 12, 13, 15, 16);
    cb.assertVisit(POSITION_POST, 3, 4, 6, 7, 12, 13, 15, 16);
    cb.assertVisit(NODE_POST, 2, 5, 11, 14);
    cb.assertVisit(POSITION_POST, 8, 9, 17, 18);
    cb.assertVisit(NODE_POST, 1, 10);
    cb.assertVisit(POSITION_POST, 19, 20);
    cb.assertVisit(NODE_POST, 0);
  }

  @Test(expectedExceptions = UnsupportedOperationException.class)
  public void testBreadthFirstBroken() {
    final Callback cb = new Callback();
    PortfolioNodeTraverser.breadthFirst(cb).traverse(createTestPortfolioNode(new AtomicInteger(), 2));
  }

}
