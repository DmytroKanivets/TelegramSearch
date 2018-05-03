package org.telegram.bot.kernel.engine;

/**
 * Created by Hendrik Hofstadt on 08.03.14.
 */

import org.telegram.bot.kernel.engine.storage.*;
import org.telegram.tl.TLContext;
import org.telegram.tl.TLObject;

/**
 * Created with IntelliJ IDEA.
 * User: Ruben Bermudez
 * Date: 09.11.13
 * Time: 0:29
 */
public class TLPersistence<T extends TLObject> extends TLContext {

    private static final String LOGTAG = "KernelPersistence";
    private Class<T> destClass;
    private T obj;

    TLPersistence(String fileName, Class<T> destClass) {
        try {
            obj = destClass.newInstance();
        } catch (Exception e1) {
            throw new RuntimeException("Unable to instantiate default settings");
        }
    }

    public void init() {
        registerClass(TLDcInfo.CLASS_ID, TLDcInfo.class);
        registerClass(TLKey.CLASS_ID, TLKey.class);
        registerClass(TLLastKnownSalt.CLASS_ID, TLLastKnownSalt.class);
        registerClass(TLOldSession.CLASS_ID, TLOldSession.class);
        registerClass(TLStorage.CLASS_ID, TLStorage.class);
    }

    public T getObj() {
        return obj;
    }

}
