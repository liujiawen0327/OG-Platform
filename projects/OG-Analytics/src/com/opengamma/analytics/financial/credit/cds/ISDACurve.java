/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.analytics.financial.credit.cds;

import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.analytics.math.curve.ConstantDoublesCurve;
import com.opengamma.analytics.math.curve.DoublesCurve;
import com.opengamma.analytics.math.curve.InterpolatedDoublesCurve;
import com.opengamma.analytics.math.interpolation.CombinedInterpolatorExtrapolator;
import com.opengamma.analytics.math.interpolation.FlatExtrapolator1D;
import com.opengamma.analytics.math.interpolation.LinearInterpolator1D;

/**
 * A curve that behaves according to the ISDA standard for CDS pricing.
 * 
 * This curve is intended for use with {@link CDSApproxISDAMethod} in order
 * to produce numbers that match the ISDA standard pricing model for CDS. It
 * may be useful in other situations where ISDA standard discount factors
 * are assumed.
 * 
 * @author Martin Traverse, Niels Stchedroff (Riskcare)
 */
public class ISDACurve {

  private final String _name;
  
  private final double _offset;
  
  private final DoublesCurve _curve;
  
  private final double[] _shiftedTimePoints;
  
  private final double _zeroDiscountFactor;
  
  public static ISDACurve fromBoxed(String name, Double[] xData, Double[] yData, double offset) {
    
    double[] rawXData = new double[xData.length];
    double[] rawYData = new double[yData.length];
    
    for (int i = 0; i < xData.length; ++i) {
      rawXData[i] = xData[i].doubleValue();
    }
    
    for (int i = 0; i < yData.length; ++i) {
      rawYData[i] = yData[i].doubleValue();
    }
    
    return new ISDACurve(name, rawXData, rawYData, offset);
  }
  
  public ISDACurve(final String name, final double[] xData, final double[] yData, final double offset) {
    
    _name = name;
    _offset = offset;
    
    // Choose interpolation/extrapolation to match the behaviour of curves in the ISDA CDS reference code
    if (xData.length > 1) {
      _curve = InterpolatedDoublesCurve.fromSorted(xData, yData,
        new CombinedInterpolatorExtrapolator(
          new ISDAInterpolator1D(),    // ISDA style interpolation
          new FlatExtrapolator1D(),    // Flat rate extrapolated to the left
          new ISDAExtrapolator1D()));  // ISDA style extrapolation to the right
    } else if (xData.length == 1) {
      _curve = ConstantDoublesCurve.from(yData[0]);  // Unless the curve is flat, in which case use a constant curve
    } else {
      throw new OpenGammaRuntimeException("Cannot construct a curve with no points");
    }
    
    _shiftedTimePoints = new double[xData.length];
    
    for (int i = 0; i < xData.length; ++i) {
      _shiftedTimePoints[i] = xData[i] + _offset;
    }
    
    _zeroDiscountFactor = Math.exp(_offset * getInterestRate(0.0));
  }
  
  // For fudge builder
  public ISDACurve(final String name, final DoublesCurve curve, final double offset, final double zeroDiscountFactor, final double[] shiftedTimePoints) {
    _name = name;
    _curve = curve;
    _offset = offset;
    _zeroDiscountFactor = zeroDiscountFactor;
    _shiftedTimePoints = shiftedTimePoints;
  }

  public String getName() {
    return _name;
  }
  
  public double getInterestRate(final Double t) {
    return _curve.getYValue(t - _offset);
  }
  
  public double getDiscountFactor(final double t) {
    return Math.exp((_offset - t) * getInterestRate(t)) / _zeroDiscountFactor;
  }
  
  public double[] getTimePoints() {
    return _shiftedTimePoints;
  }
  
  public DoublesCurve getCurve() {
    return _curve;
  }
  
  public double getOffset() {
    return _offset;
  }
  
  public double getZeroDiscountFactor() {
    return _zeroDiscountFactor;
  }
}
