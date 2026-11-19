package com.airzyy.augments;
import com.airzyy.augments.command.AugmentCommands;
import com.airzyy.augments.data.AugmentManager;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Mod(Augments.MOD_ID)
public final class Augments {
 public static final String MOD_ID="augments";
 public static final Logger LOGGER=LoggerFactory.getLogger(MOD_ID);
 public Augments(IEventBus bus) { bus.addListener(this::setup); NeoForge.EVENT_BUS.addListener(this::commands); NeoForge.EVENT_BUS.addListener(this::serverStart); }
 private void setup(FMLCommonSetupEvent e) { e.enqueueWork(AugmentManager::ensureConfigDirectory); }
 private void commands(RegisterCommandsEvent e) { AugmentCommands.register(e.getDispatcher()); }
 private void serverStart(ServerStartingEvent e) { AugmentManager.loadAll(); }
}