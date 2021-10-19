package NBT;

import org.bukkit.inventory.ItemStack;

public class NBTTag {

    private ItemStack item;

    public NBTTag(ItemStack item) {
        this.item = item;
    }

    public NBTTag setTag(String tag, Object value) {
        item = NMSManager.setTag(item, tag, value);
        return this;
    }

    public NBTTag removeTag(String tag) {
        item = NMSManager.removeTag(item, tag);
        return this;
    }

    public Object getTag(String tag) {
        return NMSManager.getTag(item, tag);
    }

    public String getString(String tag) {
        return NMSManager.getStringTag(item, tag);
    }

    public Object getTags() {
        return NMSManager.getTags(item);
    }

    public boolean hasTag(String tag) {
        return NMSManager.containsTag(item, tag);
    }

    public boolean hasTags() {
        return NMSManager.hasTag(item);
    }

    public Object getNMSItem() {
        return NMSManager.getNMSItem(item);
    }

    public ItemStack getBukkitItem() {
        return item;
    }
}
