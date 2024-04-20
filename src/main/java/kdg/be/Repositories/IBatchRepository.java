package kdg.be.Repositories;

import kdg.be.Models.Batch;
import kdg.be.Models.BatchState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface IBatchRepository extends JpaRepository<Batch,Long> {

    public Optional<Batch> findBatchBybatchDate(LocalDate localDate);

    public List<Batch> findBatchByBatchState(BatchState batchState);

    public Optional<Batch> findBatchByBatchId(Long batchId);


}
