// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

import java.io.IOException;
import java.util.Random;

/**
 * A DNS message header
 *
 * @author Brian Wellington
 * @see Message
 */

public class Header implements Cloneable{

    /** The length of a DNS Header in wire format. */
    public static final int LENGTH = 12;
    private static Random random = new Random();
    private int id;
    private int flags;
    private int[] counts;

    /**
     * Create a new empty header with a random message id
     */
    public Header(){
        init();
    }

    private void
    init(){
        counts = new int[4];
        flags = 0;
        id = -1;
    }

    /**
     * Creates a new Header from its DNS wire format representation
     *
     * @param b A byte array containing the DNS Header.
     */
    public Header(byte[] b) throws IOException{
        this(new DNSInput(b));
    }

    /**
     * Parses a Header from a stream containing DNS wire format.
     */
    Header(DNSInput in) throws IOException{
        this(in.readU16());
        flags = in.readU16();
        for(int i = 0; i < counts.length; i++){
            counts[i] = in.readU16();
        }
    }

    /**
     * Create a new empty header.
     *
     * @param id The message id
     */
    public Header(int id){
        init();
        setID(id);
    }

    public byte[]
    toWire(){
        DNSOutput out = new DNSOutput();
        toWire(out);
        return out.toByteArray();
    }

    void
    toWire(DNSOutput out){
        out.writeU16(getID());
        out.writeU16(flags);
        for(int i = 0; i < counts.length; i++){
            out.writeU16(counts[i]);
        }
    }

    /**
     * Retrieves the message ID
     */
    public int
    getID(){
        if(id >= 0){
            return id;
        }
        synchronized(this){
            if(id < 0){
                id = random.nextInt(0xffff);
            }
            return id;
        }
    }

    /**
     * Sets the message ID
     */
    public void
    setID(int id){
        if(id < 0 || id > 0xffff){
            throw new IllegalArgumentException("DNS message ID " + id +
                                               " is out of range");
        }
        this.id = id;
    }

    /**
     * Sets a flag to the supplied value
     *
     * @see Flags
     */
    public void
    setFlag(int bit){
        checkFlag(bit);
        // bits are indexed from left to right
        flags |= (1 << (15 - bit));
    }

    static private void
    checkFlag(int bit){
        if(!validFlag(bit)){
            throw new IllegalArgumentException("invalid flag bit " + bit);
        }
    }

    static private boolean
    validFlag(int bit){
        return (bit >= 0 && bit <= 0xF && Flags.isFlag(bit));
    }

    /**
     * Sets a flag to the supplied value
     *
     * @see Flags
     */
    public void
    unsetFlag(int bit){
        checkFlag(bit);
        // bits are indexed from left to right
        flags &= ~(1 << (15 - bit));
    }

    boolean[]
    getFlags(){
        boolean[] array = new boolean[16];
        for(int i = 0; i < array.length; i++){
            if(validFlag(i)){
                array[i] = getFlag(i);
            }
        }
        return array;
    }

    /**
     * Retrieves a flag
     *
     * @see Flags
     */
    public boolean
    getFlag(int bit){
        checkFlag(bit);
        // bits are indexed from left to right
        return (flags & (1 << (15 - bit))) != 0;
    }

    void
    setCount(int field, int value){
        if(value < 0 || value > 0xFFFF){
            throw new IllegalArgumentException("DNS section count " +
                                               value + " is out of range");
        }
        counts[field] = value;
    }

    void
    incCount(int field){
        if(counts[field] == 0xFFFF){
            throw new IllegalStateException("DNS section count cannot " +
                                            "be incremented");
        }
        counts[field]++;
    }

    void
    decCount(int field){
        if(counts[field] == 0){
            throw new IllegalStateException("DNS section count cannot " +
                                            "be decremented");
        }
        counts[field]--;
    }

    /* Creates a new Header identical to the current one */
    public Object
    clone(){
        Header h = new Header();
        h.id = id;
        h.flags = flags;
        System.arraycopy(counts, 0, h.counts, 0, counts.length);
        return h;
    }

    /** Converts the header into a String */
    public String
    toString(){
        return toStringWithRcode(getRcode());
    }

    String
    toStringWithRcode(int newrcode){
        StringBuffer sb = new StringBuffer();

        sb.append(";; ->>HEADER<<- ");
        sb.append("opcode: " + Opcode.string(getOpcode()));
        sb.append(", status: " + Rcode.string(newrcode));
        sb.append(", id: " + getID());
        sb.append("\n");

        sb.append(";; flags: " + printFlags());
        sb.append("; ");
        for(int i = 0; i < 4; i++){
            sb.append(Section.string(i) + ": " + getCount(i) + " ");
        }
        return sb.toString();
    }

    /**
     * Retrieves the mesasge's rcode
     *
     * @see Rcode
     */
    public int
    getRcode(){
        return flags & 0xF;
    }

    /**
     * Sets the message's rcode
     *
     * @see Rcode
     */
    public void
    setRcode(int value){
        if(value < 0 || value > 0xF){
            throw new IllegalArgumentException("DNS Rcode " + value +
                                               " is out of range");
        }
        flags &= ~0xF;
        flags |= value;
    }

    /**
     * Retrieves the mesasge's opcode
     *
     * @see Opcode
     */
    public int
    getOpcode(){
        return (flags >> 11) & 0xF;
    }

    /**
     * Sets the message's opcode
     *
     * @see Opcode
     */
    public void
    setOpcode(int value){
        if(value < 0 || value > 0xF){
            throw new IllegalArgumentException("DNS Opcode " + value +
                                               "is out of range");
        }
        flags &= 0x87FF;
        flags |= (value << 11);
    }

    /** Converts the header's flags into a String */
    public String
    printFlags(){
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < 16; i++){
            if(validFlag(i) && getFlag(i)){
                sb.append(Flags.string(i));
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * Retrieves the record count for the given section
     *
     * @see Section
     */
    public int
    getCount(int field){
        return counts[field];
    }

}
