/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.master.marketdatasnapshot;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.joda.beans.BeanDefinition;
import org.joda.beans.MetaProperty;
import org.joda.beans.Property;
import org.joda.beans.PropertyDefinition;
import org.joda.beans.impl.direct.DirectMetaProperty;

import com.opengamma.id.UniqueIdentifier;
import com.opengamma.master.AbstractDocument;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.PublicSPI;

/**
 * A document used to pass into and out of the market data snapshot master.
 * @see MarketDataSnapshotMaster
 */
@PublicSPI
@BeanDefinition
public class MarketDataSnapshotDocument extends AbstractDocument {

  /**
   * The snapshot document unique identifier.
   * This field is managed by the master but must be set for updates.
   */
  @PropertyDefinition
  private UniqueIdentifier _uniqueId;
  /**
   * The snapshot.
   */
  @PropertyDefinition
  private ManageableMarketDataSnapshot _snapshot;

  /**
   * Creates an instance.
   */
  public MarketDataSnapshotDocument() {
  }

  /**
   * Creates an instance from a snapshot and an id.
   * 
   * @param uniqueId  the unique identifier, may be null
   * @param snapshot  the snapshot, not null
   */
  public MarketDataSnapshotDocument(final UniqueIdentifier uniqueId, final ManageableMarketDataSnapshot snapshot) {
    ArgumentChecker.notNull(snapshot, "snapshot");
    setUniqueId(uniqueId);
    setSnapshot(snapshot);
  }

  /**
   * Creates an instance from a snapshot.
   * 
   * @param snapshot  the snapshot, not null
   */
  public MarketDataSnapshotDocument(final ManageableMarketDataSnapshot snapshot) {
    ArgumentChecker.notNull(snapshot, "snapshot");
    setUniqueId(snapshot.getUniqueId());
    setSnapshot(snapshot);
  }

  //-------------------------------------------------------------------------
  /**
   * Gets the name of the snapshot.
   * <p>
   * This is derived from the snapshot itself.
   * 
   * @return the name, null if no name
   */
  public String getName() {
    return (getSnapshot() != null ? getSnapshot().getName() : null);
  }

  //------------------------- AUTOGENERATED START -------------------------
  ///CLOVER:OFF
  /**
   * The meta-bean for {@code MarketDataSnapshotDocument}.
   * @return the meta-bean, not null
   */
  public static MarketDataSnapshotDocument.Meta meta() {
    return MarketDataSnapshotDocument.Meta.INSTANCE;
  }

  @Override
  public MarketDataSnapshotDocument.Meta metaBean() {
    return MarketDataSnapshotDocument.Meta.INSTANCE;
  }

  @Override
  protected Object propertyGet(String propertyName) {
    switch (propertyName.hashCode()) {
      case -294460212:  // uniqueId
        return getUniqueId();
      case 284874180:  // snapshot
        return getSnapshot();
    }
    return super.propertyGet(propertyName);
  }

  @Override
  protected void propertySet(String propertyName, Object newValue) {
    switch (propertyName.hashCode()) {
      case -294460212:  // uniqueId
        setUniqueId((UniqueIdentifier) newValue);
        return;
      case 284874180:  // snapshot
        setSnapshot((ManageableMarketDataSnapshot) newValue);
        return;
    }
    super.propertySet(propertyName, newValue);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the snapshot document unique identifier.
   * This field is managed by the master but must be set for updates.
   * @return the value of the property
   */
  public UniqueIdentifier getUniqueId() {
    return _uniqueId;
  }

  /**
   * Sets the snapshot document unique identifier.
   * This field is managed by the master but must be set for updates.
   * @param uniqueId  the new value of the property
   */
  public void setUniqueId(UniqueIdentifier uniqueId) {
    this._uniqueId = uniqueId;
  }

  /**
   * Gets the the {@code uniqueId} property.
   * This field is managed by the master but must be set for updates.
   * @return the property, not null
   */
  public final Property<UniqueIdentifier> uniqueId() {
    return metaBean().uniqueId().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the snapshot.
   * @return the value of the property
   */
  public ManageableMarketDataSnapshot getSnapshot() {
    return _snapshot;
  }

  /**
   * Sets the snapshot.
   * @param snapshot  the new value of the property
   */
  public void setSnapshot(ManageableMarketDataSnapshot snapshot) {
    this._snapshot = snapshot;
  }

  /**
   * Gets the the {@code snapshot} property.
   * @return the property, not null
   */
  public final Property<ManageableMarketDataSnapshot> snapshot() {
    return metaBean().snapshot().createProperty(this);
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code MarketDataSnapshotDocument}.
   */
  public static class Meta extends AbstractDocument.Meta {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code uniqueId} property.
     */
    private final MetaProperty<UniqueIdentifier> _uniqueId = DirectMetaProperty.ofReadWrite(this, "uniqueId", UniqueIdentifier.class);
    /**
     * The meta-property for the {@code snapshot} property.
     */
    private final MetaProperty<ManageableMarketDataSnapshot> _snapshot = DirectMetaProperty.ofReadWrite(this, "snapshot", ManageableMarketDataSnapshot.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<Object>> _map;

    @SuppressWarnings({"unchecked", "rawtypes" })
    protected Meta() {
      LinkedHashMap temp = new LinkedHashMap(super.metaPropertyMap());
      temp.put("uniqueId", _uniqueId);
      temp.put("snapshot", _snapshot);
      _map = Collections.unmodifiableMap(temp);
    }

    @Override
    public MarketDataSnapshotDocument createBean() {
      return new MarketDataSnapshotDocument();
    }

    @Override
    public Class<? extends MarketDataSnapshotDocument> beanType() {
      return MarketDataSnapshotDocument.class;
    }

    @Override
    public Map<String, MetaProperty<Object>> metaPropertyMap() {
      return _map;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code uniqueId} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<UniqueIdentifier> uniqueId() {
      return _uniqueId;
    }

    /**
     * The meta-property for the {@code snapshot} property.
     * @return the meta-property, not null
     */
    public final MetaProperty<ManageableMarketDataSnapshot> snapshot() {
      return _snapshot;
    }

  }

  ///CLOVER:ON
  //-------------------------- AUTOGENERATED END --------------------------
}
