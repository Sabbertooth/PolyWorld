/*
 * Copyright 2014 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.polyworld.water;

import java.util.Random;

import org.terasology.math.geom.Rect2d;
import org.terasology.math.geom.Vector2d;

/**
 * TODO Type description
 * @author Martin Steiger
 */
public class RadialWaterDistribution implements WaterDistribution {

    private static final double ISLAND_FACTOR = 1.07;  // 1.0 means no small islands; 2.0 leads to a lot

    private final int bumps;
    private final double startAngle;
    private final double dipAngle;
    private final double dipWidth;

    private final Rect2d bounds;

    /**
     *
     */
    public RadialWaterDistribution(Rect2d bounds) {
        Random r = new Random(9782985378925l);

        this.bounds = bounds;

        bumps = r.nextInt(5) + 1;
        startAngle = r.nextDouble() * 2 * Math.PI;
        dipAngle = r.nextDouble() * 2 * Math.PI;
        dipWidth = r.nextDouble() * .5 + .2;
    }

    public boolean isWater(Vector2d p2) {
        double nx = (p2.getX() - bounds.minX()) / bounds.width();
        double ny = (p2.getY() - bounds.minY()) / bounds.height();
        Vector2d p = new Vector2d(2 * (nx - 0.5), 2 * (ny - 0.5));

        double angle = Math.atan2(p.getY(), p.getX());
        double length = 0.5 * (Math.max(Math.abs(p.getX()), Math.abs(p.getY())) + p.length());

        double r1 = 0.5 + 0.40 * Math.sin(startAngle + bumps * angle + Math.cos((bumps + 3) * angle));
        double r2 = 0.7 - 0.20 * Math.sin(startAngle + bumps * angle - Math.sin((bumps + 2) * angle));
        if (Math.abs(angle - dipAngle) < dipWidth
                || Math.abs(angle - dipAngle + 2 * Math.PI) < dipWidth
                || Math.abs(angle - dipAngle - 2 * Math.PI) < dipWidth) {
            r1 = 0.2;
            r2 = 0.2;
        }
        return !(length < r1 || (length > r1 * ISLAND_FACTOR && length < r2));
    }
}
