package kdg.be.Managers;

import kdg.be.DTO.OrdersFromClientDTO;
import kdg.be.Models.Batch;
import kdg.be.Models.BatchState;
import kdg.be.Models.Product;
import kdg.be.Repositories.IBatchRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class BatchManager implements IBatchManager {

    private final IBatchRepository iBatchRepository;

    public BatchManager(IBatchRepository iBatchRepository) {
        this.iBatchRepository = iBatchRepository;

    }

    @Override
    public Optional<Batch> findBatchByDate(LocalDate localDate) {
        return iBatchRepository.findBatchBybatchDate(localDate);
    }

    @Override
    public Batch save(Batch batch) {
        return iBatchRepository.save(batch);
    }

    @Override
    public List<Batch> findBatchByState(BatchState batchState) {
        return iBatchRepository.findBatchByBatchState(batchState);
    }


    public Batch saveOrUpdate(Batch batch) {
        if (iBatchRepository.existsById(batch.getBatchId())) {
          System.out.println("bestaat");
            Batch bestaandebatch = iBatchRepository.findById(batch.getBatchId()).get();
            for(Map.Entry<Product, Integer> entry: batch.getProductsinBatch().entrySet()){
                if(bestaandebatch.getProductsinBatch().containsKey(entry.getKey())){

                    bestaandebatch.getProductsinBatch().replace(entry.getKey(),bestaandebatch.getProductsinBatch().get(entry.getKey())+batch.getProductsinBatch().get(entry.getKey()));





                }
                else{
                    System.out.println("er zitten "+bestaandebatch.getProductsinBatch().size()+"in de batch");
System.out.println("entry key"+entry.getKey());
                    bestaandebatch.getProductsinBatch().put(entry.getKey(),entry.getValue());
                }




            }
            bestaandebatch.setProductsinBatch(batch.getProductsinBatch());
            bestaandebatch.setBatchState(batch.getBatchState());
          Batch batch1=  iBatchRepository.save(bestaandebatch);
            batch1.getProductsinBatch().forEach((e,k)->{
                System.out.println("batch printen");
                System.out.println(e.getName());
                System.out.println(k.doubleValue());


            });

           return iBatchRepository.save(bestaandebatch);

        } else {
// om te testen
            Batch batch1=  iBatchRepository.save(batch);
            batch1.getProductsinBatch().forEach((e,k)->{
             System.out.println("batch printen");
                System.out.println(e.getName());
                System.out.println(k.doubleValue());


            });
            System.out.println();
            return iBatchRepository.save(batch);


        }


    }
    @Override
    public Optional<Batch> findBatchById(Long batchId){

        return iBatchRepository.findBatchByBatchId(batchId);



    }


}
