package kdg.be.Controllers;

import kdg.be.Managers.IBatchManager;
import kdg.be.Models.Batch;
import kdg.be.Models.BatchState;
import kdg.be.RabbitMQ.RabbitSender;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class BatchController {


    IBatchManager batchMgr;
private final RabbitSender rabbitSender;
    public BatchController(IBatchManager batchMgr, RabbitSender rabbitSender) {
        this.batchMgr = batchMgr;
        this.rabbitSender=rabbitSender;
    }


    @GetMapping("/batch")
    public ModelAndView ShowCurrentBatches(Model model){
        Batch notYetPrepared;
       List<Batch> optionalNotYetPreparedBatch= batchMgr.findBatchByState(BatchState.NotYetPrepared);
        if(optionalNotYetPreparedBatch.size()==0){

            notYetPrepared=  batchMgr.save(new Batch(BatchState.NotYetPrepared));


        }
        else{
            notYetPrepared=optionalNotYetPreparedBatch.get(0);
        }
        model.addAttribute("notYetPrepared",notYetPrepared);


        Batch inPreparation;
        List<Batch> optionalInPreparation= batchMgr.findBatchByState(BatchState.InPreparation);
        if(optionalInPreparation.size()==0){
            Batch inprep=new Batch(BatchState.InPreparation);
           Batch inprepreparation= batchMgr.save(inprep);
            model.addAttribute("inPreparation",inprepreparation);

        }
        else{
            model.addAttribute("inPreparation",optionalInPreparation);


        }


        return new ModelAndView("Batchen/Batch", (Map<String, ?>) model);

    }

@GetMapping("/batch/batchReady/{batchId}")
public String FinalizePreparation(Model model, @PathVariable int batchId){

     Optional<Batch>  optionalBatch= batchMgr.findBatchById((long) batchId);
if(optionalBatch.isPresent()){

    Batch batch=optionalBatch.get();
    batch.setBatchState(BatchState.Prepared);
    batchMgr.saveOrUpdate(batch);
    return "redirect:/batch";

}

    return "redirect:/batch";



}
    @GetMapping("/batch/startpreparing/{batchId}")
    public String StartPeparation(Model model, @PathVariable int batchId){

     Optional<Batch>optionalBatch=  batchMgr.findBatchById((long) batchId);
if(optionalBatch.isPresent()){

    Batch batch=optionalBatch.get();
    batch.setBatchState(BatchState.InPreparation);
   Batch batch1= batchMgr.saveOrUpdate(batch);
//TODO
   //    rabbitSender.sendBatch(batch1);

    return "redirect:/batch";
}


        return "redirect:/batch";


    }


    @Scheduled(cron = "0 0 22 ? * *")
    public void StartBakingAtTen(){

        batchMgr.findBatchByState(BatchState.NotYetPrepared).forEach(b->{

            b.setBatchState(BatchState.Prepared);
            batchMgr.saveOrUpdate(b);
        });


    }


  //  https://www.baeldung.com/spring-scheduled-tasks
    /*
    @GetMapping(value = {"/batch", "/batch"})
    //Werken met kleine letter
    public ModelAndView ShowBatch(@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate localdate) {
        System.out.println(localdate);
        ModelAndView modelAndView = new ModelAndView("Batchen/Batch");
        if (localdate == null) localdate = LocalDate.now();
        if (!batchMgr.findBatchByDate(localdate).isPresent()) {
            Batch batch = new Batch(localdate);
            batchMgr.save(batch);
            modelAndView.addObject("Batch", batch);

        } else {
            Batch batch = batchMgr.findBatchByDate(localdate).get();
            modelAndView.addObject("Batch", batch);
        }
        return modelAndView;
    }

*/



}
