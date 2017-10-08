// Copyright (c) 2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

import org.xbill.DNS.utils.base16;

import java.io.IOException;

/**
 * SSH Fingerprint - stores the fingerprint of an SSH host key.
 *
 * @author Brian Wellington
 */

public class SSHFPRecord extends Record{

    private static final long serialVersionUID = -8104701402654687025L;
    private int alg;
    private int digestType;
    private byte[] fingerprint;

    SSHFPRecord(){}

    /**
     * Creates an SSHFP Record from the given data.
     *
     * @param alg         The public key's algorithm.
     * @param digestType  The public key's digest type.
     * @param fingerprint The public key's fingerprint.
     */
    public SSHFPRecord(Name name, int dclass, long ttl, int alg, int digestType,
            byte[] fingerprint){
        super(name, Type.SSHFP, dclass, ttl);
        this.alg = checkU8("alg", alg);
        this.digestType = checkU8("digestType", digestType);
        this.fingerprint = fingerprint;
    }

    void
    rrFromWire(DNSInput in) throws IOException{
        alg = in.readU8();
        digestType = in.readU8();
        fingerprint = in.readByteArray();
    }

    Record
    getObject(){
        return new SSHFPRecord();
    }

    void
    rrToWire(DNSOutput out, Compression c, boolean canonical){
        out.writeU8(alg);
        out.writeU8(digestType);
        out.writeByteArray(fingerprint);
    }

    String
    rrToString(){
        StringBuffer sb = new StringBuffer();
        sb.append(alg);
        sb.append(" ");
        sb.append(digestType);
        sb.append(" ");
        sb.append(base16.toString(fingerprint));
        return sb.toString();
    }

    void
    rdataFromString(Tokenizer st, Name origin) throws IOException{
        alg = st.getUInt8();
        digestType = st.getUInt8();
        fingerprint = st.getHex(true);
    }

    /** Returns the public key's algorithm. */
    public int
    getAlgorithm(){
        return alg;
    }

    /** Returns the public key's digest type. */
    public int
    getDigestType(){
        return digestType;
    }

    /** Returns the fingerprint */
    public byte[]
    getFingerPrint(){
        return fingerprint;
    }

    public static class Algorithm{

        public static final int RSA = 1;
        public static final int DSS = 2;

        private Algorithm(){}
    }

    public static class Digest{

        public static final int SHA1 = 1;

        private Digest(){}
    }

}
