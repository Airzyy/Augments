package com.airzyy.augments.command;
import com.airzyy.augments.data.*;
import com.airzyy.augments.network.AugmentNetwork;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import net.minecraft.commands.*;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import java.util.Locale;
public final class AugmentCommands {
 private AugmentCommands(){}
 public static void register(CommandDispatcher<CommandSourceStack>d){
  d.register(Commands.literal("augment").requires(s->s.hasPermission(2))
   .then(Commands.literal("create").then(Commands.argument("name",StringArgumentType.string()).then(Commands.argument("rarity",StringArgumentType.word())
    .then(Commands.literal("attribute").then(Commands.argument("attribute",ResourceLocationArgument.id()).then(Commands.argument("operation",StringArgumentType.word()).then(Commands.argument("amount",DoubleArgumentType.doubleArg()).executes(c->create(c.getSource(),StringArgumentType.getString(c,"name"),StringArgumentType.getString(c,"rarity"),Augment.Type.ATTRIBUTE,ResourceLocationArgument.getId(c,"attribute").toString(),StringArgumentType.getString(c,"operation"),DoubleArgumentType.getDouble(c,"amount")))))))
    .then(Commands.literal("effect").then(Commands.argument("effect",ResourceLocationArgument.id()).executes(c->create(c.getSource(),StringArgumentType.getString(c,"name"),StringArgumentType.getString(c,"rarity"),Augment.Type.EFFECT,ResourceLocationArgument.getId(c,"effect")+",1200,0","",0))))
    .then(Commands.literal("loot_table").then(Commands.argument("loot_table",ResourceLocationArgument.id()).executes(c->create(c.getSource(),StringArgumentType.getString(c,"name"),StringArgumentType.getString(c,"rarity"),Augment.Type.LOOT_TABLE,ResourceLocationArgument.getId(c,"loot_table").toString(),"",0))))
    .then(Commands.literal("command").then(Commands.argument("command",StringArgumentType.string()).executes(c->create(c.getSource(),StringArgumentType.getString(c,"name"),StringArgumentType.getString(c,"rarity"),Augment.Type.COMMAND,StringArgumentType.getString(c,"command"),"",0)))))))
   .then(Commands.literal("description").then(Commands.argument("id",StringArgumentType.word()).then(Commands.argument("description",StringArgumentType.greedyString()).executes(c->{Augment a=AugmentManager.get(StringArgumentType.getString(c,"id"));if(a==null)return fail(c.getSource(),"Unknown augment");AugmentManager.save(new Augment(a.id(),a.name(),a.rarity(),a.type(),a.target(),a.operation(),a.amount(),StringArgumentType.getString(c,"description"),a.asset()));return 1;}))))
   .then(Commands.literal("define").then(Commands.literal("attribute").then(Commands.argument("attribute",ResourceLocationArgument.id()).then(Commands.argument("display",StringArgumentType.string()).executes(c->{AugmentManager.defineAttribute(ResourceLocationArgument.getId(c,"attribute").toString(),StringArgumentType.getString(c,"display"));return 1;})))))
   .then(Commands.literal("define").then(Commands.literal("asset").then(Commands.argument("id",StringArgumentType.word()).then(Commands.argument("asset",StringArgumentType.string()).executes(c->{Augment a=AugmentManager.get(StringArgumentType.getString(c,"id"));if(a==null)return fail(c.getSource(),"Unknown augment");AugmentManager.save(new Augment(a.id(),a.name(),a.rarity(),a.type(),a.target(),a.operation(),a.amount(),a.description(),StringArgumentType.getString(c,"asset")));return 1;})))))
   .then(Commands.literal("give").then(Commands.argument("targets",EntityArgument.players()).then(Commands.argument("id",StringArgumentType.word()).executes(c->{Augment a=AugmentManager.get(StringArgumentType.getString(c,"id"));if(a==null)return fail(c.getSource(),"Unknown augment");for(ServerPlayer p:EntityArgument.getPlayers(c,"targets")){AugmentManager.apply(p,a);if(a.type()==Augment.Type.COMMAND)p.getServer().getCommands().performPrefixedCommand(p.createCommandSourceStack(),a.target());}return 1;}))))
   .then(Commands.literal("remove").then(Commands.argument("targets",EntityArgument.players()).then(Commands.argument("id",StringArgumentType.word()).executes(c->{for(ServerPlayer p:EntityArgument.getPlayers(c,"targets"))AugmentManager.removeFrom(p,StringArgumentType.getString(c,"id"));return 1;}))))
   .then(Commands.literal("clear").then(Commands.argument("targets",EntityArgument.players()).executes(c->{for(ServerPlayer p:EntityArgument.getPlayers(c,"targets"))for(String id:p.getPersistentData().getString("augments:owned").split(","))if(!id.isBlank())AugmentManager.removeFrom(p,id);return 1;})))
   .then(Commands.literal("roll").then(Commands.argument("targets",EntityArgument.players()).then(Commands.argument("rarity",StringArgumentType.word()).executes(c->{String rarity=StringArgumentType.getString(c,"rarity");try{if(!rarity.equalsIgnoreCase("all"))Augment.Rarity.parse(rarity);}catch(Exception ex){return fail(c.getSource(),"Rarity must be common, uncommon, rare, epic, legendary, mythic, or all.");}int opened=0;for(ServerPlayer p:EntityArgument.getPlayers(c,"targets")){var choices=AugmentManager.roll(rarity);if(!choices.isEmpty()){AugmentNetwork.open(p,choices);opened++;}}if(opened==0)return fail(c.getSource(),"No augments available for that rarity.");return opened;}))))
  );
 }
 private static int create(CommandSourceStack s,String n,String r,Augment.Type t,String target,String op,double amount){try{Augment.Rarity rarity=Augment.Rarity.parse(r);String id=n.toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_]+","_");AugmentManager.save(new Augment(id,n,rarity,t,target,op,amount,"",""));s.sendSuccess(()->Component.literal("Created augment "+n),true);return 1;}catch(Exception e){s.sendFailure(Component.literal("Invalid rarity or augment definition."));return 0;}}
 private static int fail(CommandSourceStack s,String m){s.sendFailure(Component.literal(m));return 0;}
}