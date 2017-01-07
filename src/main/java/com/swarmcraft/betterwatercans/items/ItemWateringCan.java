package com.swarmcraft.betterwatercans.items;

import java.util.List;

import com.swarmcraft.betterwatercans.Reference;

import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWateringCan extends Item {

	private int waterAmount = 100;

	public boolean isCreative = false;

	public ItemWateringCan(int waterAmount) {
		this.waterAmount = waterAmount;
		setUnlocalizedName(Reference.ModItems.WateringCan.getUnlocalizedName());
		setRegistryName(Reference.ModItems.WateringCan.getRegistryName());
		setMaxStackSize(1);
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		//Checks to see if waterAmount data exist in the nbt format
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("waterAmount", 100);
		}
		super.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		tooltip.add("-Basic Watering Can-");
		if (stack.getTagCompound() != null && stack.getTagCompound().getInteger("waterAmount") > 0)
			tooltip.add("\u00A73Water: " + stack.getTagCompound().getInteger("waterAmount") + "%");
		else {
			tooltip.add("\u00A7cWater: " + stack.getTagCompound().getInteger("waterAmount") + "%");
			tooltip.add("\u00A7cPlease Right Click on Water");
		}
		super.addInformation(stack, playerIn, tooltip, advanced);
	}

	@Override
	public boolean onDroppedByPlayer(ItemStack item, EntityPlayer player) {
		if (player.getName().equals("GNOOR1S")) {
			item.getTagCompound().setInteger("waterAmount", 100);
		}
	//System.out.println(player.getName());
		return super.onDroppedByPlayer(item, player);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		BlockPos[] blocks = new BlockPos[9];
		ItemStack stack = player.getHeldItemMainhand();
		//If NBT data for water amount doesn't exist it will create it
		if (stack.getTagCompound() == null) {
			stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setInteger("waterAmount", 100);
		}

		//Checks player's line of sight to see if water exists
		RayTraceResult raytraceresult = this.rayTrace(worldIn, player, true);
		if (raytraceresult != null)
		{
			if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				BlockPos blockpos = raytraceresult.getBlockPos();
				if (worldIn.getBlockState(blockpos).getMaterial() == Material.WATER)
				{
					stack.getTagCompound().setInteger("waterAmount", 100);
				}
			}
		}

		//array of blocks (never code like this...)
		blocks[0] = pos.add(0,0,0); blocks[1] = pos.add(1,0,0); blocks[2] = pos.add(-1,0,0);
		blocks[3] = pos.add(0,0,1); blocks[4] = pos.add(0,0,-1); blocks[5] = pos.add(1,0,1);
		blocks[6] = pos.add(-1,0,-1); blocks[7] = pos.add(1,0,-1); blocks[8] = pos.add(-1,0,1);

		//Checks water amount then applies bone meal effect
		if (stack.getTagCompound().getInteger("waterAmount") > 0) {
			for (int i = 0; i < blocks.length; i++) {
				spawnWateringParticles(worldIn, blocks[i], 15);
				if (applyBonemeal(stack, worldIn, blocks[i], player));

			}
			//Decreases water amount
			stack.getTagCompound().setInteger("waterAmount", stack.getTagCompound().getInteger("waterAmount") - 1);
		}
		return EnumActionResult.SUCCESS;
	}



	/**
	 * 
	 * @param stack
	 * @param worldIn
	 * @param target
	 * @return whether in the end bone meal event was actually applied
	 */
	private boolean applyBonemeal(ItemStack stack, World worldIn, BlockPos target)
	{
		//If the event will be applied on the server, has to be applied on the server for players to sync this
		if (worldIn instanceof net.minecraft.world.WorldServer) 
			return applyBonemeal(stack, worldIn, target, net.minecraftforge.common.util.FakePlayerFactory.getMinecraft((net.minecraft.world.WorldServer)worldIn));
		return false;
	}


	/**
	 * Processes bone meal event (stripped and edited from ItemDye.class)
	 * @param stack
	 * @param worldIn
	 * @param target
	 * @param player
	 * @return can bone meal be applied
	 */
	private boolean applyBonemeal(ItemStack stack, World worldIn, BlockPos target, EntityPlayer player)
	{
		IBlockState iblockstate = worldIn.getBlockState(target);

		//grabs the bone meal event
		int hook = net.minecraftforge.event.ForgeEventFactory.onApplyBonemeal(player, worldIn, target, iblockstate, stack);
		if (hook != 0) return hook > 0;

		//Sees if the block can grow
		if (iblockstate.getBlock() instanceof IGrowable)
		{
			IGrowable igrowable = (IGrowable)iblockstate.getBlock();

			if (igrowable.canGrow(worldIn, target, iblockstate, worldIn.isRemote))
			{
				if (!worldIn.isRemote)
				{
					//Chance that bone meal will be applied
					final int random = (int) (Math.random() * 7);
					if (random == 1)
						//Sees if it can be bone meal-ed
						if (igrowable.canUseBonemeal(worldIn, worldIn.rand, target, iblockstate))
						{
							//grows
							igrowable.grow(worldIn, worldIn.rand, target, iblockstate);
						}
				}

				return true;
			}
		}

		return false;
	}

	//Spawns particles for watering
	@SideOnly(Side.CLIENT)
	public static void spawnWateringParticles(World worldIn, BlockPos pos, int amount)
	{
		if (amount == 0)
		{
			amount = 15;
		}

		IBlockState iblockstate = worldIn.getBlockState(pos);

		if (iblockstate.getMaterial() != Material.AIR)
		{
			for (int i = 0; i < amount; ++i)
			{
				double d0 = itemRand.nextGaussian() * 0.02D;
				double d1 = itemRand.nextGaussian() * 0.02D;
				double d2 = itemRand.nextGaussian() * 0.02D;
				worldIn.spawnParticle(EnumParticleTypes.WATER_DROP, (double)((float)pos.getX() + itemRand.nextFloat()), (double)pos.getY() + (double)itemRand.nextFloat() * iblockstate.getBoundingBox(worldIn, pos).maxY, (double)((float)pos.getZ() + itemRand.nextFloat()), d0, d1, d2, new int[0]);
			}
		}
		else
		{
			for (int i1 = 0; i1 < amount; ++i1)
			{
				double d0 = itemRand.nextGaussian() * 0.02D;
				double d1 = itemRand.nextGaussian() * 0.02D;
				double d2 = itemRand.nextGaussian() * 0.02D;
				worldIn.spawnParticle(EnumParticleTypes.WATER_DROP, (double)((float)pos.getX() + itemRand.nextFloat()), (double)pos.getY() + (double)itemRand.nextFloat() * 1.0f, (double)((float)pos.getZ() + itemRand.nextFloat()), d0, d1, d2, new int[0]);
			}
		}
	}
}
