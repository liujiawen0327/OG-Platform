/**
 * Copyright (C) 2014 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.sesame.sabr;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.joda.beans.Bean;
import org.joda.beans.BeanDefinition;
import org.joda.beans.ImmutableBean;
import org.joda.beans.ImmutableConstructor;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.opengamma.core.convention.Convention;
import com.opengamma.core.link.ConventionLink;
import com.opengamma.core.link.SnapshotLink;
import com.opengamma.core.marketdatasnapshot.NamedSnapshot;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.result.FailureResult;
import com.opengamma.util.result.FailureStatus;
import com.opengamma.util.result.Result;
import com.opengamma.util.result.SuccessResult;

/**
 * Allows the retrieval of a SABR surface matching a specified name.
 *
 * @param <C> the convention type used to interpret the surface
 * @param <S> the type of snapshot to be returned
 */
@BeanDefinition
public final class SabrSurfaceSelector<C extends Convention, S extends NamedSnapshot> implements ImmutableBean {

  /**
   * The names for SABR surfaces.
   */
  public enum SabrSurfaceName {
    /**
     * The Alpha SABR surface.
     */
    ALPHA,
    /**
     * The Beta SABR surface.
     */
    BETA,
    /**
     * The Rho SABR surface.
     */
    RHO,
    /**
     * The Nu SABR surface.
     */
    NU
  }

  /**
   * Map from SABR surface names to snapshot links. The linked snapshot will
   * contain the data for the named surface.
   */
  @PropertyDefinition(validate = "notNull")
  private final Map<SabrSurfaceName, SnapshotLink<S>> _sabrSurfaceMap;

  /**
   * Link to the convention which is appropriate for use with the
   * SABR surfaces.
   */
  @PropertyDefinition(validate = "notNull")
  private final ConventionLink<C> _convention;

  @ImmutableConstructor
  private SabrSurfaceSelector(Map<SabrSurfaceName, SnapshotLink<S>> sabrSurfaceMap,
                              ConventionLink<C> convention) {
    _sabrSurfaceMap = ImmutableMap.copyOf(ArgumentChecker.notEmpty(sabrSurfaceMap, "sabrSurfaceMap"));
    _convention = ArgumentChecker.notNull(convention, "convention");
  }

  /**
   * Find the SABR surface matching the required name. If the surface
   * exists then it will be returned wrapped in a {@link SuccessResult}. If it
   * does not exist then a {@link FailureResult} will be returned.
   *
   * @param surfaceName the surface name to find
   * @return Result containing the required surface, if it is available
   */
  public Result<S> findSabrSurface(SabrSurfaceName surfaceName) {
    return _sabrSurfaceMap.containsKey(surfaceName) ?
        Result.success(_sabrSurfaceMap.get(surfaceName).resolve()) :
        Result.<S>failure(FailureStatus.MISSING_DATA, "Config contains no {} surface", surfaceName);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code SabrSurfaceSelector}.
   * @return the meta-bean, not null
   */
  @SuppressWarnings("rawtypes")
  public static SabrSurfaceSelector.Meta meta() {
    return SabrSurfaceSelector.Meta.INSTANCE;
  }

  /**
   * The meta-bean for {@code SabrSurfaceSelector}.
   * @param <R>  the first generic type
   * @param <S>  the second generic type
   * @param cls1  the first generic type
   * @param cls2  the second generic type
   * @return the meta-bean, not null
   */
  @SuppressWarnings("unchecked")
  public static <R extends Convention, S extends NamedSnapshot> SabrSurfaceSelector.Meta<R, S> metaSabrSurfaceSelector(Class<R> cls1, Class<S> cls2) {
    return SabrSurfaceSelector.Meta.INSTANCE;
  }

