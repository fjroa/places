package places;
import java.util.List;

import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlaceRepository extends MongoRepository<Place, String>{
	
	List<Place> findByCity (String c);
	
	List<Place> findByPositionNear(Point p, Distance d);
}
