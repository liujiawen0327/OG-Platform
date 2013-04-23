/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.fudgemsg;

import static org.testng.AssertJUnit.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.testng.annotations.Test;

import com.opengamma.core.id.ExternalSchemes;
import com.opengamma.financial.analytics.curve.CurveNodeIdMapper;
import com.opengamma.financial.analytics.fudgemsg.AnalyticsTestBase;
import com.opengamma.financial.analytics.ircurve.CurveInstrumentProvider;
import com.opengamma.financial.analytics.ircurve.StaticCurveInstrumentProvider;
import com.opengamma.util.test.TestGroup;
import com.opengamma.util.time.Tenor;

/**
 * Test.
 */
@Test(groups = TestGroup.UNIT)
public class CurveNodeIdMapperBuilderTest extends AnalyticsTestBase {

  @Test
  public void test() {
    final Map<Tenor, CurveInstrumentProvider> cashIds = new HashMap<>();
    cashIds.put(Tenor.ONE_DAY, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("123")));
    cashIds.put(Tenor.ONE_WEEK, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("1234")));
    cashIds.put(Tenor.ONE_MONTH, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("12345")));
    cashIds.put(Tenor.TWO_MONTHS, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("123456")));
    cashIds.put(Tenor.THREE_MONTHS, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("1234567")));
    final Map<Tenor, CurveInstrumentProvider> creditSpreadIds = new HashMap<>();
    creditSpreadIds.put(Tenor.ONE_MONTH, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("ABC")));
    creditSpreadIds.put(Tenor.TWO_MONTHS, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("DEF")));
    creditSpreadIds.put(Tenor.THREE_MONTHS, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("GHI")));
    creditSpreadIds.put(Tenor.FOUR_MONTHS, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("JKL")));
    creditSpreadIds.put(Tenor.FIVE_MONTHS, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("MNO")));
    creditSpreadIds.put(Tenor.SIX_MONTHS, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("PQR")));
    creditSpreadIds.put(Tenor.SEVEN_MONTHS, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("STU")));
    creditSpreadIds.put(Tenor.EIGHT_MONTHS, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("VWX")));
    final Map<Tenor, CurveInstrumentProvider> swapIds = new HashMap<>();
    swapIds.put(Tenor.ONE_YEAR, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("q")));
    swapIds.put(Tenor.TWO_YEARS, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("w")));
    swapIds.put(Tenor.THREE_YEARS, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("e")));
    swapIds.put(Tenor.FOUR_YEARS, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("r")));
    swapIds.put(Tenor.FIVE_YEARS, new StaticCurveInstrumentProvider(ExternalSchemes.bloombergTickerSecurityId("t")));
    final CurveNodeIdMapper mapper = new CurveNodeIdMapper(cashIds, creditSpreadIds, swapIds);
    assertEquals(mapper, cycleObject(CurveNodeIdMapper.class, mapper));
  }
}
