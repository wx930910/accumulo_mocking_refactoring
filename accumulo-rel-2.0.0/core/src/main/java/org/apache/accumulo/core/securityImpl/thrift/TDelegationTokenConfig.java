/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
/**
 * Autogenerated by Thrift Compiler (0.12.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package org.apache.accumulo.core.securityImpl.thrift;

@SuppressWarnings({"cast", "rawtypes", "serial", "unchecked", "unused"})
public class TDelegationTokenConfig implements org.apache.thrift.TBase<TDelegationTokenConfig, TDelegationTokenConfig._Fields>, java.io.Serializable, Cloneable, Comparable<TDelegationTokenConfig> {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("TDelegationTokenConfig");

  private static final org.apache.thrift.protocol.TField LIFETIME_FIELD_DESC = new org.apache.thrift.protocol.TField("lifetime", org.apache.thrift.protocol.TType.I64, (short)1);

  private static final org.apache.thrift.scheme.SchemeFactory STANDARD_SCHEME_FACTORY = new TDelegationTokenConfigStandardSchemeFactory();
  private static final org.apache.thrift.scheme.SchemeFactory TUPLE_SCHEME_FACTORY = new TDelegationTokenConfigTupleSchemeFactory();

  public long lifetime; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    LIFETIME((short)1, "lifetime");

    private static final java.util.Map<java.lang.String, _Fields> byName = new java.util.HashMap<java.lang.String, _Fields>();

    static {
      for (_Fields field : java.util.EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // LIFETIME
          return LIFETIME;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new java.lang.IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    @org.apache.thrift.annotation.Nullable
    public static _Fields findByName(java.lang.String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final java.lang.String _fieldName;

    _Fields(short thriftId, java.lang.String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public java.lang.String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __LIFETIME_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private static final _Fields optionals[] = {_Fields.LIFETIME};
  public static final java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    java.util.Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new java.util.EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.LIFETIME, new org.apache.thrift.meta_data.FieldMetaData("lifetime", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    metaDataMap = java.util.Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(TDelegationTokenConfig.class, metaDataMap);
  }

  public TDelegationTokenConfig() {
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public TDelegationTokenConfig(TDelegationTokenConfig other) {
    __isset_bitfield = other.__isset_bitfield;
    this.lifetime = other.lifetime;
  }

  public TDelegationTokenConfig deepCopy() {
    return new TDelegationTokenConfig(this);
  }

  @Override
  public void clear() {
    setLifetimeIsSet(false);
    this.lifetime = 0;
  }

  public long getLifetime() {
    return this.lifetime;
  }

  public TDelegationTokenConfig setLifetime(long lifetime) {
    this.lifetime = lifetime;
    setLifetimeIsSet(true);
    return this;
  }

  public void unsetLifetime() {
    __isset_bitfield = org.apache.thrift.EncodingUtils.clearBit(__isset_bitfield, __LIFETIME_ISSET_ID);
  }

  /** Returns true if field lifetime is set (has been assigned a value) and false otherwise */
  public boolean isSetLifetime() {
    return org.apache.thrift.EncodingUtils.testBit(__isset_bitfield, __LIFETIME_ISSET_ID);
  }

  public void setLifetimeIsSet(boolean value) {
    __isset_bitfield = org.apache.thrift.EncodingUtils.setBit(__isset_bitfield, __LIFETIME_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, @org.apache.thrift.annotation.Nullable java.lang.Object value) {
    switch (field) {
    case LIFETIME:
      if (value == null) {
        unsetLifetime();
      } else {
        setLifetime((java.lang.Long)value);
      }
      break;

    }
  }

  @org.apache.thrift.annotation.Nullable
  public java.lang.Object getFieldValue(_Fields field) {
    switch (field) {
    case LIFETIME:
      return getLifetime();

    }
    throw new java.lang.IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new java.lang.IllegalArgumentException();
    }

    switch (field) {
    case LIFETIME:
      return isSetLifetime();
    }
    throw new java.lang.IllegalStateException();
  }

  @Override
  public boolean equals(java.lang.Object that) {
    if (that == null)
      return false;
    if (that instanceof TDelegationTokenConfig)
      return this.equals((TDelegationTokenConfig)that);
    return false;
  }

  public boolean equals(TDelegationTokenConfig that) {
    if (that == null)
      return false;
    if (this == that)
      return true;

    boolean this_present_lifetime = true && this.isSetLifetime();
    boolean that_present_lifetime = true && that.isSetLifetime();
    if (this_present_lifetime || that_present_lifetime) {
      if (!(this_present_lifetime && that_present_lifetime))
        return false;
      if (this.lifetime != that.lifetime)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    int hashCode = 1;

    hashCode = hashCode * 8191 + ((isSetLifetime()) ? 131071 : 524287);
    if (isSetLifetime())
      hashCode = hashCode * 8191 + org.apache.thrift.TBaseHelper.hashCode(lifetime);

    return hashCode;
  }

  @Override
  public int compareTo(TDelegationTokenConfig other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;

    lastComparison = java.lang.Boolean.valueOf(isSetLifetime()).compareTo(other.isSetLifetime());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetLifetime()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.lifetime, other.lifetime);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  @org.apache.thrift.annotation.Nullable
  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    scheme(iprot).read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    scheme(oprot).write(oprot, this);
  }

  @Override
  public java.lang.String toString() {
    java.lang.StringBuilder sb = new java.lang.StringBuilder("TDelegationTokenConfig(");
    boolean first = true;

    if (isSetLifetime()) {
      sb.append("lifetime:");
      sb.append(this.lifetime);
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, java.lang.ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class TDelegationTokenConfigStandardSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TDelegationTokenConfigStandardScheme getScheme() {
      return new TDelegationTokenConfigStandardScheme();
    }
  }

  private static class TDelegationTokenConfigStandardScheme extends org.apache.thrift.scheme.StandardScheme<TDelegationTokenConfig> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, TDelegationTokenConfig struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // LIFETIME
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.lifetime = iprot.readI64();
              struct.setLifetimeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, TDelegationTokenConfig struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.isSetLifetime()) {
        oprot.writeFieldBegin(LIFETIME_FIELD_DESC);
        oprot.writeI64(struct.lifetime);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class TDelegationTokenConfigTupleSchemeFactory implements org.apache.thrift.scheme.SchemeFactory {
    public TDelegationTokenConfigTupleScheme getScheme() {
      return new TDelegationTokenConfigTupleScheme();
    }
  }

  private static class TDelegationTokenConfigTupleScheme extends org.apache.thrift.scheme.TupleScheme<TDelegationTokenConfig> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, TDelegationTokenConfig struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol oprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet optionals = new java.util.BitSet();
      if (struct.isSetLifetime()) {
        optionals.set(0);
      }
      oprot.writeBitSet(optionals, 1);
      if (struct.isSetLifetime()) {
        oprot.writeI64(struct.lifetime);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, TDelegationTokenConfig struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TTupleProtocol iprot = (org.apache.thrift.protocol.TTupleProtocol) prot;
      java.util.BitSet incoming = iprot.readBitSet(1);
      if (incoming.get(0)) {
        struct.lifetime = iprot.readI64();
        struct.setLifetimeIsSet(true);
      }
    }
  }

  private static <S extends org.apache.thrift.scheme.IScheme> S scheme(org.apache.thrift.protocol.TProtocol proto) {
    return (org.apache.thrift.scheme.StandardScheme.class.equals(proto.getScheme()) ? STANDARD_SCHEME_FACTORY : TUPLE_SCHEME_FACTORY).getScheme();
  }
  private static void unusedMethod() {}
}

