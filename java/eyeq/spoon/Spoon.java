package eyeq.spoon;

import com.google.common.collect.Lists;
import eyeq.spoon.block.BlockSlabMelon;
import eyeq.spoon.item.ItemSpoon;
import eyeq.util.client.model.UModelCreator;
import eyeq.util.client.model.UModelLoader;
import eyeq.util.client.model.gson.ItemmodelJsonFactory;
import eyeq.util.client.renderer.ResourceLocationFactory;
import eyeq.util.client.renderer.block.statemap.StateMapper;
import eyeq.util.client.resource.ULanguageCreator;
import eyeq.util.client.resource.lang.LanguageResourceManager;
import eyeq.util.item.UItemSlab;
import eyeq.util.oredict.UOreDictionary;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.io.File;

import static eyeq.spoon.Spoon.MOD_ID;

@Mod(modid = MOD_ID, version = "1.0", dependencies = "after:eyeq_util")
@Mod.EventBusSubscriber
public class Spoon {
    public static final String MOD_ID = "eyeq_spoon";

    @Mod.Instance(MOD_ID)
    public static Spoon instance;

    private static final ResourceLocationFactory resource = new ResourceLocationFactory(MOD_ID);

    public static BlockSlab slabMelon;

    public static Item spoonWood;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        addRecipes();
        if(event.getSide().isServer()) {
            return;
        }
        renderBlockModels();
        renderItemModels();
        createFiles();
    }

    @SubscribeEvent
    protected static void registerBlocks(RegistryEvent.Register<Block> event) {
        slabMelon = (BlockSlab) new BlockSlabMelon().setHardness(1.0F).setUnlocalizedName("slabMelon");

        GameRegistry.register(slabMelon, resource.createResourceLocation("melon_slab"));
    }

    @SubscribeEvent
    protected static void registerItems(RegistryEvent.Register<Item> event) {
        spoonWood = new ItemSpoon(Item.ToolMaterial.WOOD).setUnlocalizedName("spoonWood");

        GameRegistry.register(new UItemSlab(slabMelon), slabMelon.getRegistryName());

        GameRegistry.register(spoonWood, resource.createResourceLocation("wood_spoon"));
    }

    public static void addRecipes() {
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(spoonWood),
                "X ", " Y", " Y",
                'X', UOreDictionary.OREDICT_PLANKS,
                'Y', UOreDictionary.OREDICT_STICK));
    }

    @SideOnly(Side.CLIENT)
    public static void renderBlockModels() {
        ModelLoader.setCustomStateMapper(slabMelon, new StateMapper(resource, null, "melon_slab", Lists.newArrayList(new IProperty[]{BlockSlabMelon.VARIANT})));
    }

    @SideOnly(Side.CLIENT)
    public static void renderItemModels() {
        UModelLoader.setCustomModelResourceLocation(slabMelon);

        UModelLoader.setCustomModelResourceLocation(spoonWood);
    }

    public static void createFiles() {
        File project = new File("../1.11.2-Spoon");

        LanguageResourceManager language = new LanguageResourceManager();

        language.register(LanguageResourceManager.EN_US, slabMelon, "Melon Slab");
        language.register(LanguageResourceManager.JA_JP, slabMelon, "スイカハーフブロック");

        language.register(LanguageResourceManager.EN_US, spoonWood, "Spoon");
        language.register(LanguageResourceManager.JA_JP, spoonWood, "スプーン");

        ULanguageCreator.createLanguage(project, MOD_ID, language);

        UModelCreator.createItemJson(project, spoonWood, ItemmodelJsonFactory.ItemmodelParent.GENERATED);
    }
}
