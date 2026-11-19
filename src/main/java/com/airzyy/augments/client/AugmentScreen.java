package com.airzyy.augments.client;
import com.airzyy.augments.network.OpenAugmentScreenPayload;
import com.airzyy.augments.network.SelectAugmentPayload;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.PacketDistributor;
import java.util.List;
import java.util.Locale;
public class AugmentScreen extends Screen {
 private final List<OpenAugmentScreenPayload.Card> cards;
 private int age;
 private boolean choosing;
 public AugmentScreen(List<OpenAugmentScreenPayload.Card> cards){super(Component.literal("Choose an Augment"));this.cards=List.copyOf(cards);}
 @Override protected void init(){super.init();}
 @Override public void tick(){super.tick();age++;}
 private int cardWidth(){return Math.min(210,(int)(width*.235));}
 private int cardHeight(){return Math.min(310,(int)(height*.72));}
 private int gap(){return 12;}
 private int totalWidth(){return cards.size()*cardWidth()+Math.max(0,cards.size()-1)*gap();}
 private int left(){return (width-totalWidth())/2;}
 private int top(){return (height-cardHeight())/2+12;}
 @Override public void render(GuiGraphics g,int mx,int my,float partial){
  g.fill(0,0,width,height,0x99060A12);
  g.drawCenteredString(font,"CHOOSE AN AUGMENT",width/2,Math.max(12,top()-34),0xFFFFFFFF);
  g.drawCenteredString(font,"SELECT ONE TO ADD IT TO YOUR BUILD",width/2,Math.max(25,top()-18),0xFFB9C4D2);
  for(int i=0;i<cards.size();i++){
   float reveal=Math.min(1f,Math.max(0f,(age+partial-i*7)/12f));
   if(reveal<=0)continue;
   int x=left()+i*(cardWidth()+gap());
   int y=top()+(int)((1-reveal)*26);
   float alpha=reveal;
   drawCard(g,cards.get(i),x,y,mx,my,alpha,i);
  }
  super.render(g,mx,my,partial);
 }
 private void drawCard(GuiGraphics g,OpenAugmentScreenPayload.Card c,int x,int y,int mx,int my,float alpha,int index){
  int w=cardWidth(),h=cardHeight();
  boolean hover=mx>=x&&mx<x+w&&my>=y&&my<y+h;
  int edge=color(c.rarity),panel=panel(c.rarity);
  int a=(int)(alpha*255)<<24;
  g.fill(x-2,y-2,x+w+2,y+h+2,(a)|(edge&0xFFFFFF));
  g.fill(x,y,x+w,y+h,(a)|(panel&0xFFFFFF));
  if(c.rarity.equals("EPIC")||c.rarity.equals("LEGENDARY")||c.rarity.equals("MYTHIC")){
   g.fillGradient(x+2,y+2,x+w-2,y+h/2,(a)|blend(edge,0x101522,0.15),(a)|blend(edge,0x202033,0.65));
   g.fillGradient(x+2,y+h/2,x+w-2,y+h-2,(a)|blend(edge,0x171827,0.55),(a)|blend(edge,0x090D16,0.8));
  }
  if(hover)g.fill(x+2,y+2,x+w-2,y+h-2,0x18FFFFFF);
  int text=(int)(alpha*255)<<24|0xFFFFFF;
  String rarity=c.rarity.toUpperCase(Locale.ROOT);
  g.drawCenteredString(font,rarity,x+w/2,y+13,text&0xFFFFFFFF);
  g.fill(x+14,y+34,x+w-14,y+35,(a)|edge);
  // Reserved icon area: custom assets are added in the next UI pass.
  g.drawCenteredString(font,c.name,x+w/2,y+48,(a)|0xFFFFFF);
  int yy=y+76;
  for(String line:font.split(Component.literal(c.value),w-24)){
   g.drawCenteredString(font,line,x+w/2,yy,(a)|edge);yy+=font.lineHeight+2;
  }
  yy+=10;
  for(String line:font.split(Component.literal(c.description==null?"":c.description),w-24)){
   if(yy>y+h-44)break;
   g.drawString(font,line,x+12,yy,(a)|0xD9E0EA,false);yy+=font.lineHeight+2;
  }
  String typeLabel=hover?"CLICK TO SELECT":"AUGMENT";
  g.drawCenteredString(font,typeLabel,x+w/2,y+h-26,(a)|(hover?0xFFFFFFFF:0xFFB8C2D0));
  if(c.rarity.equals("MYTHIC")&&age%16<8){
   g.fill(x+w-13,y+12,x+w-10,y+15,(a)|0xFFFFD5D5);
   g.fill(x+10,y+h-16,x+13,y+h-13,(a)|0xFFFFD5D5);
  }
 }
 private static int color(String r){return switch(r){case "UNCOMMON"->0xFF91C99B;case "RARE"->0xFF2468C8;case "EPIC"->0xFFB14DDB;case "LEGENDARY"->0xFFE2B84E;case "MYTHIC"->0xFFE34855;default->0xFFB9BEC7;};}
 private static int panel(String r){return switch(r){case "RARE"->0xFF10294C;case "EPIC"->0xFF281438;case "LEGENDARY"->0xFF382A12;case "MYTHIC"->0xFF3A141D;default->0xFF292D35;};}
 private static int blend(int a,int b,double t){int r=(int)(((a>>16)&255)*(1-t)+((b>>16)&255)*t),g=(int)(((a>>8)&255)*(1-t)+((b>>8)&255)*t),bl=(int)((a&255)*(1-t)+(b&255)*t);return (r<<16)|(g<<8)|bl;}
 @Override public boolean mouseClicked(double mx,double my,int button){
  if(button==0&&!choosing){for(int i=0;i<cards.size();i++){int x=left()+i*(cardWidth()+gap()),y=top();if(mx>=x&&mx<x+cardWidth()&&my>=y&&my<y+cardHeight()){choosing=true;PacketDistributor.sendToServer(new SelectAugmentPayload(cards.get(i).id()));Minecraft.getInstance().setScreen(null);return true;}}}
  return super.mouseClicked(mx,my,button);
 }
 @Override public boolean isPauseScreen(){return false;}
 @Override public boolean shouldCloseOnEsc(){return false;}
}