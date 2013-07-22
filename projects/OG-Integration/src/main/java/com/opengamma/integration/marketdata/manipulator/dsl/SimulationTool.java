/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.marketdata.manipulator.dsl;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.component.tool.AbstractTool;
import com.opengamma.core.config.ConfigSource;
import com.opengamma.core.config.impl.ConfigItem;
import com.opengamma.engine.marketdata.spec.MarketDataSpecification;
import com.opengamma.engine.view.ViewDefinition;
import com.opengamma.engine.view.ViewProcessor;
import com.opengamma.engine.view.listener.ViewResultListener;
import com.opengamma.financial.tool.ToolContext;
import com.opengamma.id.UniqueId;
import com.opengamma.id.VersionCorrection;
import com.opengamma.scripts.Scriptable;

/**
 * Tool for running simulations defined in Groovy DSL scripts.
 */
@Scriptable
public class SimulationTool extends AbstractTool<ToolContext> {

  /** Command line option for view definition name. */
  private static final String VIEW_DEF_NAME_OPTION = "v";
  /** Command line option for whether to execute in batch mode. */
  private static final String BATCH_MODE_OPTION = "b";
  /** Command line option for the class name of an implementation of ViewResultListener. */
  private static final String RESULT_LISTENER_CLASS_OPTION = "r";
  /** Command line option for the location of the Groovy script that defines the simulation. */
  private static final String SIMULATION_SCRIPT_OPTION = "s";
  /** Command line option for the location of the Groovy script that defines the simulation parameters. */
  private static final String PARAMETER_SCRIPT_OPTION = "p";
  /** Command line option for the names of the market data sources used for running the view. */
  private static final String MARKET_DATA_OPTION = "m";

  public static void main(final String[] args) {
    new SimulationTool().initAndRun(args, ToolContext.class);
    System.exit(0);
  }

  @Override
  protected void doRun() throws Exception {
    ViewProcessor viewProcessor = getToolContext().getViewProcessor();
    ConfigSource configSource = getToolContext().getConfigSource();
    String viewDefName = getCommandLine().getOptionValue('v');
    boolean batchMode = getCommandLine().hasOption('b');
    ViewResultListener listener;
    if (getCommandLine().hasOption('v')) {
      String listenerClass = getCommandLine().getOptionValue('v');
      listener = instantiate(listenerClass, ViewResultListener.class);
    } else {
      listener = null;
    }
    String[] marketDataSpecNames = getCommandLine().getOptionValues('m');
    List<MarketDataSpecification> marketDataSpecs = Lists.newArrayListWithCapacity(marketDataSpecNames.length);
    for (String marketDataSpecName : marketDataSpecNames) {
      // TODO need some format like
      //   live:name
      //   fixedhistorical:date, time series rating
      //   latesthistorical:time series rating
      //   snapshot:name
      //marketDataSpecs.add();
    }
    /*List<MarketDataSpecification> marketDataSpecs =
        ImmutableList.<MarketDataSpecification>of(new LiveMarketDataSpecification("Simulated live market data"));*/
    Map<String, Object> paramValues;
    if (getCommandLine().hasOption('p')) {
      String paramScript = getCommandLine().getOptionValue('p');
      ScenarioDslParameters params = new ScenarioDslParameters(FileUtils.readFileToString(new File(paramScript)));
      paramValues = params.getParameters();
    } else {
      paramValues = null;
    }
    String simulationScript = getCommandLine().getOptionValue('s');
    Simulation simulation = SimulationUtils.createSimulationFromDsl(simulationScript, paramValues);
    VersionCorrection viewDefVersionCorrection = VersionCorrection.LATEST;
    Collection<ConfigItem<ViewDefinition>> viewDefs =
        configSource.get(ViewDefinition.class, viewDefName, viewDefVersionCorrection);
    if (viewDefs.isEmpty()) {
      throw new IllegalStateException("View definition " + viewDefName + " not found");
    }
    ConfigItem<ViewDefinition> viewDef = viewDefs.iterator().next();
    UniqueId viewDefId = viewDef.getUniqueId();
    simulation.run(viewDefId, marketDataSpecs, batchMode, listener, viewProcessor);
  }

  @SuppressWarnings("unchecked")
  private <T> T instantiate(String className, Class<T> expectedType) {
    Class<?> supplierClass;
    try {
      supplierClass = Class.forName(className);
    } catch (ClassNotFoundException e) {
      throw new OpenGammaRuntimeException("Failed to create instance of class " + className, e);
    }
    if (!expectedType.isAssignableFrom(supplierClass)) {
      throw new IllegalArgumentException("Class " + className + " doesn't implement " + expectedType.getName());
    }
    try {
      return (T) supplierClass.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new OpenGammaRuntimeException("Failed to instantiate " + supplierClass.getName(), e);
    }
  }

  @Override
  protected Options createOptions(boolean mandatoryConfigResource) {
    Options options = super.createOptions(mandatoryConfigResource);

    Option viewDefNameOption = new Option(VIEW_DEF_NAME_OPTION, true, "View definition name");
    viewDefNameOption.setRequired(true);
    viewDefNameOption.setArgName("viewdefname");
    options.addOption(viewDefNameOption);

    Option marketDataOption = new Option(MARKET_DATA_OPTION, true, "Market data source names");
    marketDataOption.setRequired(true);
    marketDataOption.setArgName("marketdata");
    options.addOption(marketDataOption);

    Option simulationScriptOption = new Option(SIMULATION_SCRIPT_OPTION, true, "Simulation script location");
    simulationScriptOption.setRequired(true);
    simulationScriptOption.setArgName("simulationscript");
    options.addOption(simulationScriptOption);

    Option paramScriptOption = new Option(PARAMETER_SCRIPT_OPTION, true, "Simulation parameters script location");
    paramScriptOption.setArgName("simulationparameters");
    options.addOption(paramScriptOption);

    Option batchModeOption = new Option(BATCH_MODE_OPTION, false, "Run in batch mode");
    batchModeOption.setArgName("batchmode");
    options.addOption(batchModeOption);

    Option resultListenerClassOption = new Option(RESULT_LISTENER_CLASS_OPTION, true, "Result listener class " +
        "implementing ViewResultListener");
    resultListenerClassOption.setArgName("resultlistenerclass");
    options.addOption(resultListenerClassOption);

    return options;
  }
}

