package com.airzyy.augments.data;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.*;
import net.neoforged.fml.loading.FMLPaths;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;
public final class AugmentManager {
 private static final Gson GSON=new GsonBuilder().setPrettyPrinting().create();
 private static final Type LIST_TYPE=new TypeToken<List<Augment>>(){}.getType();
 private static final Map<String,Augment> AUGMENTS=new LinkedHashMap<>();
 private static final Map<String,String> NAMES=new HashMap<>();
 private static final Random RANDOM=new Random();
 private static Path dir(){return FMLPaths.CONFIGDIR.get().resolve("augments");}
 private AugmentManager(){}
 public static void ensureConfigDirectory(){try{Files.createDirectories(dir());for(Augment.Rarity r:Augment.Rarity.values()){Path p=dir().resolve(r.fileName());if(!Files.exists(p))Files.writeString(p,"[]");}Path p=dir().resolve("attributes.json");if(!Files.exists(p))Files.writeString(p,"{}");}catch(IOException e){throw new IllegalStateException(e);}}
 public static synchronized void loadAll(){ensureConfigDirectory();AUGMENTS.clear();for(Augment.Rarity r:Augment.Rarity.values())try{List<Augment> l=GSON.fromJson(Files.readString(dir().resolve(r.fileName())),LIST_TYPE);if(l!=null)for(Augment a:l)AUGMENTS.put(a.id().toLowerCase(Locale.ROOT),a);}catch(Exception e){System.err.println("[Augments] "+e);}try{Type t=new TypeToken<Map<String,String>>(){}.getType();Map<String,String> m=GSON.fromJson(Files.readString(dir().resolve("attributes.json")),t);NAMES.clear();if(m!=null)NAMES.putAll(m);}catch(Exception ignored){}}
 public static synchronized void save(Augment a){AUGMENTS.put(a.id().toLowerCase(Locale.ROOT),a);write(dir().resolve(a.rarity().fileName()),GSON.toJson(AUGMENTS.values().stream().filter(x->x.rarity()==a.rarity()).toList()));}
 public static Augment get(String id){return AUGMENTS.get(id.toLowerCase(Locale.ROOT));}
 public static List<Augment> all(){return List.copyOf(AUGMENTS.values());}
 public static List<Augment> byRarity(Augment.Rarity r){return AUGMENTS.values().stream().filter(a->a.rarity()==r).toList();}
 public static synchronized void defineAttribute(String id,String name){NAMES.put(id,name);write(dir().resolve("attributes.json"),GSON.toJson(NAMES));}
 public static String displayName(String id){return NAMES.getOrDefault(id,id);}
 public static void apply(ServerPlayer p,Augment a){
  if(a.type()==Augment.Type.ATTRIBUTE){ResourceLocation key=ResourceLocation.tryParse(a.target());if(key==null)return;Attribute attr=BuiltInRegistries.ATTRIBUTE.get(key);AttributeInstance inst=p.getAttribute(attr);if(inst==null)return;ResourceLocation mid=ResourceLocation.fromNamespaceAndPath("augments",a.id());inst.removeModifier(mid);boolean pct="percent".equalsIgnoreCase(a.operation());double value=pct?a.amount()/100.0-1.0:a.amount();inst.addOrUpdateTransientModifier(new AttributeModifier(mid,value,pct?AttributeModifier.Operation.ADD_MULTIPLIED_BASE:AttributeModifier.Operation.ADD_VALUE));}
  else if(a.type()==Augment.Type.EFFECT){String[] x=a.target().split(",");ResourceLocation id=ResourceLocation.tryParse(x[0]);var effect=id==null?null:BuiltInRegistries.MOB_EFFECT.get(id);if(effect!=null)p.addEffect(new net.minecraft.world.effect.MobEffectInstance(effect,x.length>1?Integer.parseInt(x[1]):1200,x.length>2?Integer.parseInt(x[2]):0));}
  Set<String> owned=new HashSet<>(p.getPersistentData().getString("augments:owned").isBlank()?List.of():Arrays.asList(p.getPersistentData().getString("augments:owned").split(",")));owned.add(a.id());p.getPersistentData().putString("augments:owned",String.join(",",owned));
 }
 public static void removeFrom(ServerPlayer p,String id){Augment a=get(id);if(a!=null&&a.type()==Augment.Type.ATTRIBUTE){ResourceLocation key=ResourceLocation.tryParse(a.target());if(key!=null){AttributeInstance i=p.getAttribute(BuiltInRegistries.ATTRIBUTE.get(key));if(i!=null)i.removeModifier(ResourceLocation.fromNamespaceAndPath("augments",a.id()));}}Set<String>s=new HashSet<>(Arrays.asList(p.getPersistentData().getString("augments:owned").split(",")));s.remove(id.toLowerCase(Locale.ROOT));p.getPersistentData().putString("augments:owned",String.join(",",s));}
 public static List<Augment> roll(String rarity){
  List<Augment> result=new ArrayList<>();
  if(!rarity.equalsIgnoreCase("all")){List<Augment> pool=new ArrayList<>(byRarity(Augment.Rarity.parse(rarity)));Collections.shuffle(pool);return pool.stream().limit(3).collect(Collectors.toList());}
  Set<String> selected=new HashSet<>();
  int[] weights={500,280,130,60,25,5};
  Augment.Rarity[] tiers=Augment.Rarity.values();
  for(int slot=0;slot<3;slot++){
   List<Augment> candidates=List.of();
   for(int attempt=0;attempt<12&&candidates.isEmpty();attempt++){
    int total=Arrays.stream(weights).sum(),pick=RANDOM.nextInt(total),index=0;
    while(index<weights.length-1&&pick>=weights[index]){pick-=weights[index];index++;}
    candidates=byRarity(tiers[index]).stream().filter(a->!selected.contains(a.id())).toList();
   }
   if(candidates.isEmpty())candidates=all().stream().filter(a->!selected.contains(a.id())).toList();
   if(candidates.isEmpty())break;
   Augment chosen=candidates.get(RANDOM.nextInt(candidates.size()));selected.add(chosen.id());result.add(chosen);
  }
  return result;
 }
 private static void write(Path p,String s){try{Files.createDirectories(dir());Files.writeString(p,s);}catch(IOException e){throw new IllegalStateException(e);}}
}