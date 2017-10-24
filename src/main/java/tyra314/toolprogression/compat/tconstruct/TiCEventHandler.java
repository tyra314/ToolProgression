package tyra314.toolprogression.compat.tconstruct;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import slimeknights.tconstruct.library.events.MaterialEvent;
import slimeknights.tconstruct.library.events.TinkerCraftingEvent;
import slimeknights.tconstruct.library.materials.HeadMaterialStats;
import slimeknights.tconstruct.library.modifiers.IModifier;
import slimeknights.tconstruct.library.tools.ToolNBT;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.library.utils.TinkerUtil;
import slimeknights.tconstruct.tools.modifiers.ModDiamond;
import slimeknights.tconstruct.tools.modifiers.ModEmerald;
import tyra314.toolprogression.ToolProgressionMod;
import tyra314.toolprogression.proxy.CommonProxy;

public class TiCEventHandler
{
    @SubscribeEvent
    public void handleMaterial(MaterialEvent.StatRegisterEvent<HeadMaterialStats> event)
    {
        String name = event.material.identifier;


        if (event.stats instanceof HeadMaterialStats)
        {
            HeadMaterialStats stats = (HeadMaterialStats) event.stats;

            CommonProxy.mats_config.getString(name, "material", String.valueOf(stats
                    .harvestLevel), name);

            ToolProgressionMod.logger.info("TiC Material registered: " + name);

            if (TiCMaterial.hasOverwrite(event.material.identifier))
            {
                int newLevel = TiCMaterial.getOverwrite(event.material.identifier);
                event.overrideResult(new HeadMaterialStats(stats.durability,
                        stats.miningspeed,
                        stats.attack,
                        newLevel));

                ToolProgressionMod.logger.info("TiC Material overwrite: " +
                                               String.valueOf(newLevel));
            }
        }
    }

    private int getToolHarvestLevel(ItemStack tinker_tool)
    {
        return TagUtil.getToolStats(tinker_tool.getTagCompound()).harvestLevel;
    }

    private void setToolHarvestLevel(ItemStack tinker_tool, int harvestLevel)
    {
        NBTTagCompound tag = tinker_tool.getTagCompound();
        ToolNBT toolData = TagUtil.getToolStats(tag);
        toolData.harvestLevel = harvestLevel;

        TagUtil.setToolTag(tag, toolData.get());
        tinker_tool.setTagCompound(tag);
    }

    @SubscribeEvent
    public void handleToolModifier(TinkerCraftingEvent.ToolModifyEvent event)
    {
        int harvestLevel = getToolHarvestLevel(event.getItemStack());

        for(IModifier mod :  TinkerUtil.getModifiers(event.getItemStack()))
        {
            if (mod instanceof ModDiamond || mod instanceof ModEmerald)
            {
                harvestLevel--;
            }
        }

        setToolHarvestLevel(event.getItemStack(), harvestLevel);
    }
}
