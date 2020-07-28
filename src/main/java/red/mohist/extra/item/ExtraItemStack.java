package red.mohist.extra.item;

import net.minecraft.item.Item;

public interface ExtraItemStack {

    public void setItem(Item item);

    public void convertStack(int version);
}
