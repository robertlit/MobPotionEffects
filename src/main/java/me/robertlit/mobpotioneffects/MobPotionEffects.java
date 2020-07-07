package me.robertlit.mobpotioneffects;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Collection;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public final class MobPotionEffects extends JavaPlugin implements Listener {

    private Collection<PotionEffectType> effects;
    private Collection<EntityType> mobs;
    private int chance;
    private int lv2Chance;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        effects = getConfig().getStringList("effects").stream().map(PotionEffectType::getByName).filter(Objects::nonNull).collect(Collectors.toSet());
        mobs = getConfig().getStringList("mobs").stream().map(EntityType::valueOf).collect(Collectors.toSet());
        chance = getConfig().getInt("chance", 10);
        lv2Chance = getConfig().getInt("level-2-chance", 5);
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(ignoreCancelled = true)
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (!mobs.contains(event.getEntityType())) {
            return;
        }
        Random random = ThreadLocalRandom.current();
        for (PotionEffectType effectType : effects) {
            if (!(random.nextInt(100) < chance)) {
                continue;
            }
            int amplifier = 0;
            if (random.nextInt(100) < lv2Chance) {
                amplifier = 1;
            }
            event.getEntity().addPotionEffect(new PotionEffect(effectType, Integer.MAX_VALUE, amplifier, false, true));
        }
    }
}
