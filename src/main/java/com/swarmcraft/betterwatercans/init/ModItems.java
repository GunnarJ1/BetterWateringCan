package com.swarmcraft.betterwatercans.init;

import com.swarmcraft.betterwatercans.items.ItemWateringCan;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems {
	
	public static Item wateringCan;
	public static Item wateringCanEmpty;
	
	public static void init() {
		wateringCan = new ItemWateringCan(100);
		wateringCanEmpty = new ItemWateringCan(1);
	}
	
	public static void register() {
		GameRegistry.register(wateringCan);
		
	}
	
	public static void registerRecipes() {
		GameRegistry.addRecipe(new ItemStack(wateringCan), "#PS", "IBI", "PIP", '#', Blocks.IRON_BARS, 'S', Blocks.STONE_SLAB, 'I', Items.IRON_INGOT, 'B', Items.WATER_BUCKET);
		GameRegistry.addRecipe(new ItemStack(wateringCanEmpty), "#PS", "IBI", "PIP", '#', Blocks.IRON_BARS, 'S', Blocks.STONE_SLAB, 'I', Items.IRON_INGOT, 'B', Items.BUCKET);
	}
	
	public static void registerRenders() {
		registerRender(wateringCan);
		registerRender(wateringCanEmpty);
	}
	
	private static void registerRender(Item item) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
	}
	
}
