package tyra314.toolprogression.item;

import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import tyra314.toolprogression.ToolProgressionMod;

@Mod.EventBusSubscriber
@GameRegistry.ObjectHolder(ToolProgressionMod.MODID)
public class ModItems
{
    public static final BaseItem magic_mushroom = null;

    public static Item[] ITEMS = {
            new MagicMushroom("magic_mushroom"),
    };

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        ToolProgressionMod.logger.info("Registering items");

        for (Item item : ModItems.ITEMS)
        {
            ToolProgressionMod.logger.debug("Register item: " + item.getUnlocalizedName());
            event.getRegistry().register(item);
        }

        ToolProgressionMod.logger.info("Registered items");
    }
}