  static {
    JodaBeanUtils.registerMetaBean(SabrSurfaceSelector.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @param <C>  the type
   * @param <S>  the type
   * @return the builder, not null
   */
  public static <C extends Convention, S extends NamedSnapshot> SabrSurfaceSelector.Builder<C, S> builder() {
    return new SabrSurfaceSelector.Builder<C, S>();
  }

  @SuppressWarnings("unchecked")
  @Override
  public SabrSurfaceSelector.Meta<C, S> metaBean() {
    return SabrSurfaceSelector.Meta.INSTANCE;
  }

  @Override
  public <R> Property<R> property(String propertyName) {
    return metaBean().<R>metaProperty(propertyName).createProperty(this);
  }

  @Override
  public Set<String> propertyNames() {
    return metaBean().metaPropertyMap().keySet();
  }

  //-----------------------------------------------------------------------
  /**
   * Gets map from SABR surface names to snapshot links. The linked snapshot will
   * contain the data for the named surface.
   * @return the value of the property, not null
   */
  public Map<SabrSurfaceName, SnapshotLink<S>> getSabrSurfaceMap() {
    return _sabrSurfaceMap;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets link to the convention which is appropriate for use with the
   * SABR surfaces.
   * @return the value of the property, not null
   */
  public ConventionLink<C> getConvention() {
    return _convention;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder<C, S> toBuilder() {
    return new Builder<C, S>(this);
  }

  @Override
  public SabrSurfaceSelector<C, S> clone() {
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      SabrSurfaceSelector<?, ?> other = (SabrSurfaceSelector<?, ?>) obj;
      return JodaBeanUtils.equal(getSabrSurfaceMap(), other.getSabrSurfaceMap()) &&
          JodaBeanUtils.equal(getConvention(), other.getConvention());
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash += hash * 31 + JodaBeanUtils.hashCode(getSabrSurfaceMap());
    hash += hash * 31 + JodaBeanUtils.hashCode(getConvention());
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(96);
    buf.append("SabrSurfaceSelector{");
    buf.append("sabrSurfaceMap").append('=').append(getSabrSurfaceMap()).append(',').append(' ');
    buf.append("convention").append('=').append(JodaBeanUtils.toString(getConvention()));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code SabrSurfaceSelector}.
   */
  public static final class Meta<C extends Convention, S extends NamedSnapshot> extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    @SuppressWarnings("rawtypes")
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code sabrSurfaceMap} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<Map<SabrSurfaceName, SnapshotLink<S>>> _sabrSurfaceMap = DirectMetaProperty.ofImmutable(
        this, "sabrSurfaceMap", SabrSurfaceSelector.class, (Class) Map.class);
    /**
     * The meta-property for the {@code convention} property.
     */
    @SuppressWarnings({"unchecked", "rawtypes" })
    private final MetaProperty<ConventionLink<C>> _convention = DirectMetaProperty.ofImmutable(
        this, "convention", SabrSurfaceSelector.class, (Class) ConventionLink.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> _metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "sabrSurfaceMap",
        "convention");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 241185581:  // sabrSurfaceMap
          return _sabrSurfaceMap;
        case 2039569265:  // convention
          return _convention;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public SabrSurfaceSelector.Builder<C, S> builder() {
      return new SabrSurfaceSelector.Builder<C, S>();
    }

    @SuppressWarnings({"unchecked", "rawtypes" })
    @Override
    public Class<? extends SabrSurfaceSelector<C, S>> beanType() {
      return (Class) SabrSurfaceSelector.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return _metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code sabrSurfaceMap} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Map<SabrSurfaceName, SnapshotLink<S>>> sabrSurfaceMap() {
      return _sabrSurfaceMap;
    }

    /**
     * The meta-property for the {@code convention} property.
     * @return the meta-property, not null
     */
    public MetaProperty<ConventionLink<C>> convention() {
      return _convention;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 241185581:  // sabrSurfaceMap
          return ((SabrSurfaceSelector<?, ?>) bean).getSabrSurfaceMap();
        case 2039569265:  // convention
          return ((SabrSurfaceSelector<?, ?>) bean).getConvention();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code SabrSurfaceSelector}.
   */
  public static final class Builder<C extends Convention, S extends NamedSnapshot> extends DirectFieldsBeanBuilder<SabrSurfaceSelector<C, S>> {

    private Map<SabrSurfaceName, SnapshotLink<S>> _sabrSurfaceMap = new HashMap<SabrSurfaceName, SnapshotLink<S>>();
    private ConventionLink<C> _convention;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(SabrSurfaceSelector<C, S> beanToCopy) {
      this._sabrSurfaceMap = new HashMap<SabrSurfaceName, SnapshotLink<S>>(beanToCopy.getSabrSurfaceMap());
      this._convention = beanToCopy.getConvention();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 241185581:  // sabrSurfaceMap
          return _sabrSurfaceMap;
        case 2039569265:  // convention
          return _convention;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Builder<C, S> set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 241185581:  // sabrSurfaceMap
          this._sabrSurfaceMap = (Map<SabrSurfaceName, SnapshotLink<S>>) newValue;
          break;
        case 2039569265:  // convention
          this._convention = (ConventionLink<C>) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder<C, S> set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder<C, S> setString(String propertyName, String value) {
      setString(meta().metaProperty(propertyName), value);
      return this;
    }

    @Override
    public Builder<C, S> setString(MetaProperty<?> property, String value) {
      super.set(property, value);
      return this;
    }

    @Override
    public Builder<C, S> setAll(Map<String, ? extends Object> propertyValueMap) {
      super.setAll(propertyValueMap);
      return this;
    }

    @Override
    public SabrSurfaceSelector<C, S> build() {
      return new SabrSurfaceSelector<C, S>(
          _sabrSurfaceMap,
          _convention);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the {@code sabrSurfaceMap} property in the builder.
     * @param sabrSurfaceMap  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder<C, S> sabrSurfaceMap(Map<SabrSurfaceName, SnapshotLink<S>> sabrSurfaceMap) {
      JodaBeanUtils.notNull(sabrSurfaceMap, "sabrSurfaceMap");
      this._sabrSurfaceMap = sabrSurfaceMap;
      return this;
    }

    /**
     * Sets the {@code convention} property in the builder.
     * @param convention  the new value, not null
     * @return this, for chaining, not null
     */
    public Builder<C, S> convention(ConventionLink<C> convention) {
      JodaBeanUtils.notNull(convention, "convention");
      this._convention = convention;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(96);
      buf.append("SabrSurfaceSelector.Builder{");
      buf.append("sabrSurfaceMap").append('=').append(JodaBeanUtils.toString(_sabrSurfaceMap)).append(',').append(' ');
      buf.append("convention").append('=').append(JodaBeanUtils.toString(_convention));
      buf.append('}');
      return buf.toString();
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------

}
