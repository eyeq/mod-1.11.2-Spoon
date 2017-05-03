package eyeq.spoon.block;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;

import java.util.Random;

public class BlockSlabMelon extends BlockSlab {
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    public BlockSlabMelon() {
        super(Material.GROUND, MapColor.LIME);
        this.setSoundType(SoundType.WOOD);
        this.setDefaultState(blockState.getBaseState().withProperty(VARIANT, Variant.DEFAULT).withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM));
        this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
        this.useNeighborBrightness = true;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Blocks.MELON_BLOCK.getItemDropped(state, rand, fortune);
    }

    @Override
    public int quantityDropped(Random rand) {
        return Blocks.MELON_BLOCK.quantityDropped(rand) / 2;
    }

    @Override
    public int quantityDroppedWithBonus(int fortune, Random rand) {
        return Blocks.MELON_BLOCK.quantityDroppedWithBonus(fortune, rand) / 2;
    }

    @Override
    public String getUnlocalizedName(int meta) {
        return this.getUnlocalizedName();
    }

    @Override
    public boolean isDouble() {
        return false;
    }

    @Override
    public IProperty<?> getVariantProperty() {
        return VARIANT;
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack itemStack) {
        return Variant.DEFAULT;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, Variant.DEFAULT).withProperty(HALF, meta == 0 ? BlockSlab.EnumBlockHalf.BOTTOM : BlockSlab.EnumBlockHalf.TOP);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(HALF) == EnumBlockHalf.BOTTOM ? 0 : 1;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT, HALF);
    }

    public enum Variant implements IStringSerializable {
        DEFAULT;

        public String getName() {
            return "default";
        }
    }
}
