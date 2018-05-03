package org.telegram.api.functions.messages;

import org.telegram.api.input.peer.TLAbsInputPeer;
import org.telegram.api.messages.TLMessagesPeerDialogs;
import org.telegram.tl.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The type TL request messages get chats.
 */
public class TLRequestMessagesGetPeerDialogs extends TLMethod<TLMessagesPeerDialogs> {
    /**
     * The constant CLASS_ID.
     */
    public static final int CLASS_ID = 0x2d9776b9;

    private TLVector<TLAbsInputPeer> peers;

    /**
     * Instantiates a new TL request messages get chats.
     */
    public TLRequestMessagesGetPeerDialogs() {
        super();
    }

    public int getClassId() {
        return CLASS_ID;
    }

    public TLMessagesPeerDialogs deserializeResponse(InputStream stream, TLContext context)
            throws IOException {
        final TLObject res = StreamingUtils.readTLObject(stream, context);
        if (res == null) {
            throw new IOException("Unable to parse response");
        }
        if ((res instanceof TLMessagesPeerDialogs)) {
            return (TLMessagesPeerDialogs) res;
        }
        throw new IOException("Incorrect response type. Expected " + TLMessagesPeerDialogs.class.getName() + ", got: " + res.getClass().getCanonicalName());
    }

    public TLVector<TLAbsInputPeer> getPeers() {
        return peers;
    }

    public void setPeers(TLVector<TLAbsInputPeer> peers) {
        this.peers = peers;
    }

    public void serializeBody(OutputStream stream)
            throws IOException {
        StreamingUtils.writeTLVector(peers, stream);
    }

    public void deserializeBody(InputStream stream, TLContext context)
            throws IOException {
        peers = StreamingUtils.readTLVector(stream, context, TLAbsInputPeer.class);
    }

    public String toString() {
        return "messages.getPeerDialogs#2d9776b9";
    }
}