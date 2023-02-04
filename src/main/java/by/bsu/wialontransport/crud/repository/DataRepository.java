package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DataRepository extends JpaRepository<DataEntity, Long> {

    @Query("SELECT e FROM DataEntity e JOIN FETCH e.calculations WHERE e.tracker.id = :trackerId")
    Optional<DataEntity> findTrackerLastDataByTrackerId(final Long trackerId);
}
