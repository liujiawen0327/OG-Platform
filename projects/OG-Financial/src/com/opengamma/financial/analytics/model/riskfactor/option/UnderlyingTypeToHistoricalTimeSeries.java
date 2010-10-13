/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.financial.analytics.model.riskfactor.option;

import org.apache.commons.lang.NotImplementedException;

import com.opengamma.engine.historicaldata.HistoricalDataSource;
import com.opengamma.engine.security.Security;
import com.opengamma.engine.security.SecuritySource;
import com.opengamma.financial.pnl.UnderlyingType;
import com.opengamma.financial.security.option.OptionSecurity;
import com.opengamma.id.IdentifierBundle;
import com.opengamma.id.UniqueIdentifier;
import com.opengamma.util.timeseries.localdate.LocalDateDoubleTimeSeries;
import com.opengamma.util.tuple.Pair;

/**
 * 
 */
public class UnderlyingTypeToHistoricalTimeSeries {
  private static final String LAST_PRICE = "PX_LAST";
  //private static final String IMPLIED_VOLATILITY = "OPT_IMPLIED_VOLATILITY_BST";
  //private static final String VOLUME = "VOLUME";

  public static LocalDateDoubleTimeSeries getSeries(final HistoricalDataSource source, String dataSourceName, String dataProviderName, final SecuritySource secMaster, 
      final UnderlyingType underlying, final Security security) {
    if (security instanceof OptionSecurity) {
      final OptionSecurity option = (OptionSecurity) security;
      switch (underlying) {
        case SPOT_PRICE:
          final Security underlyingSecurity = secMaster.getSecurity(new IdentifierBundle(option.getUnderlyingIdentifier()));
          final Pair<UniqueIdentifier, LocalDateDoubleTimeSeries> tsPair = source.getHistoricalData(underlyingSecurity.getIdentifiers(), dataSourceName, dataProviderName, LAST_PRICE);
          return tsPair.getSecond();
        default:
          throw new NotImplementedException("Don't know how to time series for " + underlying);
      }
    }
    throw new NotImplementedException("Can only get time series for options");
  }
}
