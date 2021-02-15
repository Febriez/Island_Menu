package com.splendor.menu.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemBuilder extends ItemStack {

    private final Material material;
    private final ItemMeta itemmeta;
    private final List<String> lore;

    public ItemBuilder(Material material) {
        this.material = material;
        setType(material);
        this.itemmeta = getItemMeta();
        setAmount(1);
        lore = new ArrayList<>();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.material = itemStack.getType();
        setType(material);
        this.itemmeta = itemStack.getItemMeta();
        setDurability(itemStack.getDurability());
        setItemMeta(itemmeta);
        setAmount(itemStack.getAmount());
        lore = itemStack.getItemMeta().hasLore() ? itemStack.getItemMeta().getLore() : new ArrayList<>();
    }

    public ItemBuilder setDatas(short data) {
        setDurability(data);
        return this;
    }

    public ItemBuilder setAmounts(int amount) {
        setAmount(amount);
        return this;
    }

    public ItemBuilder setDisplayName(String name) {
        itemmeta.setDisplayName(name);
        return this;
    }

    public ItemBuilder HideECHANTMENT(boolean b) {
        if (b)
            itemmeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        else
            itemmeta.removeItemFlags(ItemFlag.HIDE_ENCHANTS);
        return this;
    }

    public ItemBuilder addEnchants(Enchantment enchant, int i) {
        itemmeta.addEnchant(enchant, i, false);
        return this;
    }

    public ItemBuilder addLore(String s) {
        lore.add(s);
        return this;
    }

    public ItemStack build() {
        itemmeta.setLore(lore);
        setItemMeta(itemmeta);
        return this;
    }

}
