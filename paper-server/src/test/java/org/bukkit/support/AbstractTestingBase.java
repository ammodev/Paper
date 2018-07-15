package org.bukkit.support;

import com.google.common.collect.ImmutableList;
import java.util.Collections;
import java.util.List;
import net.minecraft.server.DispenserRegistry;
import net.minecraft.server.EnumResourcePackType;
import net.minecraft.server.ResourceManager;
import net.minecraft.server.ResourcePackVanilla;
import net.minecraft.server.TagRegistry;
import org.bukkit.Material;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.junit.Assert;

/**
 *  If you are getting: java.lang.ExceptionInInitializerError
 *    at net.minecraft.server.StatisticList.<clinit>(SourceFile:58)
 *    at net.minecraft.server.Item.<clinit>(SourceFile:252)
 *    at net.minecraft.server.Block.<clinit>(Block.java:577)
 *
 *  extend this class to solve it.
 */
public abstract class AbstractTestingBase {
    // Materials that only exist in block form (or are legacy)
    public static final List<Material> INVALIDATED_MATERIALS;

    static {
        DispenserRegistry.c();
        // Set up resource manager
        ResourceManager resourceManager = new ResourceManager(EnumResourcePackType.SERVER_DATA);
        // add tags for unit tests
        resourceManager.a(new TagRegistry());
        // Register vanilla pack
        resourceManager.a(Collections.singletonList(new ResourcePackVanilla("minecraft")));

        DummyServer.setup();
        DummyEnchantments.setup();

        ImmutableList.Builder<Material> builder = ImmutableList.builder();
        for (Material m : Material.values()) {
            if (m.isLegacy() || CraftMagicNumbers.getItem(m) == null) {
                builder.add(m);
            }
        }
        INVALIDATED_MATERIALS = builder.build();
        Assert.assertTrue("Expected 533 invalidated materials (got " + INVALIDATED_MATERIALS.size() + ")", INVALIDATED_MATERIALS.size() == 533);
    }
}
