package eyeq.spoon.item;

import eyeq.spoon.Spoon;
import eyeq.util.entity.EntityLivingBaseUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.lang.reflect.InvocationTargetException;

public class ItemSpoon extends ItemSword {
    public ItemSpoon(ToolMaterial material) {
        super(material);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        Block block = world.getBlockState(pos).getBlock();
        if(block != Blocks.MELON_BLOCK && block != Spoon.slabMelon) {
            return EnumActionResult.PASS;
        }
        ItemStack itemStack = player.getHeldItem(hand);
        if(!player.canPlayerEdit(pos, facing, itemStack)) {
            return EnumActionResult.FAIL;
        }
        if(player.canEat(false)) {
            IBlockState state;
            if(block == Spoon.slabMelon) {
                state = Blocks.AIR.getDefaultState();
            } else {
                state = Spoon.slabMelon.getDefaultState();
                if(facing == EnumFacing.DOWN) {
                    state = state.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP);
                }
            }
            world.setBlockState(pos, state, 3);
            Items.MELON.onItemUseFinish(new ItemStack(Items.MELON), world, player);
        }
        return EnumActionResult.SUCCESS;
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase target, EntityLivingBase attacker) {
        World world = target.world;
        try {
            if(EntityLivingBaseUtils.canDropLoot(target) && world.getGameRules().getBoolean("doMobLoot")) {
                target.captureDrops = true;
                target.capturedDrops.clear();
                EntityLivingBaseUtils.dropLoot(target, true, EnchantmentHelper.getLootingModifier(attacker), DamageSource.causeMobDamage(attacker));
            }
        } catch(InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
        if(target.captureDrops) {
            target.captureDrops = false;
            for(EntityItem item : target.capturedDrops) {
                String name = item.getEntityItem().getUnlocalizedName().toLowerCase();
                if(name.contains("eye") || name.contains("pearl")) {
                    world.spawnEntity(item);
                }
            }
        }
        return super.hitEntity(itemStack, target, attacker);
    }
}
