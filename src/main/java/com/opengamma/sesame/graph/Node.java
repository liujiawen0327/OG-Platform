/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.sesame.graph;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;

/**
 *
 */
public abstract class Node {

  private final ImmutableList<Node> _dependencies;

  /* package */ Node(List<Node> dependencies) {
    _dependencies = ImmutableList.copyOf(dependencies);
  }

  /* package */ Node() {
    this(Collections.<Node>emptyList());
  }

  /* package */ abstract Object create(Map<Class<?>, Object> singletons);

  public ImmutableList<Node> getDependencies() {
    return _dependencies;
  }
}
