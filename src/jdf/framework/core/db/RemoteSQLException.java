package jdf.framework.core.db;

/**
 * @(#) RemoteSQLException.java
 * Copyright 1999-2000 by  LG-EDS Systems, Inc.,
 * Information Technology Group, Application Architecture Team, 
 * Application Infrastructure Part.
 * 236-1, Hyosung-2dong, Kyeyang-gu, Inchun, 407-042, KOREA.
 * All rights reserved.
 * 
 * NOTICE !      You can copy or redistribute this code freely, 
 * but you should not remove the information about the copyright notice 
 * and the author.
 * 
 * @author  Soo-Kyung Lim, sukyunglim@lgeds.lg.co.kr.
 */

public class RemoteSQLException extends java.rmi.RemoteException{
    /**
     * @serial
     */
    private String SQLState;

    /**
     * @serial
     */
    private int vendorCode;

    /**
     * @serial
     */
    private RemoteSQLException next;

    /**
     * Constructs a fully-specified <code>RemoteSQLException</code> object.  
     *
     * @param reason a description of the exception 
     * @param SQLState an XOPEN code identifying the exception
     * @param vendorCode a database vendor-specific exception code
     */
    public RemoteSQLException(String reason, String SQLState, int vendorCode) {
        super(reason);
        this.SQLState = SQLState;
        this.vendorCode = vendorCode;
    }


    /**
     * Constructs an <code>RemoteSQLException</code> object with a reason and SQLState;
     * vendorCode defaults to 0.
     *
     * @param reason a description of the exception 
     * @param SQLState an XOPEN code identifying the exception 
     */
    public RemoteSQLException(String reason, String SQLState) {
        super(reason);
        this.SQLState = SQLState;
        this.vendorCode = 0;
    }

    /**
     * Constructs an <code>RemoteSQLException</code> object with a reason;
     * SQLState defaults to null, and vendorCode defaults to 0.
     *
     * @param reason a description of the exception 
     */
    public RemoteSQLException(String reason) {
        super(reason);
        this.SQLState = null;
        this.vendorCode = 0;
    }

    /**
     * Constructs an <code>RemoteSQLException</code> object;
     * reason defaults to null, SQLState
     * defaults to null, and vendorCode defaults to 0.
     * */
    public RemoteSQLException() {
        super();
        this.SQLState = null;
        this.vendorCode = 0;
    }

    /**
     * Retrieves the SQLState for this <code>RemoteSQLException</code> object.
     *
     * @return the SQLState value
     */
    public String getSQLState() {
        return (SQLState);
    }   

    /**
     * Retrieves the vendor-specific exception code
     * for this <code>RemoteSQLException</code> object.
     *
     * @return the vendor's error code
     */
    public int getErrorCode() {
        return (vendorCode);
    }

    /**
     * Retrieves the exception chained to this 
     * <code>RemoteSQLException</code> object.
     *
     * @return the next RemoteSQLException in the chain; null if none
     */
    public RemoteSQLException getNextException() {
        return (next);
    }

    /**
     * Adds an <code>RemoteSQLException</code> object to the end of the chain.
     *
     * @param ex the new exception that will be added to the end of
     *            the RemoteSQLException chain
     */
    public synchronized void setNextException(RemoteSQLException ex) {
        RemoteSQLException theEnd = this;
        while (theEnd.next != null) {
            theEnd = theEnd.next;
        }
        theEnd.next = ex;
    }

}