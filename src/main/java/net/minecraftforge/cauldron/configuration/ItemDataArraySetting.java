package net.minecraftforge.cauldron.configuration;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.World;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemDataArraySetting extends ArraySetting<Long> 
{
    public ItemDataArraySetting(ConfigBase config, String path, String def, String description)
    {
        super(path, def, description, config);
    }

    public Long getValue(int id, int data)
    {
    	return (((long)id) << 32L) ^ data;
    }
    
    public boolean contains(ItemStack stack)
    {
    	return super.contains(getValue(Item.getIdFromItem(stack.getItem()), stack.getItemDamage()));
    }
    
    public boolean contains(Item item)
    {
    	return super.contains(getValue(Item.getIdFromItem(item), 0));
    }
    
    public boolean contains(Block block)
    {
    	return super.contains(getValue(Block.getIdFromBlock(block), 0));
    }
    
    public boolean contains(World world, int x, int y, int z)
    {
    	return super.contains(getValue(world.getBlockTypeIdAt(x, y, z), world.getBlockAt(x, y, z).getData()));
    }    
    
	@Override
	public void initArr(String array)
	{
		String[] potential_values = array.split(",");
		
		this.value_array = new ArrayList<Long>(potential_values.length);
		this.value_set = new HashSet<Long>(potential_values.length);
		
		for(String potval : potential_values)
		{
			try 
			{
				if(potval.length() == 0)
					continue;
				String[] val_colon = potval.split(":");
				if(val_colon.length != 2)
					continue;
				Long val_id = getValue(Integer.parseInt(val_colon[0]), Integer.parseInt(val_colon[1]));
				this.value_array.add(val_id);
			} 
			catch ( Throwable t) 
			{
				System.out.println("[Thermos] Failed to add an option from config file");
				t.printStackTrace();
			}
		}
		this.value_set.addAll(this.value_array);
	}
}
