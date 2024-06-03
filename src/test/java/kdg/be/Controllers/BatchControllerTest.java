package kdg.be.Controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class BatchControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void showCurrentBatches() throws Exception {
        mockMvc.perform(get("/batch")).andExpect(status().isOk());
    }

    @Test
    void finalizePreparation() {
    }

    @Test
    void startPeparation() {
    }

    @Test
    void startBakingAtTen() {
    }
}