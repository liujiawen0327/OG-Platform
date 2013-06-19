/**
 * Copyright (C) 2013 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.integration.viewer.status;

import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.lang.StringUtils;

import com.google.common.collect.Lists;
import com.opengamma.OpenGammaRuntimeException;
import com.opengamma.livedata.UserPrincipal;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.EnumUtils;

/**
 * View status reporter options
 */
public final class ViewStatusReporterOption {
  
  private static final String DEFAULT_FORMAT = "html";
  
  private static final List<String> SUPPORTED_FORMAT = Lists.newArrayList("html", "csv");
  /** 
   * Portfolio name option flag 
   */
  private static final String PORTFOLIO_NAME_OPT = "n";
  /** 
   * Username option flag
   */
  private static final String USERNAME_OPT = "u";
  /**
   * User ip address flag
   */
  private static final String USER_IP_ADDRESS_OPT = "ip";
  /**
   * Result format type flag
   */
  private static final String FORMAT_TYPE_OPT = "fm";
  /**
   * Aggregation type flag
   */
  private static final String AGGREGATION_TYPE_OPT = "a";
  
  /**
   * Default user
   */
  private static final UserPrincipal DEFAULT_USER = UserPrincipal.getLocalUser();
  
  private final String _portfolioName;
  
  private final ResultFormat _format;
  
  private final UserPrincipal _user;
  
  private final AggregateType _aggregateType;
    
  private ViewStatusReporterOption(final String portfolioName, String formatOption, UserPrincipal user, String aggregateType) {
    ArgumentChecker.notNull(portfolioName, "portfolioName");
    ArgumentChecker.notNull(user, "user");
    ArgumentChecker.notNull(formatOption, "formatOption");
    
    _portfolioName = portfolioName;
    validateFormat(formatOption);
    _format = ResultFormat.of(formatOption);
    _user = user;
    if (aggregateType != null) {
      _aggregateType = AggregateType.of(aggregateType);
    } else {
      _aggregateType = AggregateType.NO_AGGREGATION;
    }
  }

  private void validateFormat(String formatOption) {
    formatOption = formatOption.toLowerCase();
    if (!SUPPORTED_FORMAT.contains(formatOption)) {
      throw new OpenGammaRuntimeException("Unsupported format type: " + formatOption);
    }
  }
  
  /**
   * Creates command line options
   * 
   * @return the command line options, not-null.
   */
  public static Options createOptions() {
    
    Options options = new Options();
    
    Option portfolioNameOption = new Option(PORTFOLIO_NAME_OPT, "name", true, "the name of the source OpenGamma portfolio");
    portfolioNameOption.setArgName("portfolioName");
    portfolioNameOption.setRequired(true);
   
    Option usernameOption = new Option(USERNAME_OPT, "username", true, "the username for computing views");
    usernameOption.setArgName("username");
    
    Option ipaddressOption = new Option(USER_IP_ADDRESS_OPT, "ipaddress", true, "the ip address of user for computing views");
    ipaddressOption.setArgName("ipaddress");
    
    Option formatTypeOption = new Option(FORMAT_TYPE_OPT, "format", true, "the format of status result, default is html");
    formatTypeOption.setArgName("csv, xml, html");
    
    Option aggregationTypeOption = new Option(AGGREGATION_TYPE_OPT, "aggregation", true, "the aggregation type of result, default is no-aggregation");
    aggregationTypeOption.setArgName("TSVC, CSVT");
    
    options.addOption(portfolioNameOption);
    options.addOption(usernameOption);
    options.addOption(formatTypeOption);
    options.addOption(ipaddressOption);
    options.addOption(aggregationTypeOption);
    
    return options;
  }
  
  /**
   * Creates a View status option instance from the options supplied from the command line
   * 
   * @param commandLine the command line, not-null
   * @return the view status option, not-null
   */
  public static ViewStatusReporterOption getViewStatusReporterOption(final CommandLine commandLine) {
    ArgumentChecker.notNull(commandLine, "commandLine");
    
    String portfolioName = StringUtils.trimToNull(commandLine.getOptionValue(PORTFOLIO_NAME_OPT));
    String username = StringUtils.trimToNull(commandLine.getOptionValue(USERNAME_OPT));
    UserPrincipal user = null;
    if (username == null) {
      user = DEFAULT_USER;
    } else {
      String ipaddress = StringUtils.trimToNull(commandLine.getOptionValue(USER_IP_ADDRESS_OPT));
      if (ipaddress == null) {
        user = DEFAULT_USER;
      } else {
        user = new UserPrincipal(username, ipaddress);
      }
    }
    
    String format = StringUtils.trimToNull(commandLine.getOptionValue(FORMAT_TYPE_OPT));
    format = StringUtils.defaultString(format, DEFAULT_FORMAT);
    
    String aggregationOption = StringUtils.trimToNull(commandLine.getOptionValue(AGGREGATION_TYPE_OPT));
    return new ViewStatusReporterOption(portfolioName, format, user, aggregationOption);
  }

  /**
   * Gets the portfolioName.
   * @return the portfolioName
   */
  public String getPortfolioName() {
    return _portfolioName;
  }
  
  /**
   * Gets the user.
   * @return the user
   */
  public UserPrincipal getUser() {
    return _user;
  }
    
  /**
   * Gets the format.
   * @return the format
   */
  public ResultFormat getFormat() {
    return _format;
  }
  
  /**
   * Gets the aggregationOrder.
   * @return the aggregationOrder
   */
  public AggregateType getAggregationOrder() {
    return _aggregateType;
  }

  /**
   * View result format
   */
  public static enum ResultFormat {
    /**
     * CSV
     */
    CSV("csv"),
    /**
     * XML
     */
    XML("xml"),
    /**
     * HTML
     */
    HTML("html");
    
    private String _extension;
    
    /**
     * Creates an instance.
     * 
     * @param extension  the file suffix, not null
     */
    private ResultFormat(String extension) {
      _extension = extension;
    }
    
    /**
     * Gets the file extension for a format
     * 
     * @return the file extension, not null
     */
    public String getExtension() {
      return _extension;
    }
    
    public static ResultFormat of(String resultFormatStr) {
      resultFormatStr = StringUtils.trimToNull(resultFormatStr);
      if (resultFormatStr != null) {
        return EnumUtils.safeValueOf(ResultFormat.class, resultFormatStr.toUpperCase());
      }
      return null;
    }
  }
  
}
