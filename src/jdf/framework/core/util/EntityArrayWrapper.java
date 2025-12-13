// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   EntityArrayWrapper.java

package jdf.framework.core.util;

import jdf.framework.core.io.FormatedEntity;

public class EntityArrayWrapper extends FormatedEntity
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -4470937536803730776L;

	public EntityArrayWrapper()
    {
        //size = 0;
        atom = new EntityArrayWrapper[0];
    }

    public int[] getFieldsLength()
    {
        return (new int[] {
            
        });
    }

	/**
	 * 
	 * @uml.property name="atom"
	 * @uml.associationEnd multiplicity="(0 -1)"
	 */
	//public int size;
	public FormatedEntity atom[];

}
