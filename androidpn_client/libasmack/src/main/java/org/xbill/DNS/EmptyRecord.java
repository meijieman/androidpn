// Copyright (c) 1999-2004 Brian Wellington (bwelling@xbill.org)

package org.xbill.DNS;

import java.io.IOException;

/**
 * A class implementing Records with no data; that is, records used in
 * the question section of messages and meta-records in dynamic update.
 *
 * @author Brian Wellington
 */

class EmptyRecord extends Record{

    private static final long serialVersionUID = 3601852050646429582L;

    EmptyRecord(){}

    void
    rrFromWire(DNSInput in) throws IOException{
    }

    Record
    getObject(){
        return new EmptyRecord();
    }

    void
    rrToWire(DNSOutput out, Compression c, boolean canonical){
    }

    String
    rrToString(){
        return "";
    }

    void
    rdataFromString(Tokenizer st, Name origin) throws IOException{
    }

}
