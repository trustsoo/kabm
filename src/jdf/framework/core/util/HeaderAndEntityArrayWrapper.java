// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   HeaderAndEntityArrayWrapper.java

package jdf.framework.core.util;

import jdf.framework.core.io.FormatedEntity;

// Referenced classes of package com.kse.util:
//            EntityArrayWrapper

public class HeaderAndEntityArrayWrapper extends FormatedEntity
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -3792601847133070349L;

	public HeaderAndEntityArrayWrapper()
    {
        size = 0;
        atom = new EntityArrayWrapper[0];
    }

    public int[] getFieldsLength()
    {
        return (new int[] {
            4
        });
    }

    public int size;

	/**
	 * 
	 * @uml.property name="header"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	public FormatedEntity header;

	/**
	 * 
	 * @uml.property name="atom"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 */
	public FormatedEntity atom[];

}
