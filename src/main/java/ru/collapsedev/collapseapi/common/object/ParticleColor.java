package ru.collapsedev.collapseapi.common.object;

import lombok.Getter;

@Getter
public class ParticleColor {
    double red, green, blue;

    private ParticleColor(double red, double green, double blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
    public static ParticleColor of(double red, double green, double blue) {
        return new ParticleColor(red, green, blue);
    }
}
