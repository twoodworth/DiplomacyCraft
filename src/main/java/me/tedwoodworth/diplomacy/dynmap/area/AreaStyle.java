//package me.tedwoodworth.diplomacy.dynmap.area;
//
//
//import org.dynmap.markers.MarkerIcon;
//
//import java.awt.*;
//
//public class AreaStyle {
//    private final String strokecolor;
//    private final double strokeopacity;
//    private final int strokeweight;
//    private final String fillcolor;
//    private final double fillopacity;
//    private final String homemarker;
//    private final boolean boost;
//
//    private MarkerIcon homeicon;
//
//    public String getStrokecolor() {
//        return strokecolor;
//    }
//
//    public double getStrokeopacity() {
//        return strokeopacity;
//    }
//
//    public int getStrokeweight() {
//        return strokeweight;
//    }
//
//    public String getFillcolor() {
//        return fillcolor;
//    }
//
//    public double getFillopacity() {
//        return fillopacity;
//    }
//
//    public String getHomemarker() {
//        return homemarker;
//    }
//
//    public boolean isBoost() {
//        return boost;
//    }
//
//    public MarkerIcon getHomeicon() {
//        return homeicon;
//    }
//
//    public AreaStyle(Color color) {
//        strokecolor = color.toString();
//        strokeopacity = 0.8;
//        strokeweight = 3;
//        fillcolor = color.toString();
//        fillopacity = 0.35;
//        homemarker = null;
//        boost = false;
//    }
//}