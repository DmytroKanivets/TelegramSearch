package org.telegram.api.functions.account;

import org.telegram.tl.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The type TL request account register device.
 */
public class TLRequestAccountRegisterDevice extends TLMethod<TLBool> {
    /**
     * The constant CLASS_ID.
     */
    public static final int CLASS_ID = 0x637ea878;

    private int tokenType;
    private String token;

    /**
     * Instantiates a new TL request account register device.
     */
    public TLRequestAccountRegisterDevice() {
        super();
    }

    public int getClassId() {
        return CLASS_ID;
    }

    public TLBool deserializeResponse(InputStream stream, TLContext context)
            throws IOException {
        TLObject res = StreamingUtils.readTLObject(stream, context);
        if (res == null)
            throw new IOException("Unable to parse response");
        if ((res instanceof TLBool))
            return (TLBool) res;
        throw new IOException("Incorrect response type. Expected org.telegram.tl.TLBool, got: " + res.getClass().getCanonicalName());
    }

    /**
     * Gets token type.
     *
     * @return the token type
     */
    public int getTokenType() {
        return this.tokenType;
    }

    /**
     * Sets token type.
     *
     * @param value the value
     */
    public void setTokenType(int value) {
        this.tokenType = value;
    }

    /**
     * Gets token.
     *
     * @return the token
     */
    public String getToken() {
        return this.token;
    }

    /**
     * Sets token.
     *
     * @param value the value
     */
    public void setToken(String value) {
        this.token = value;
    }

    public void serializeBody(OutputStream stream)
            throws IOException {
        StreamingUtils.writeInt(this.tokenType, stream);
        StreamingUtils.writeTLString(this.token, stream);
    }

    public void deserializeBody(InputStream stream, TLContext context)
            throws IOException {
        this.tokenType = StreamingUtils.readInt(stream);
        this.token = StreamingUtils.readTLString(stream);
    }

    public String toString() {
        return "account.registerDevice#637ea878";
    }
}