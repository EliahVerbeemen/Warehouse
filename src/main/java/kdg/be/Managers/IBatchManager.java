package kdg.be.Managers;

import kdg.be.Models.Batch;
import kdg.be.Models.BatchState;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IBatchManager {

    public Optional<Batch> findBatchByDate(LocalDate localDate);

    public Batch save(Batch batch);

    public List<Batch> findBatchByState(BatchState batchState);

    public Batch saveOrUpdate(Batch batch);

    public Optional<Batch> findBatchById(Long batchId);
}
