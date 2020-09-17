package com.mohistmc.configuration;

import java.util.ArrayList;
import java.util.HashSet;

public class StringArraySetting extends ArraySetting<String> 
{
    public StringArraySetting(ConfigBase config, String path, String def, String description) 
    {
        super(path, def, description, config);
    }
    
    @Override
    public void initArr(String values)
    {    	
    	String[] vals = values.split(",");
    	
    	this.value_array = new ArrayList<>(vals.length);
    	this.value_set = new HashSet<>(vals.length);
    	
    	for(String val : vals)
    	{
    		if(val.length() == 0) {
				continue;
			}
    		this.value_array.add(val);
    	}
    	this.value_set.addAll(this.value_array);
    }
}
