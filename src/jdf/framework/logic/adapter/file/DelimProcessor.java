package jdf.framework.logic.adapter.file;

import java.io.File;
import java.io.RandomAccessFile;

import jdf.framework.core.data.DataSet;
import jdf.framework.core.data.schema.Block;
import jdf.framework.core.data.schema.Field;
import jdf.framework.core.data.schema.IOSchema;
import jdf.framework.core.data.schema.Processor;


public class DelimProcessor extends Processor implements ProcessProperties
{
    
    private AdapterProperties adapterProperties;
    

    
    
    public void setAdapterProperties(AdapterProperties properties)
    {
        this.adapterProperties=properties;
    }
    


    
    protected void executeAll(DataSet input, DataSet output) throws Exception
    {
        IOSchema bodySchema= input.getIOSchema();
        
        
        
        
        String filename = this.getProperty(FILE_NAME);
        
        File f = new File(this.adapterProperties.getRootDirectory()+filename);
        // raf
        RandomAccessFile raf = new RandomAccessFile(f, "r");
        
        
        Block[] blocks = bodySchema.getOutputBlocks();
        
        
        for(int i=0; i<blocks.length; i++)
        {

            Field[] fields = blocks[i].getFields();
            
            
            for(int j=0;j<fields.length;j++)
            {
                
            }
            
        }
        
        
        
        
    }

}
