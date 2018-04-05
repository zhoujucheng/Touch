// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: chatmsg.proto

package com.dnnt.touch.protobuf;

public final class Chatmsg {
  private Chatmsg() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistryLite registry) {
  }
  public interface ChatMsgOrBuilder extends
      // @@protoc_insertion_point(interface_extends:com.dnnt.touch.protobuf.ChatMsg)
      com.google.protobuf.MessageLiteOrBuilder {

    /**
     * <code>optional int32 from = 1;</code>
     */
    int getFrom();

    /**
     * <code>optional int32 to = 2;</code>
     */
    int getTo();

    /**
     * <code>optional int64 time = 3;</code>
     */
    long getTime();

    /**
     * <code>optional string msg = 4;</code>
     */
    java.lang.String getMsg();
    /**
     * <code>optional string msg = 4;</code>
     */
    com.google.protobuf.ByteString
        getMsgBytes();
  }
  /**
   * Protobuf type {@code com.dnnt.touch.protobuf.ChatMsg}
   */
  public  static final class ChatMsg extends
      com.google.protobuf.GeneratedMessageLite<
          ChatMsg, ChatMsg.Builder> implements
      // @@protoc_insertion_point(message_implements:com.dnnt.touch.protobuf.ChatMsg)
      ChatMsgOrBuilder {
    private ChatMsg() {
      msg_ = "";
    }
    public static final int FROM_FIELD_NUMBER = 1;
    private int from_;
    /**
     * <code>optional int32 from = 1;</code>
     */
    public int getFrom() {
      return from_;
    }
    /**
     * <code>optional int32 from = 1;</code>
     */
    private void setFrom(int value) {
      
      from_ = value;
    }
    /**
     * <code>optional int32 from = 1;</code>
     */
    private void clearFrom() {
      
      from_ = 0;
    }

    public static final int TO_FIELD_NUMBER = 2;
    private int to_;
    /**
     * <code>optional int32 to = 2;</code>
     */
    public int getTo() {
      return to_;
    }
    /**
     * <code>optional int32 to = 2;</code>
     */
    private void setTo(int value) {
      
      to_ = value;
    }
    /**
     * <code>optional int32 to = 2;</code>
     */
    private void clearTo() {
      
      to_ = 0;
    }

    public static final int TIME_FIELD_NUMBER = 3;
    private long time_;
    /**
     * <code>optional int64 time = 3;</code>
     */
    public long getTime() {
      return time_;
    }
    /**
     * <code>optional int64 time = 3;</code>
     */
    private void setTime(long value) {
      
      time_ = value;
    }
    /**
     * <code>optional int64 time = 3;</code>
     */
    private void clearTime() {
      
      time_ = 0L;
    }

    public static final int MSG_FIELD_NUMBER = 4;
    private java.lang.String msg_;
    /**
     * <code>optional string msg = 4;</code>
     */
    public java.lang.String getMsg() {
      return msg_;
    }
    /**
     * <code>optional string msg = 4;</code>
     */
    public com.google.protobuf.ByteString
        getMsgBytes() {
      return com.google.protobuf.ByteString.copyFromUtf8(msg_);
    }
    /**
     * <code>optional string msg = 4;</code>
     */
    private void setMsg(
        java.lang.String value) {
      if (value == null) {
    throw new NullPointerException();
  }
  
      msg_ = value;
    }
    /**
     * <code>optional string msg = 4;</code>
     */
    private void clearMsg() {
      
      msg_ = getDefaultInstance().getMsg();
    }
    /**
     * <code>optional string msg = 4;</code>
     */
    private void setMsgBytes(
        com.google.protobuf.ByteString value) {
      if (value == null) {
    throw new NullPointerException();
  }
  checkByteStringIsUtf8(value);
      
      msg_ = value.toStringUtf8();
    }

    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      if (from_ != 0) {
        output.writeInt32(1, from_);
      }
      if (to_ != 0) {
        output.writeInt32(2, to_);
      }
      if (time_ != 0L) {
        output.writeInt64(3, time_);
      }
      if (!msg_.isEmpty()) {
        output.writeString(4, getMsg());
      }
    }

    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;

      size = 0;
      if (from_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, from_);
      }
      if (to_ != 0) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(2, to_);
      }
      if (time_ != 0L) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt64Size(3, time_);
      }
      if (!msg_.isEmpty()) {
        size += com.google.protobuf.CodedOutputStream
          .computeStringSize(4, getMsg());
      }
      memoizedSerializedSize = size;
      return size;
    }

    public static com.dnnt.touch.protobuf.Chatmsg.ChatMsg parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.dnnt.touch.protobuf.Chatmsg.ChatMsg parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.dnnt.touch.protobuf.Chatmsg.ChatMsg parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data);
    }
    public static com.dnnt.touch.protobuf.Chatmsg.ChatMsg parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, data, extensionRegistry);
    }
    public static com.dnnt.touch.protobuf.Chatmsg.ChatMsg parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static com.dnnt.touch.protobuf.Chatmsg.ChatMsg parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }
    public static com.dnnt.touch.protobuf.Chatmsg.ChatMsg parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input);
    }
    public static com.dnnt.touch.protobuf.Chatmsg.ChatMsg parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return parseDelimitedFrom(DEFAULT_INSTANCE, input, extensionRegistry);
    }
    public static com.dnnt.touch.protobuf.Chatmsg.ChatMsg parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input);
    }
    public static com.dnnt.touch.protobuf.Chatmsg.ChatMsg parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return com.google.protobuf.GeneratedMessageLite.parseFrom(
          DEFAULT_INSTANCE, input, extensionRegistry);
    }

    public static Builder newBuilder() {
      return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(com.dnnt.touch.protobuf.Chatmsg.ChatMsg prototype) {
      return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    /**
     * Protobuf type {@code com.dnnt.touch.protobuf.ChatMsg}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageLite.Builder<
          com.dnnt.touch.protobuf.Chatmsg.ChatMsg, Builder> implements
        // @@protoc_insertion_point(builder_implements:com.dnnt.touch.protobuf.ChatMsg)
        com.dnnt.touch.protobuf.Chatmsg.ChatMsgOrBuilder {
      // Construct using com.dnnt.touch.protobuf.Chatmsg.ChatMsg.newBuilder()
      private Builder() {
        super(DEFAULT_INSTANCE);
      }


      /**
       * <code>optional int32 from = 1;</code>
       */
      public int getFrom() {
        return instance.getFrom();
      }
      /**
       * <code>optional int32 from = 1;</code>
       */
      public Builder setFrom(int value) {
        copyOnWrite();
        instance.setFrom(value);
        return this;
      }
      /**
       * <code>optional int32 from = 1;</code>
       */
      public Builder clearFrom() {
        copyOnWrite();
        instance.clearFrom();
        return this;
      }

      /**
       * <code>optional int32 to = 2;</code>
       */
      public int getTo() {
        return instance.getTo();
      }
      /**
       * <code>optional int32 to = 2;</code>
       */
      public Builder setTo(int value) {
        copyOnWrite();
        instance.setTo(value);
        return this;
      }
      /**
       * <code>optional int32 to = 2;</code>
       */
      public Builder clearTo() {
        copyOnWrite();
        instance.clearTo();
        return this;
      }

      /**
       * <code>optional int64 time = 3;</code>
       */
      public long getTime() {
        return instance.getTime();
      }
      /**
       * <code>optional int64 time = 3;</code>
       */
      public Builder setTime(long value) {
        copyOnWrite();
        instance.setTime(value);
        return this;
      }
      /**
       * <code>optional int64 time = 3;</code>
       */
      public Builder clearTime() {
        copyOnWrite();
        instance.clearTime();
        return this;
      }

      /**
       * <code>optional string msg = 4;</code>
       */
      public java.lang.String getMsg() {
        return instance.getMsg();
      }
      /**
       * <code>optional string msg = 4;</code>
       */
      public com.google.protobuf.ByteString
          getMsgBytes() {
        return instance.getMsgBytes();
      }
      /**
       * <code>optional string msg = 4;</code>
       */
      public Builder setMsg(
          java.lang.String value) {
        copyOnWrite();
        instance.setMsg(value);
        return this;
      }
      /**
       * <code>optional string msg = 4;</code>
       */
      public Builder clearMsg() {
        copyOnWrite();
        instance.clearMsg();
        return this;
      }
      /**
       * <code>optional string msg = 4;</code>
       */
      public Builder setMsgBytes(
          com.google.protobuf.ByteString value) {
        copyOnWrite();
        instance.setMsgBytes(value);
        return this;
      }

      // @@protoc_insertion_point(builder_scope:com.dnnt.touch.protobuf.ChatMsg)
    }
    protected final Object dynamicMethod(
        com.google.protobuf.GeneratedMessageLite.MethodToInvoke method,
        Object arg0, Object arg1) {
      switch (method) {
        case NEW_MUTABLE_INSTANCE: {
          return new com.dnnt.touch.protobuf.Chatmsg.ChatMsg();
        }
        case IS_INITIALIZED: {
          return DEFAULT_INSTANCE;
        }
        case MAKE_IMMUTABLE: {
          return null;
        }
        case NEW_BUILDER: {
          return new Builder();
        }
        case VISIT: {
          Visitor visitor = (Visitor) arg0;
          com.dnnt.touch.protobuf.Chatmsg.ChatMsg other = (com.dnnt.touch.protobuf.Chatmsg.ChatMsg) arg1;
          from_ = visitor.visitInt(from_ != 0, from_,
              other.from_ != 0, other.from_);
          to_ = visitor.visitInt(to_ != 0, to_,
              other.to_ != 0, other.to_);
          time_ = visitor.visitLong(time_ != 0L, time_,
              other.time_ != 0L, other.time_);
          msg_ = visitor.visitString(!msg_.isEmpty(), msg_,
              !other.msg_.isEmpty(), other.msg_);
          if (visitor == com.google.protobuf.GeneratedMessageLite.MergeFromVisitor
              .INSTANCE) {
          }
          return this;
        }
        case MERGE_FROM_STREAM: {
          com.google.protobuf.CodedInputStream input =
              (com.google.protobuf.CodedInputStream) arg0;
          com.google.protobuf.ExtensionRegistryLite extensionRegistry =
              (com.google.protobuf.ExtensionRegistryLite) arg1;
          try {
            boolean done = false;
            while (!done) {
              int tag = input.readTag();
              switch (tag) {
                case 0:
                  done = true;
                  break;
                default: {
                  if (!input.skipField(tag)) {
                    done = true;
                  }
                  break;
                }
                case 8: {

                  from_ = input.readInt32();
                  break;
                }
                case 16: {

                  to_ = input.readInt32();
                  break;
                }
                case 24: {

                  time_ = input.readInt64();
                  break;
                }
                case 34: {
                  String s = input.readStringRequireUtf8();

                  msg_ = s;
                  break;
                }
              }
            }
          } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw new RuntimeException(e.setUnfinishedMessage(this));
          } catch (java.io.IOException e) {
            throw new RuntimeException(
                new com.google.protobuf.InvalidProtocolBufferException(
                    e.getMessage()).setUnfinishedMessage(this));
          } finally {
          }
        }
        case GET_DEFAULT_INSTANCE: {
          return DEFAULT_INSTANCE;
        }
        case GET_PARSER: {
          if (PARSER == null) {    synchronized (com.dnnt.touch.protobuf.Chatmsg.ChatMsg.class) {
              if (PARSER == null) {
                PARSER = new DefaultInstanceBasedParser(DEFAULT_INSTANCE);
              }
            }
          }
          return PARSER;
        }
      }
      throw new UnsupportedOperationException();
    }


    // @@protoc_insertion_point(class_scope:com.dnnt.touch.protobuf.ChatMsg)
    private static final com.dnnt.touch.protobuf.Chatmsg.ChatMsg DEFAULT_INSTANCE;
    static {
      DEFAULT_INSTANCE = new ChatMsg();
      DEFAULT_INSTANCE.makeImmutable();
    }

    public static com.dnnt.touch.protobuf.Chatmsg.ChatMsg getDefaultInstance() {
      return DEFAULT_INSTANCE;
    }

    private static volatile com.google.protobuf.Parser<ChatMsg> PARSER;

    public static com.google.protobuf.Parser<ChatMsg> parser() {
      return DEFAULT_INSTANCE.getParserForType();
    }
  }


  static {
  }

  // @@protoc_insertion_point(outer_class_scope)
}
