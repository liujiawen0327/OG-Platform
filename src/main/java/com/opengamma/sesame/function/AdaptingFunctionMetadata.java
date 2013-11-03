/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.sesame.function;

import com.opengamma.core.position.PositionOrTrade;
import com.opengamma.core.security.Security;
import com.opengamma.sesame.config.FunctionArguments;

/**
 * Wraps an {@link InvokableFunction} that expects a {@link Security} input in one that expects a {@link PositionOrTrade}
 * input. When the invoker is called it gets the security from the {@link PositionOrTrade} and uses it when
 * invoking the wrapped invoker.
 */
public class AdaptingFunctionMetadata extends FunctionMetadata {

  public AdaptingFunctionMetadata(FunctionMetadata function) {
    super(function);
  }

  @Override
  public InvokableFunction getInvokableFunction(Object receiver) {
    return new AdaptingInvokableFunction(super.getInvokableFunction(receiver));
  }

  private static class AdaptingInvokableFunction implements InvokableFunction {

    private final InvokableFunction _delegate;

    private AdaptingInvokableFunction(InvokableFunction delegate) {
      _delegate = delegate;
    }

    @Override
    public Object invoke(Object input, FunctionArguments args) {
      Security security = ((PositionOrTrade) input).getSecurity();
      return _delegate.invoke(security, args);
    }

    @Override
    public String getOutputName() {
      return _delegate.getOutputName();
    }

    @Override
    public Object getReceiver() {
      return _delegate.getReceiver();
    }
  }
}
