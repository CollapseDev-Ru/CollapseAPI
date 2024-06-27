package ru.collapsedev.collapseapi.builder;


import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.XParticle;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import ru.collapsedev.collapseapi.util.ColorUtil;
import ru.collapsedev.collapseapi.util.LocationUtil;
import ru.collapsedev.collapseapi.util.ParametersUtil;

import java.awt.*;
import java.util.Map;

public class ParticleBuilder {

    private final ParticleDisplay display;
    private final Location location;

    Color color;
    int size = 1;

    int count = 1;

    Vector offset = LocationUtil.getEmptyVector();

    ParticleShape shape = ParticleShape.NONE;

    String[] shapeSettings = new String[0];


    public ParticleBuilder(String line, Location location) {
        this.display = ParticleDisplay.of(XParticle.getParticle(line.split(" ")[0]));
        this.location = location;

        Map<String, String> params = ParametersUtil.parseParams(line);
        if (params.containsKey("color")) {
            this.color = ColorUtil.getColor(params.get("color"));
        }
        if (params.containsKey("size")) {
            this.size = Integer.parseInt(params.get("size"));
        }
        display.withColor(color, size);

        if (params.containsKey("count")) {
            this.count = Integer.parseInt(params.get("count"));
        }
        display.withCount(count);

        if (params.containsKey("offset")) {
            String[] args = params.get("offset").split(":");
            this.offset = LocationUtil.stringToVector(args);
        }
        location.add(offset);

        if (params.containsKey("shape")) {
            this.shape = ParticleShape.valueOf(params.get("shape").toUpperCase());
        }

        if (params.containsKey("shape-settings")) {
            this.shapeSettings = params.get("shape-settings").split(":");
        }

        display.withLocation(location);

        switch (shape) {
            case NONE: {
                display.spawn();
                break;
            }
            case SPHERE: {
                if (shapeSettings.length == 0) {
                    XParticle.sphere(2, 10, display);
                } else {
                    XParticle.sphere(Double.parseDouble(shapeSettings[0]), Double.parseDouble(shapeSettings[1]), display);
                }
                break;
            }
            case CYLINDER: {
                if (shapeSettings.length == 0) {
                    XParticle.cylinder(1.7, 1, 10, display);
                } else {
                    XParticle.cylinder(Double.parseDouble(shapeSettings[0]), Double.parseDouble(shapeSettings[1]),
                            Double.parseDouble(shapeSettings[2]), display);
                }
                break;

            }
        }
    }


    public enum ParticleShape {
        NONE,
        SPHERE,
        CYLINDER,
        ;
    }
}
