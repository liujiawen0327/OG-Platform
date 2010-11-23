/**
 * Copyright (C) 2009 - 2010 by OpenGamma Inc.
 *
 * Please see distribution for license.
 */
package com.opengamma.engine.security.server;

import com.opengamma.util.rest.AbstractResourceService;

/**
 * A class of names used in the RESTful security source.
 */
public final class SecuritySourceServiceNames {

  /*package*/ static final String DEFAULT_SECURITYSOURCE_NAME = AbstractResourceService.DEFAULT_RESOURCE_NAME;

  /**
   * Fudge key for a security.
   */
  public static final String SECURITYSOURCE_SECURITY = "security";

  /**
   * No instances.
   */
  private SecuritySourceServiceNames() {
  }

}
