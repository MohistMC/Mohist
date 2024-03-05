package com.mohistmc.api.bar;

import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class StringBar {

    private ChatColor full = ChatColor.GREEN;
    private ChatColor empty = ChatColor.RED;
    private double percent;
    private int sizeOfBar = 100;
    private char character = '|';
    private Sense sense = Sense.NORMAL;

    /**
     * Default method, it will set the percent of green bars
     * @param percent
     */

    public StringBar(double percent){
        this.percent = percent;
    }

    /**
     * It will set the percent of green bars and the size of bar
     * @param percent
     * @param sizeOfBar
     */

    public StringBar(double percent, int sizeOfBar){
        this.percent = percent;
        this.sizeOfBar = sizeOfBar;
    }

    /**
     * It will set the percent of green bars, the size of bar and the caratcter (default is '|' )
     * @param percent
     * @param sizeOfBar
     * @param character
     */

    public StringBar(double percent, int sizeOfBar, char character){
        this.percent = percent;
        this.sizeOfBar = sizeOfBar;
        this.character = character;
    }

    /**
     * It will set the percent of green bars and the caracter (default is '|' )
     * @param percent
     * @param character
     */

    public StringBar(double percent, char character){
        this.percent = percent;
        this.character = character;
    }

    /**
     * It will set the percent of green bars, the size of the total bar, the caracter, the full color (default is green) and the empty color (default id red)
     * @param percent
     * @param sizeOfBar
     * @param character
     * @param full
     * @param empty
     */

    public StringBar(double percent, int sizeOfBar, char character, ChatColor full, ChatColor empty){
        this.percent = percent;
        this.character = character;
    }

    /**
     * set the percent of the bar (if you want to make animations)
     * @param percent
     * @return
     */

    public StringBar setPercent(double percent){
        this.percent = percent;
        return this;
    }

    /**
     * set the full color (default is green)
     * @param color
     */

    public StringBar setFullColor(ChatColor color){
        this.full = color;
        return this;
    }

    /**
     * set the empty color (default is red)
     * @param color
     */

    public StringBar setEmptyColor(ChatColor color){
        this.empty = color;
        return this;
    }

    /**
     * set the size of the bar (default is 100)
     * @param size
     */

    public StringBar setSize(int size){
        this.sizeOfBar = size;
        return this;
    }

    /**
     * set the caracter of bar (default is '|')
     * @param character
     */

    public StringBar setCharacter(char character){
        this.character = character;
        return this;
    }

    /**
     * Return the current percent of the bar
     * @return
     */

    public double getPercent(){
        return this.percent;
    }

    /**
     * Return the current percent of the bar
     * @param bar
     * @return
     */

    public double getPercent(StringBar bar){
        return bar.getPercent();
    }

    /**
     * Return the current size of the bar
     * @return
     */

    public int getSize(){
        return this.sizeOfBar;
    }

    /**
     * Return the current size of the bar
     * @param bar
     * @return
     */

    public int getSize(StringBar bar){
        return bar.getSize();
    }

    /**
     * Return the current full color
     * @return
     */

    public ChatColor getFullColor(){
        return this.full;
    }

    /**
     * Return the current full color
     * @return
     */

    public ChatColor getFullColor(StringBar bar){
        return bar.getFullColor();
    }

    /**
     * Return the current empty color
     * @return
     */

    public ChatColor getEmptyColor(){
        return this.empty;
    }

    /**
     * Return the current empty color
     * @return
     */

    public ChatColor getEmptyColor(StringBar bar){
        return bar.getEmptyColor();
    }

    /**
     * Return the bar character (default is |)
     * @return
     */

    public char getCharacter() {
        return this.character;
    }

    /**
     * Return the bar character (default is |)
     * @return
     */

    public char getCharacter(StringBar bar) {
        return bar.getCharacter();
    }

    /**
     * Switch the full & the empty colors of the bar
     * @return
     */

    public StringBar switchColors(){
        ChatColor colorSwi = this.empty;
        this.empty = this.full;
        this.full = colorSwi;
        return this;
    }

    /**
     * Switch the full & the empty colors of the bar
     * @return barBuilder
     */

    public StringBar switchColors(StringBar barBuilder){
        ChatColor colorSwi = barBuilder.getEmptyColor();
        barBuilder.setEmptyColor(barBuilder.getFullColor());
        barBuilder.setFullColor(colorSwi);
        return barBuilder;
    }

    /**
     * Set the sense of the bar ( Sense.NORMAL Or Sense.REVERSE )
     * @param sense
     */

    public StringBar setSense(Sense sense) {
        this.sense = sense;
        return this;
    }

    /**
     * Invert the sense of the bar (red at the beggining and green at the end)
     * @return barBuilder
     */

    public StringBar invert(){
        if (this.getSense().equals(Sense.REVERSE)) {
            this.switchSense(Sense.NORMAL);
        } else {
            this.switchSense(Sense.REVERSE);
        }
        return this;
    }

    /**
     * Invert the sense of the bar (red at the beggining and green at the end)
     * @return barBuilder
     * @param barBuilder
     */

    public StringBar invert(StringBar barBuilder){
        if (barBuilder.getSense().equals(Sense.REVERSE)) {
            barBuilder.switchSense(Sense.NORMAL);
        } else {
            barBuilder.switchSense(Sense.REVERSE);
        }
        return barBuilder;
    }

    /**
     * Get the sense of the bar (Normal/Reversed)
     * @return
     */

    public Sense getSense() {
        return sense;
    }

    /**
     * Change the sense of the bar (normal & reversed)
     * @return barBuilder
     * @param sense
     */

    private StringBar switchSense(Sense sense){
        if(this.getSense() != sense){
            this.switchColors();
        }
        this.sense = sense;
        this.percent = 100 - this.percent;
        return this;
    }

    /**
     * Change the sense of the bar (normal & reversed)
     * @return
     * @param sense
     * @param barBuilder
     */

    private StringBar switchSense(Sense sense, StringBar barBuilder){
        if(barBuilder.getSense() != sense){
            barBuilder.switchColors();
        }
        barBuilder.switchSense(sense);
        return barBuilder;
    }

    /**
     * Send the bar in the chat to the player
     * @param player
     */

    public void sendToPlayer(Player player){
        player.sendMessage(this.build());
    }

    /**
     * Send the bar in the chat to the player
     * @param player
     * @param bar
     */

    public void sendToPlayer(Player player, StringBar bar){
        player.sendMessage(bar.build());
    }

    /**
     * Send the bar to the console
     */

    public void sendToConsole(){
        Bukkit.getConsoleSender().sendMessage(this.build());
    }

    /**
     * Send the bar to the console
     * @param bar
     */

    public void sendToConsole(StringBar bar){
        Bukkit.getConsoleSender().sendMessage(bar.build());
    }

    /**
     * Send the bar to all online players
     */

    public void sendToAllPlayers(){
        for(Player players : Bukkit.getOnlinePlayers()){
            sendToPlayer(players);
        }
    }

    /**
     * Send the bar to all online players
     * @param bar
     */

    public void sendToAllPlayers(StringBar bar){
        for(Player players : Bukkit.getOnlinePlayers()){
            sendToPlayer(players, bar);
        }
    }

    /**
     * Build the bar. Use this method after all ones !
     * @return build.toString();
     */

    public String build(){
        long completed = Math.round(this.sizeOfBar * (this.percent / 100));
        StringBuilder build = new StringBuilder();
        build.append(this.getFullColor());

        for(int i = 0; i < sizeOfBar; i++){
            build.append(i == completed ? this.getEmptyColor() : "").append(this.getCharacter());
        }

        return build.toString();
    }

    public enum Sense{
        NORMAL(0),
        REVERSE(1);

        private int id;

        Sense(int id){
            this.id = id;
        }

        /**
         * Get the id of a sense
         * @return
         */

        public int getId() {
            return id;
        }

        /**
         * Get enum value by the sense id (0 & 1)
         * @param id
         * @return
         */

        public static Sense getById(int id){
            return Arrays.stream(values()).filter(r -> r.getId() == id).findFirst().orElse(null);
        }
    }
}
