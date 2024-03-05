# OUTDATED
please use this API from the [SpigotApi](https://github.com/Lucaa8/SpigotApi) plugin (1.20.x and later)

![NBTTags](https://i.imgur.com/w4F5Ka3.png)

## About NBTTags API

- This API makes editing Minecraft items NBTTags easy!

The main goal of this API is to be compatible with all Spigot/Paper versions.

- Is the API hard to take hands on ?

No, there is only one class to import and all methods are explicit. Everybody can use it.

## About Minecraft NBTTags

A nbt tag can be applied on minecraft items. It is a way to store some data into an item.

The concept is basic, like a Map entry, the nbt contains a key linked to a value(String, Integer, Float, etc...)

This is a really nice way to identify your items into random inventories or tracking them without ruining your beautiful item meta(name, lore, etc..).

Here is an example of usage for an itemstack's NBTTag : {CraftedBy:"Player0",ItemID:"Player0s_Item",CraftedDate:1634670924333}

There is some minecraft default tags, like; 

- "RepairCost" : stores the experience level cost to repair the item with an anvil
- "Damage" : stores the item's durability

**NBTTags are saved on server restart.**

## Why would I need an API for that

You can't edit nbttags with the Spigot API. You need to use net.minecraft.server(known as NMS) package. Unfortunately this package's name changes for each minecraft server version. So you need to import for example the "1.15.2" NMS version to use nbt on Spigot 1.15.2. But if you decide to switch to 1.16 later, you have to edit your plugin to import now the 1.16 NMS version.

NBTTags API uses java reflection to get your server's version and access to the right NMS package. So you can use your plugin with all versions.

## How do I use the API

Import;
```java
import NBT.NBTTag;
```

Get the NBT editor instance of an Itemstack;

```java
ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD, 1);
NBTTag itemTagEditor = new NBTTag(itemStack);
```

Add or edit an existing tag on the ItemStack;

```java
itemTagEditor.setTag("CraftedBy", "Player0");
```

Add or edit multiple tags on the ItemStack;

```java
itemTagEditor.setTag("CraftedBy", "Player0").setTag("CraftedDate", System.currentTimeMillis());
```

Apply changes to the ItemStack;

```java
itemStack = itemTagEditor.getBukkitItem();
```

Get back any tag on the ItemStack;

```java
String CraftedBy = itemTagEditor.getString("CraftedBy");
long CraftedDate = (long)itemTagEditor.getTag("CraftedDate");
```

Check if an ItemStack contains tags, or a specific key and remove it

```java
if(itemTagEditor.hasTags()){
    if(itemTagEditor.hasTag("CraftedBy")){
        itemTagEditor.removeTag("CraftedBy");
    }
    if(itemTagEditor.hasTag("CraftedDate")){
        itemTagEditor.removeTag("CraftedDate");      
    }
}
```

Print all tags contained on the ItemStack;

```java
System.out.println(itemTagEditor.getTags());
```

(No needs to check with "hasTags". If the itemstack doesn't contains any tags it will print "{}")

Format: {CraftedBy:"Player0",CraftedDate:1634670924333}

## Edit an ItemStack's durability with NBTTags

"Damage" is a default Minecraft nbttag, you can add it on any ItemStack.

So you can add it to a new damageable item or edit an old value with;
```java
ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD, 1);
NBTTag itemTagEditor = new NBTTag(itemStack);
itemStack = itemTagEditor.setTag("Damage",100).getBukkitItem();
```

But now your item's damage is 100. It means that if your item's max durability is 1561, now it has 1461 out of 1561.
![SwordDamage](https://i.imgur.com/hUkv8GD.png)

But you can substract the damage to the item's max durability to actually set the "right" damage.
```java
ItemStack itemStack = new ItemStack(Material.DIAMOND_SWORD, 1);
NBTTag itemTagEditor = new NBTTag(itemStack);
int durability = 100;
int damage = itemStack.getType().getMaxDurability()-durability;
itemStack = itemTagEditor.setTag("Damage",damage).getBukkitItem();
```
![SwordDurability](https://i.imgur.com/q83TOGZ.png)

## Built With

* [IntelliJ IDEA](https://www.jetbrains.com/fr-fr/idea/) - Using Maven

## Author(s)

The entire code was written by me.

This readme.md is inspired from @suroxdesigns's ones

## License
 
This project is licensed under the GPLv3
 
![GNU GPLV3](https://imgur.com/imkUoGR.png)
