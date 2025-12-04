package com.camp.camping_service.utils;

import com.camp.camping_service.dto.select.SelectLandmarkListRecord;
import com.camp.camping_service.entities.Landmark;
import org.locationtech.jts.geom.*;

import java.util.List;

public class GeoUtil {
    public static Point getCenter(List<SelectLandmarkListRecord> landmarks){

        if(landmarks.isEmpty()){
            return null;
        }

        GeometryFactory geometryFactory = new GeometryFactory();

        if(landmarks.size() < 2){
            return geometryFactory.createPoint(new Coordinate(
                    landmarks.get(0).lng().doubleValue(),
                    landmarks.get(0).lat().doubleValue()
            ));
        }

        Coordinate[] coordinates = landmarks.stream()
                .map(l -> new Coordinate(l.lng().doubleValue(),l.lat().doubleValue()))
                .toList()
                .toArray(Coordinate[]::new);
        coordinates = closeRing(coordinates);
        Polygon polygon = geometryFactory.createPolygon(coordinates);

        Envelope envelope = polygon.getEnvelopeInternal();

        double x = (envelope.getMinX() + envelope.getMaxX()) / 2.0;
        double y = (envelope.getMinY() + envelope.getMaxY()) / 2.0;

        return geometryFactory.createPoint(new Coordinate(x,y));
    }

    private static Coordinate[] closeRing(Coordinate[] coords) {
        if (!coords[0].equals2D(coords[coords.length - 1])) {
            // copy array + append first point at end
            Coordinate[] closed = new Coordinate[coords.length + 1];
            System.arraycopy(coords, 0, closed, 0, coords.length);
            closed[coords.length] = coords[0];
            return closed;
        }
        return coords;
    }
}
