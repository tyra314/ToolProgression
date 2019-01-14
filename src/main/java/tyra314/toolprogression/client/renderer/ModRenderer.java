package tyra314.toolprogression.client.renderer;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.item.ModItems;

@SuppressWarnings("WeakerAccess")
@Mod.EventBusSubscriber
public class ModRenderer
{
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void registerModels(ModelRegistryEvent event)
    {
        ToolProgressionMod.logger.debug("Registering block models");

        ToolProgressionMod.logger.debug("Registering item models");

        for (Item item : ModItems.ITEMS)
        {
            if (item instanceof IModelRegister)
            {
                ((IModelRegister) item).registerModel();
            }
        }

        ToolProgressionMod.logger.debug("Registered models");
    }
}
