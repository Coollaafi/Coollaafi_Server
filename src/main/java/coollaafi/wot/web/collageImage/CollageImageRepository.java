package coollaafi.wot.web.collageImage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CollageImageRepository extends JpaRepository<CollageImage, Long> {

}