package com.airzyy.augments.data;
import java.util.Locale;
public record Augment(String id,String name,Rarity rarity,Type type,String target,String operation,double amount,String description,String asset) {
 public enum Rarity { COMMON,UNCOMMON,RARE,EPIC,LEGENDARY,MYTHIC;
  public static Rarity parse(String s){return valueOf(s.toUpperCase(Locale.ROOT));}
  public String fileName(){return name().toLowerCase(Locale.ROOT)+".json";}
 }
 public enum Type { ATTRIBUTE,LOOT_TABLE,EFFECT,COMMAND,SPECIAL }
 public String displayValue(){
  if(type==Type.EFFECT){String[] e=target.split(",");String effect=e[0];String duration=e.length>1?e[1]:"1200";String level=e.length>2?e[2]:"0";return effect+" · "+duration+" ticks · "+(Integer.parseInt(level)+1)+" level";}
  if(type!=Type.ATTRIBUTE)return type.name().replace('_',' ');
  String stat=AugmentManager.displayName(target);
  if("percent".equalsIgnoreCase(operation)){double p=amount-100;return (p>=0?"+":"")+String.format(Locale.ROOT,"%.0f%%",p)+" "+stat;}
  return (amount>=0?"+":"")+String.format(Locale.ROOT,"%s",amount)+" "+stat;
 }
}