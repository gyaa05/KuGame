package objects;

import java.awt.*;

public class ColorEntity {
    public objects.Point point;

    public String name;

    public Color color;

    public ColorEntity(Point point, String name, Color color) {
        this.point = point;
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return this.name;
    }

    public Color getColor() {
        return this.color;
    }

    public Point getPoint() {
        return this.point;
    }

    public void setPoint(Point point) {this.point = point;}

    @Override
    public String toString() {
        return "ColorEntity{" +
                "point=" + point +
                ", name='" + name + '\'' +
                ", color=" + color +
                '}';
    }
}
