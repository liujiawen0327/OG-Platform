/**
 * Copyright (C) 2009 - 2009 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.function;

import java.util.Collection;

import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.util.PublicAPI;

/**
 * All inputs that are being passed into a {@link FunctionInvoker} during invocation.
 * It is the responsibility of the Engine to ensure that the instance of
 * {@code FunctionInputs} provided contains all required values for invocation.
 */
@PublicAPI
public interface FunctionInputs {
  /**
   * Obtain all values required by the function.
   * This form is preferrable to {@link #getValue(String)} and {@link #getValue(ValueRequirement)}
   * where the metadata contained in the {@link ValueSpecification} is required, or where
   * the function is just going to iterate over all values.
   * 
   * @return All values required by the function receiving this instance.
   */
  Collection<ComputedValue> getAllValues();
  
  /**
   * Obtain the actual value computed as part of the specified requirement.
   * This is equivalent to obtaining the embedded {@link ComputedValue} and calling
   * its {@link ComputedValue#getValue()} method.
   * 
   * @param requirement The full requirement desired.
   * @return The ultimate value computed for that requirement.
   */
  Object getValue(ValueRequirement requirement);
  
  /**
   * Obtain an actual value which has the given requirement name.
   * This is equivalent to obtaining the embedded {@link ComputedValue} and calling
   * its {@link ComputedValue#getValue()} method.
   * If the {@link FunctionDefinition} requires multiple requirements with the same
   * name but on different targets, it is undefined which one will be returned by
   * this method, and so this method is only suitable as a convenience for simple
   * functions with a restricted set of inputs.
   * 
   * @param requirementName The name of the requirement desired.
   * @return The ultimate value computed for that requirement.
   */
  Object getValue(String requirementName);

}
