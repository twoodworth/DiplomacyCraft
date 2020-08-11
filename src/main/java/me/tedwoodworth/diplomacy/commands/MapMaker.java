package me.tedwoodworth.diplomacy.commands;

import me.tedwoodworth.diplomacy.Diplomacy;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunk;
import me.tedwoodworth.diplomacy.nations.DiplomacyChunks;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class MapMaker {

    private static MapMaker instance = null;
    private final File file = new File(Diplomacy.getInstance().getDataFolder(), "nationMap.png");
    private final File defaultFile = new File(Diplomacy.getInstance().getDataFolder(), "worldMap.png");
    private BufferedImage image;

    public void registerEvents() {
        Bukkit.getPluginManager().registerEvents(new MapMaker.EventListener(), Diplomacy.getInstance());
    }

    public static MapMaker getInstance() {
        if (instance == null) {
            instance = new MapMaker();
        }
        return instance;
    }

    public void paintChunk(DiplomacyChunk diplomacyChunk) {
        var chunk = diplomacyChunk.getChunk();
        var x = chunk.getX();
        var z = chunk.getZ();
        var nation = diplomacyChunk.getNation();
        var color = nation.getColor();
        var image = this.getImage();
        var defaultImage = this.getDefaultImage();
        for (var i = x * 16 + 256; i < x * 16 + 16 + 256; i++) {
            for (var j = z * 16 + 256; j < z * 16 + 16 + 256; j++) {
                var defaultRGB = defaultImage.getRGB(i, j);
                var defaultColor = Color.decode(String.valueOf(defaultRGB));
                var red = (color.getRed() * 3 + defaultColor.getRed() + 255 * 3) / 7;
                var green = (color.getGreen() * 3 + defaultColor.getGreen() + 255 * 3) / 7;
                var blue = (color.getBlue() * 3 + defaultColor.getBlue() + 255 * 3) / 7;
                var newColor = new Color(red, blue, green);
                image.setRGB(i, j, newColor.getRGB());
            }
        }
        var northChunk = chunk.getWorld().getChunkAt(x, z + 1);
        var eastChunk = chunk.getWorld().getChunkAt(x + 1, z);
        var southChunk = chunk.getWorld().getChunkAt(x, z - 1);
        var westChunk = chunk.getWorld().getChunkAt(x - 1, z);

        var northDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(northChunk);
        var eastDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(eastChunk);
        var southDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(southChunk);
        var westDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(westChunk);

        if (Objects.equals(nation, northDiplomacyChunk.getNation())) {
            var j = (z + 1) * 16 + 256;
            for (var i = x * 16 + 256; i < x * 16 + 16 + 256; i++) {
                var defaultRGB = defaultImage.getRGB(i, j);
                var defaultColor = Color.decode(String.valueOf(defaultRGB));
                var red = (color.getRed() * 3 + defaultColor.getRed() + 255 * 3) / 7;
                var green = (color.getGreen() * 3 + defaultColor.getGreen() + 255 * 3) / 7;
                var blue = (color.getBlue() * 3 + defaultColor.getBlue() + 255 * 3) / 7;
                var newColor = new Color(red, blue, green);
                image.setRGB(i, j, newColor.getRGB());
            }
        } else {
            var j = z * 16 + 15 + 256;
            for (var i = x * 16 + 256; i < x * 16 + 16 + 256; i++) {
                image.setRGB(i, j, color.getRGB());
            }
        }

        if (Objects.equals(nation, eastDiplomacyChunk.getNation())) {
            var i = (x + 1) * 16 + 256;
            for (var j = z * 16 + 256; j < z * 16 + 16 + 256; j++) {
                var defaultRGB = defaultImage.getRGB(i, j);
                var defaultColor = Color.decode(String.valueOf(defaultRGB));
                var red = (color.getRed() * 3 + defaultColor.getRed() + 255 * 3) / 7;
                var green = (color.getGreen() * 3 + defaultColor.getGreen() + 255 * 3) / 7;
                var blue = (color.getBlue() * 3 + defaultColor.getBlue() + 255 * 3) / 7;
                var newColor = new Color(red, blue, green);
                image.setRGB(i, j, newColor.getRGB());
            }
        } else {
            var i = x * 16 + 15 + 256;
            for (var j = z * 16 + 256; j < z * 16 + 16 + 256; j++) {
                image.setRGB(i, j, color.getRGB());
            }
        }

        if (Objects.equals(nation, southDiplomacyChunk.getNation())) {
            var j = (z - 1) * 16 + 15 + 256;
            for (var i = x * 16 + 256; i < x * 16 + 16 + 256; i++) {
                var defaultRGB = defaultImage.getRGB(i, j);
                var defaultColor = Color.decode(String.valueOf(defaultRGB));
                var red = (color.getRed() * 3 + defaultColor.getRed() + 255 * 3) / 7;
                var green = (color.getGreen() * 3 + defaultColor.getGreen() + 255 * 3) / 7;
                var blue = (color.getBlue() * 3 + defaultColor.getBlue() + 255 * 3) / 7;
                var newColor = new Color(red, blue, green);
                image.setRGB(i, j, newColor.getRGB());
            }
        } else {
            var j = z * 16 + 256;
            for (var i = x * 16 + 256; i < x * 16 + 16 + 256; i++) {
                image.setRGB(i, j, color.getRGB());
            }
        }
        if (Objects.equals(nation, westDiplomacyChunk.getNation())) {
            var i = (x - 1) * 16 + 15 + 256;
            for (var j = z * 16 + 256; j < z * 16 + 16 + 256; j++) {
                var defaultRGB = defaultImage.getRGB(i, j);
                var defaultColor = Color.decode(String.valueOf(defaultRGB));
                var red = (color.getRed() * 3 + defaultColor.getRed() + 255 * 3) / 7;
                var green = (color.getGreen() * 3 + defaultColor.getGreen() + 255 * 3) / 7;
                var blue = (color.getBlue() * 3 + defaultColor.getBlue() + 255 * 3) / 7;
                var newColor = new Color(red, blue, green);
                image.setRGB(i, j, newColor.getRGB());
            }
        } else {
            var i = x * 16 + 256;
            for (var j = z * 16 + 256; j < z * 16 + 16 + 256; j++) {
                image.setRGB(i, j, color.getRGB());
            }
        }

        var northEastChunk = chunk.getWorld().getChunkAt(x + 1, z + 1);
        var southEastChunk = chunk.getWorld().getChunkAt(x + 1, z - 1);
        var southWestChunk = chunk.getWorld().getChunkAt(x - 1, z - 1);
        var northWestChunk = chunk.getWorld().getChunkAt(x - 1, z + 1);

        var northEastDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(northEastChunk);
        var southEastDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(southEastChunk);
        var southWestDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(southWestChunk);
        var northWestDiplomacyChunk = DiplomacyChunks.getInstance().getDiplomacyChunk(northWestChunk);

        if (Objects.equals(nation, northDiplomacyChunk.getNation()) && Objects.equals(nation, northEastDiplomacyChunk.getNation()) && Objects.equals(nation, eastDiplomacyChunk.getNation())) {
            for (var i = x * 16 + 8 + 256; i < x * 16 + 16 + 8 + 256; i++) {
                for (var j = z * 16 + 8 + 256; j < z * 16 + 16 + 8 + 256; j++) {
                    var defaultRGB = defaultImage.getRGB(i, j);
                    var defaultColor = Color.decode(String.valueOf(defaultRGB));
                    var red = (color.getRed() * 3 + defaultColor.getRed() + 255 * 3) / 7;
                    var green = (color.getGreen() * 3 + defaultColor.getGreen() + 255 * 3) / 7;
                    var blue = (color.getBlue() * 3 + defaultColor.getBlue() + 255 * 3) / 7;
                    var newColor = new Color(red, blue, green);
                    image.setRGB(i, j, newColor.getRGB());
                }
            }

        } else {
            var i = x * 16 + 15 + 256;
            var j = z * 16 + 15 + 256;
            image.setRGB(i, j, color.getRGB());
            image.setRGB(i - 1, j, color.getRGB());
            image.setRGB(i, j - 1, color.getRGB());
        }

        if (Objects.equals(nation, eastDiplomacyChunk.getNation()) && Objects.equals(nation, southEastDiplomacyChunk.getNation()) && Objects.equals(nation, southDiplomacyChunk.getNation())) {
            for (var i = x * 16 + 8 + 256; i < x * 16 + 16 + 8 + 256; i++) {
                for (var j = z * 16 - 8 + 256; j < z * 16 + 16 - 8 + 256; j++) {
                    var defaultRGB = defaultImage.getRGB(i, j);
                    var defaultColor = Color.decode(String.valueOf(defaultRGB));
                    var red = (color.getRed() * 3 + defaultColor.getRed() + 255 * 3) / 7;
                    var green = (color.getGreen() * 3 + defaultColor.getGreen() + 255 * 3) / 7;
                    var blue = (color.getBlue() * 3 + defaultColor.getBlue() + 255 * 3) / 7;
                    var newColor = new Color(red, blue, green);
                    image.setRGB(i, j, newColor.getRGB());
                }
            }

        } else {
            var i = x * 16 + 15 + 256;
            var j = z * 16 + 256;
            image.setRGB(i, j, color.getRGB());
            image.setRGB(i - 1, j, color.getRGB());
            image.setRGB(i, j + 1, color.getRGB());
        }

        if (Objects.equals(nation, southDiplomacyChunk.getNation()) && Objects.equals(nation, southWestDiplomacyChunk.getNation()) && Objects.equals(nation, westDiplomacyChunk.getNation())) {
            for (var i = x * 16 - 8 + 256; i < x * 16 + 16 - 8 + 256; i++) {
                for (var j = z * 16 - 8 + 256; j < z * 16 + 16 - 8 + 256; j++) {
                    var defaultRGB = defaultImage.getRGB(i, j);
                    var defaultColor = Color.decode(String.valueOf(defaultRGB));
                    var red = (color.getRed() * 3 + defaultColor.getRed() + 255 * 3) / 7;
                    var green = (color.getGreen() * 3 + defaultColor.getGreen() + 255 * 3) / 7;
                    var blue = (color.getBlue() * 3 + defaultColor.getBlue() + 255 * 3) / 7;
                    var newColor = new Color(red, blue, green);
                    image.setRGB(i, j, newColor.getRGB());
                }
            }

        } else {
            var i = x * 16 + 256;
            var j = z * 16 + 256;
            image.setRGB(i, j, color.getRGB());
            image.setRGB(i + 1, j, color.getRGB());
            image.setRGB(i, j + 1, color.getRGB());
        }

        if (Objects.equals(nation, westDiplomacyChunk.getNation()) && Objects.equals(nation, northWestDiplomacyChunk.getNation()) && Objects.equals(nation, northDiplomacyChunk.getNation())) {
            for (var i = x * 16 - 8 + 256; i < x * 16 + 16 - 8 + 256; i++) {
                for (var j = z * 16 + 8 + 256; j < z * 16 + 16 + 8 + 256; j++) {
                    var defaultRGB = defaultImage.getRGB(i, j);
                    var defaultColor = Color.decode(String.valueOf(defaultRGB));
                    var red = (color.getRed() * 3 + defaultColor.getRed() + 255 * 3) / 7;
                    var green = (color.getGreen() * 3 + defaultColor.getGreen() + 255 * 3) / 7;
                    var blue = (color.getBlue() * 3 + defaultColor.getBlue() + 255 * 3) / 7;
                    var newColor = new Color(red, blue, green);
                    image.setRGB(i, j, newColor.getRGB());
                }
            }
        } else {
            var i = x * 16 + 256;
            var j = z * 16 + 15 + 256; //TODO fix messy borders
            image.setRGB(i, j, color.getRGB());
            image.setRGB(i + 1, j, color.getRGB());
            image.setRGB(i, j - 1, color.getRGB());
        }


        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void unpaintChunk(DiplomacyChunk diplomacyChunk) {
        var chunk = diplomacyChunk.getChunk();
        var x = chunk.getX();
        var z = chunk.getZ();
        var image = this.getImage();
        var defaultImage = this.getDefaultImage();
        for (var i = x * 16 + 256; i < x * 16 + 16 + 256; i++) {
            for (var j = z * 16 + 256; j < z * 16 + 16 + 256; j++) {
                var defaultRGB = defaultImage.getRGB(i, j);
                image.setRGB(i, j, defaultRGB);
            }
        }

        try {
            ImageIO.write(image, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private BufferedImage getDefaultImage() {
        try {
            image = ImageIO.read(defaultFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ADD DEFAULT WORLD MAP!!!!");
        }
        return image;
    }

    private BufferedImage getImage() {
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            image = this.getDefaultImage();
        }

        return image;
    }

    public void save() {
        try {
            ImageIO.write(this.getImage(), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class EventListener implements Listener {
        @EventHandler
        void onWorldSave(WorldSaveEvent event) {
            save();
        }
    }

}
