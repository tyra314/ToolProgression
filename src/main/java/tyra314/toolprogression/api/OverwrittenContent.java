package tyra314.toolprogression.api;

import tyra314.toolprogression.harvest.*;
import java.util.*;
import net.minecraft.item.Item.ToolMaterial;


/**
 * Public API of ToolProgression
 *
 * The intent of this class is to provide an interface to other mods,
 * such that they can **READ** all overwrites and the mining levels used.
 *
 * While they may allow to change ToolProgression internals right now,
 * you shouldn't depend on that behavior!
 */
public class OverwrittenContent
{
    public static final Map<String,BlockOverwrite> blocks = new HashMap<>();
    public static final Map<String,ToolOverwrite> tools = new HashMap<>();
    public static final Map<ToolMaterial,MaterialOverwrite> materials = new HashMap<>();
    public static final Map<Integer, HarvestLevelName> mining_level = new HashMap<>();
}
